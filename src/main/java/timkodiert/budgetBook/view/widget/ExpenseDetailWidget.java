package timkodiert.budgetBook.view.widget;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.view.View;

public class ExpenseDetailWidget implements View, Initializable {

    @FXML
    private Label positionLabel;
    @FXML
    private TextFlow noteTextFlow;
    @FXML
    private FlowPane categoriesFlow;

    private FixedExpense expense;

    public ExpenseDetailWidget() {
        expense = new FixedExpense();
    }

    public void setExpense(FixedExpense expense) {
        this.expense = expense;
        initProperties();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProperties();
    }

    private void initProperties() {
        positionLabel.textProperty().bind(expense.getAdapter().positionProperty());
        noteTextFlow.getChildren().setAll(new Text(expense.getNote()));

        categoriesFlow.getChildren().clear();
        expense.getCategories().forEach(cat -> {
            Label label = new Label(cat.getName());
            label.getStyleClass().add("tag");
            categoriesFlow.getChildren().add(label);
        });
    }
}
