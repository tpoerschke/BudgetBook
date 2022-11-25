package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import timkodiert.budgetBook.controller.FixedExpenseController;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.util.BoldTableColumn;
import timkodiert.budgetBook.util.BoldTableRow;
import timkodiert.budgetBook.util.CurrencyTableCell;

public class AnnualOverviewView implements Initializable {

    private static List<String> MONTH_NAMES = List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");
    private static int CURRENT_YEAR = 2022;

    @FXML
    private TableView<FixedExpense> mainTable;
    @FXML
    private TableColumn<FixedExpense, String> positionColumn;

    private List<TableColumn<FixedExpense, Number>> monthColumns = new ArrayList<>();
    private TableColumn<FixedExpense, Number> cumulativeColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        positionColumn.setCellValueFactory(cellData -> cellData.getValue().getAdapter().positionProperty());

        FixedExpenseController fixedExpenseController = new FixedExpenseController();
        fixedExpenseController.loadAll();

        // Hätte gerne sowas wie in Python:
        // for i, month in enumerate(monthNames):
        //   ...
        // Java machts hier umständlich :/
        ListIterator<String> iterator = MONTH_NAMES.listIterator();
        while(iterator.hasNext()) {
            int index = iterator.nextIndex();
            String month = iterator.next();

            TableColumn<FixedExpense, Number> tableColumn = new TableColumn<>(month);
            tableColumn.setPrefWidth(90);
            tableColumn.setResizable(false);
            tableColumn.getStyleClass().add("annual-overview-tablecolumn");
            tableColumn.setCellFactory(col -> new CurrencyTableCell<>());
            tableColumn.setCellValueFactory(cellData -> {
                FixedExpense expense = cellData.getValue();
                return new ReadOnlyDoubleWrapper(expense.getValueFor(CURRENT_YEAR, index+1));
            });
            monthColumns.add(tableColumn);
            mainTable.getColumns().add(tableColumn);
        }

        // Kummulative Spalte
        cumulativeColumn = new BoldTableColumn<>("Gesamt");
        cumulativeColumn.setPrefWidth(90);
        cumulativeColumn.setResizable(false);
        cumulativeColumn.setCellFactory(col -> new CurrencyTableCell<>());
        cumulativeColumn.setCellValueFactory(cellData -> {
            FixedExpense expense = cellData.getValue();
            return new ReadOnlyDoubleWrapper(expense.getCurrentYearValue());
        });
        mainTable.getColumns().add(cumulativeColumn);
        // Kummulative Zeile
        FixedExpense cumulativeExpense = new FixedExpense("Gesamt", 0, PaymentType.CUMULATIVE, IntStream.rangeClosed(1, 12).boxed().toList());
        IntStream.rangeClosed(1, 12).forEach(i -> {
            for(FixedExpense expense : fixedExpenseController.getAllExpenses()) {
                // TODO: Überarbeiten mit vernünftiger Schnittstelle
                double added = cumulativeExpense.getValueFor(CURRENT_YEAR, i) + expense.getValueFor(CURRENT_YEAR, i);
                cumulativeExpense.getPaymentInformations().get(0).getPayments().put(i, added);
            }
        });

        mainTable.setRowFactory(tableView -> {
            return new BoldTableRow(PaymentType.CUMULATIVE);
        });

        mainTable.getItems().addAll(fixedExpenseController.getAllExpenses());
        mainTable.getItems().add(cumulativeExpense);
    }
}
