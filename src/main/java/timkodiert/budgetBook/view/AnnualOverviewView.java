package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import timkodiert.budgetBook.controller.FixedExpenseController;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.util.BoldTableColumn;
import timkodiert.budgetBook.util.BoldTableRow;

public class AnnualOverviewView implements Initializable {
    
    @FXML
    private TableView<FixedExpense> mainTable;
    @FXML
    private TableColumn<FixedExpense, String> positionColumn;

    private List<TableColumn<FixedExpense, String>> monthColumns = new ArrayList<>();
    private TableColumn<FixedExpense, String> cumulativeColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        positionColumn.setCellValueFactory(cellData -> cellData.getValue().getAdapter().positionProperty());

        FixedExpenseController fixedExpenseController = new FixedExpenseController();
        fixedExpenseController.loadAll();

        List<String> monthNames = List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");

        // Hätte gerne sowas wie in Python:
        // for i, month in enumerate(monthNames):
        //   ...
        // Java machts hier umständlich :/
        ListIterator<String> iterator = monthNames.listIterator();
        while(iterator.hasNext()) {
            int index = iterator.nextIndex();
            String month = iterator.next();

            TableColumn<FixedExpense, String> tableColumn = new TableColumn<>(month);
            tableColumn.setPrefWidth(90);
            tableColumn.setResizable(false);
            tableColumn.getStyleClass().add("annual-overview-tablecolumn");
            tableColumn.setCellValueFactory(cellData -> {
                FixedExpense expense = cellData.getValue();
                if(expense.getValueFor(2022, index+1) > 0) {
                    return new ReadOnlyStringWrapper(expense.getValueFor(2022, index+1) + "€");
                }
                return new ReadOnlyStringWrapper("-");
            });
            monthColumns.add(tableColumn);
            mainTable.getColumns().add(tableColumn);
        }

        // Kummulative Spalte
        cumulativeColumn = new BoldTableColumn<>("Gesamt");
        cumulativeColumn.setPrefWidth(90);
        cumulativeColumn.setResizable(false);
        cumulativeColumn.setCellValueFactory(cellData -> {
            FixedExpense expense = cellData.getValue();
            return new ReadOnlyStringWrapper(expense.getCurrentYearValue() + "€");
        });
        mainTable.getColumns().add(cumulativeColumn);
        // Kummulative Zeile
        FixedExpense cumulativeExpense = new FixedExpense("Gesamt", 0, PaymentType.CUMULATIVE, IntStream.rangeClosed(1, 12).boxed().toList());
        IntStream.rangeClosed(1, 12).forEach(i -> {
            for(FixedExpense expense : fixedExpenseController.getAllExpenses()) {
                // TODO: Überarbeiten mit vernünftiger Schnittstelle
                if(expense.getPaymentInformations().get(0).getMonthsOfPayment().contains(i)) {
                    double added = cumulativeExpense.getPaymentInformations().get(0).getValueFor(i) + expense.getPaymentInformations().get(0).getValueFor(i);
                    cumulativeExpense.getPaymentInformations().get(0).getPayments().put(i, added);
                }
            }
        });

        mainTable.setRowFactory(tableView -> {
            return new BoldTableRow(PaymentType.CUMULATIVE);
        });

        mainTable.getItems().addAll(fixedExpenseController.getAllExpenses());
        mainTable.getItems().add(cumulativeExpense);
    }
}
