package timkodiert.budgetBook.view.widget;

import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import lombok.Getter;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.MonthYear;
import timkodiert.budgetBook.view.View;

import static timkodiert.budgetBook.util.ObjectUtils.nvl;

public class BudgetWidget implements Initializable, View {

    @FXML
    @Getter
    private Pane root;
    @FXML
    private Label budgetLabel;
    @FXML
    private ProgressBar budgetProgressBar;

    @Getter
    private final ObjectProperty<Category> categoryProperty = new SimpleObjectProperty<>();
    @Getter
    private final ObjectProperty<MonthYear> selectedMonthYearProperty = new SimpleObjectProperty<>();

    @Inject
    public BudgetWidget() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        budgetLabel.textProperty().bind(Bindings.createStringBinding(() -> nvl(categoryProperty.get(), Category::getName), categoryProperty));
        budgetProgressBar.progressProperty().bind(Bindings.createDoubleBinding(this::getProgress, categoryProperty, selectedMonthYearProperty));
        budgetProgressBar.prefWidthProperty().bind(root.widthProperty());
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
