package timkodiert.budgetBook.view.widget;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import timkodiert.budgetBook.domain.model.Expense;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.view.View;

public class ExpenseDetailWidget implements View, Initializable {

    @FXML
    private Label positionLabel, noteLabel;

    private Expense expense;

    public ExpenseDetailWidget() {
        expense = new FixedExpense();
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
        initProperties();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProperties();
    }

    private void initProperties() {
        positionLabel.textProperty().bind(expense.getAdapter().positionProperty());
        noteLabel.textProperty().bind(expense.getAdapter().noteProperty());
    }
}
