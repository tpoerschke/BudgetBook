package timkodiert.budgetbook.view.widget;

import java.net.URL;
import java.util.ResourceBundle;

import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.view.View;

public class ExpenseDetailWidget implements View, Initializable {

    @FXML
    private Node root;
    @FXML
    private Label positionLabel;
    @FXML
    private TextFlow noteTextFlow;
    @FXML
    private FlowPane categoriesFlow;

    private FixedTurnover expense;

    public ExpenseDetailWidget() {
        expense = new FixedTurnover();
    }

    public void setExpense(FixedTurnover expense) {
        this.expense = expense;
        initProperties();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initProperties();
        initStyles();
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

    private void initStyles() {
        root.getStyleClass().add(Styles.BG_DEFAULT);
    }
}
