package timkodiert.budgetbook.view.widget;

import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;
import javax.inject.Inject;

import atlantafx.base.theme.Styles;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import timkodiert.budgetbook.budget.BudgetService;
import timkodiert.budgetbook.budget.BudgetState;
import timkodiert.budgetbook.converter.BbCurrencyStringConverter;
import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.view.View;

import static timkodiert.budgetbook.util.ObjectUtils.nvl;

public class BudgetWidget implements Initializable, View {

    private static final double CRITICAL_BUDGET_LIMIT_FACTOR = 0.9;

    @FXML
    @Getter
    private Pane root;
    @FXML
    private Label budgetLabel;
    @FXML
    private ProgressBar budgetProgressBar;
    @FXML
    private Label budgetProgressLabel;

    @Getter
    private final ObjectProperty<Reference<CategoryDTO>> categoryProperty = new SimpleObjectProperty<>();
    @Getter
    private final ObjectProperty<YearMonth> selectedYearMonthProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<BudgetState> budgetStateProperty = new SimpleObjectProperty<>();

    private final BbCurrencyStringConverter currencyStringConverter;
    private final BudgetService budgetService;

    @Inject
    public BudgetWidget(BbCurrencyStringConverter currencyStringConverter, BudgetService budgetService) {
        this.currencyStringConverter = currencyStringConverter;
        this.budgetService = budgetService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        budgetStateProperty.bind(Bindings.createObjectBinding(this::loadBudgetState, categoryProperty, selectedYearMonthProperty));
        budgetLabel.textProperty().bind(Bindings.createStringBinding(() -> nvl(categoryProperty.get(), Reference::name), categoryProperty));
        budgetProgressBar.progressProperty().addListener((observable, oldVal, newVal) -> {
            boolean criticalLimitReached = newVal.doubleValue() >= CRITICAL_BUDGET_LIMIT_FACTOR;
            root.pseudoClassStateChanged(Styles.STATE_DANGER, criticalLimitReached);
        });
        budgetProgressBar.prefWidthProperty().bind(root.widthProperty());
        budgetProgressBar.progressProperty().bind(Bindings.createDoubleBinding(this::getProgress, budgetStateProperty));
        budgetProgressLabel.textProperty().bind(Bindings.createStringBinding(this::getProgressLabel, budgetStateProperty));
    }

    private @Nullable BudgetState loadBudgetState() {
        if (categoryProperty.get() == null || selectedYearMonthProperty.get() == null) {
            return null;
        }
        return budgetService.getBudgetState(categoryProperty.get(), selectedYearMonthProperty.get());
    }

    private String getProgressLabel() {
        if (budgetStateProperty.get() == null) {
            return "";
        }
        int budgetValue = budgetStateProperty.get().budgetValue();
        int categorySum = budgetStateProperty.get().usedBudgetValue();
        return String.format("%s / %s", currencyStringConverter.toString(categorySum), currencyStringConverter.toString(budgetValue));
    }

    private double getProgress() {
        if (budgetStateProperty.get() == null) {
            return 0.0;
        }
        int budgetValue = budgetStateProperty.get().budgetValue();
        int categorySum = budgetStateProperty.get().usedBudgetValue();
        return (double) categorySum / budgetValue;
    }
}
