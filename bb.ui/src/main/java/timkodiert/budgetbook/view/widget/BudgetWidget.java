package timkodiert.budgetbook.view.widget;

import java.net.URL;
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

import timkodiert.budgetbook.converter.DoubleCurrencyStringConverter;
import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.view.View;

import static timkodiert.budgetbook.util.ObjectUtils.nvl;

public class BudgetWidget implements Initializable, View {

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
    private final ObjectProperty<Category> categoryProperty = new SimpleObjectProperty<>();
    @Getter
    private final ObjectProperty<MonthYear> selectedMonthYearProperty = new SimpleObjectProperty<>();

    private final DoubleCurrencyStringConverter currencyStringConverter;

    @Inject
    public BudgetWidget(DoubleCurrencyStringConverter currencyStringConverter) {
        this.currencyStringConverter = currencyStringConverter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        budgetLabel.textProperty().bind(Bindings.createStringBinding(() -> nvl(categoryProperty.get(), Category::getName), categoryProperty));
        budgetProgressBar.progressProperty().addListener((observable, oldVal, newVal) -> {
            root.pseudoClassStateChanged(Styles.STATE_DANGER, newVal.doubleValue() >= 0.9);
        });
        budgetProgressBar.progressProperty().bind(Bindings.createDoubleBinding(this::getProgress, categoryProperty, selectedMonthYearProperty));
        budgetProgressBar.prefWidthProperty().bind(root.widthProperty());
        budgetProgressLabel.textProperty().bind(Bindings.createStringBinding(this::getProgressLabel, categoryProperty, selectedMonthYearProperty));
    }

    private String getProgressLabel() {
        if (categoryProperty.get() == null || !categoryProperty.get().hasActiveBudget() || selectedMonthYearProperty.get() == null) {
            return "";
        }
        int budgetValue = categoryProperty.get().getBudgetValue();
        int categorySum = Math.abs(categoryProperty.get().sumTurnovers(selectedMonthYearProperty.get()));
        return String.format("%s / %s", currencyStringConverter.toString(categorySum), currencyStringConverter.toString(budgetValue));
    }

    private double getProgress() {
        if(categoryProperty.get() == null || !categoryProperty.get().hasActiveBudget() || selectedMonthYearProperty.get() == null) {
            return 0;
        }
        double budgetValue = categoryProperty.get().getBudgetValue();
        double categorySum = categoryProperty.get().sumTurnovers(selectedMonthYearProperty.get());
        return Math.abs(categorySum) / budgetValue;
    }
}
