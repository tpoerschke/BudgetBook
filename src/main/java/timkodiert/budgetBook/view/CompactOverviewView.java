package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import timkodiert.budgetBook.controller.FixedExpenseController;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;
import timkodiert.budgetBook.util.CurrencyTableCell;

public class CompactOverviewView implements Initializable, View {

    @FXML
    private TableColumn<ExpenseAdapter, String> monthlyPositionCol, monthlyTypeCol, currentMonthPositionCol, currentMonthTypeCol, nextMonthPositionCol, nextMonthTypeCol;
    @FXML
    private TableColumn<ExpenseAdapter, Double> monthlyValueCol, currentMonthValueCol, nextMonthValueCol;
    @FXML
    private TableView<ExpenseAdapter> monthlyTable, currentMonthTable, nextMonthTable;

    @FXML 
    private Label monthlySumLabel, monthlySumLabel1, monthlySumLabel2, currentMonthSumLabel, currentMonthTotalSumLabel, nextMonthSumLabel, nextMonthTotalSumLabel;

    private FixedExpenseController fixedExpenseController;

    @Inject
    public CompactOverviewView(FixedExpenseController fixedExpenseController) {
        this.fixedExpenseController = fixedExpenseController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {        
        monthlyPositionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        monthlyPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        monthlyValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("currentMonthValue"));
        monthlyValueCol.setCellFactory(col -> new CurrencyTableCell<>());
        monthlyTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().paymentTypeProperty().get().getType()));

        currentMonthPositionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        currentMonthPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        currentMonthValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("currentMonthValue"));
        currentMonthValueCol.setCellFactory(col -> new CurrencyTableCell<>());
        currentMonthTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().paymentTypeProperty().get().getType()));

        nextMonthPositionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        nextMonthPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nextMonthValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("nextMonthValue"));
        nextMonthValueCol.setCellFactory(col -> new CurrencyTableCell<>());
        nextMonthTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().paymentTypeProperty().get().getType()));

        // Ausgaben anzeigen
        monthlyTable.setItems(fixedExpenseController.getMonthlyExpenses());
        currentMonthTable.setItems(fixedExpenseController.getCurrentMonthExpenses());
        nextMonthTable.setItems(fixedExpenseController.getNextMonthExpenses());

        // Summen
        monthlySumLabel.textProperty().bind(fixedExpenseController.monthlyExpensesSumTextProperty());
        monthlySumLabel1.textProperty().bind(fixedExpenseController.monthlyExpensesSumTextProperty());
        monthlySumLabel2.textProperty().bind(fixedExpenseController.monthlyExpensesSumTextProperty());
        currentMonthSumLabel.textProperty().bind(fixedExpenseController.currentMonthExpensesSumTextProperty());
        currentMonthTotalSumLabel.textProperty().bind(fixedExpenseController.currentMonthExpensesTotalSumTextProperty());
        nextMonthSumLabel.textProperty().bind(fixedExpenseController.nextMonthExpensesSumTextProperty());
        nextMonthTotalSumLabel.textProperty().bind(fixedExpenseController.nextMonthExpensesTotalSumTextProperty());
    }

    private void reloadData() {
        // TODO: Über Dependency Injection müsste das doch funktionieren, oder?
        // Wenn sich alle Views den Controller teilen, könnte man hier aktualisieren
        // und es wirkt sich auf alle anderen Views aus.
        fixedExpenseController.loadAll();
        fixedExpenseController.addNextYearToAllExpenses();
    }
}
