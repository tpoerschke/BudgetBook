package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import timkodiert.budgetBook.domain.model.FixedExpense;

public class AnnualOverviewView implements Initializable {
    
    @FXML
    private TableView<FixedExpense> mainTable;
    @FXML
    private TableColumn<FixedExpense, String> positionColumn, typeColumn;

    private List<TableColumn<FixedExpense, Number>> monthColumns = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        positionColumn.setCellValueFactory(cellData -> cellData.getValue().getAdapter().positionProperty());
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().getType()));

        FixedExpenseController fixedExpenseController = new FixedExpenseController();
        fixedExpenseController.loadAll();

        List<String> monthNames = List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "November", "Dezember");

        // Hätte gerne sowas wie in Python:
        // for i, month in enumerate(monthNames):
        //   ...
        // Java machts hier umständlich :/
        ListIterator<String> iterator = monthNames.listIterator();
        while(iterator.hasNext()) {
            int index = iterator.nextIndex();
            String month = iterator.next();

            TableColumn<FixedExpense, Number> tableColumn = new TableColumn<>(month);
            tableColumn.setPrefWidth(90);
            tableColumn.setResizable(false);
            tableColumn.setCellValueFactory(cellData -> {
                FixedExpense expense = cellData.getValue();
                if(expense.getDatesOfPayment().contains(index+1)) {
                    return expense.getAdapter().valueProperty();
                }
                return new SimpleDoubleProperty(0.0);
            });
            monthColumns.add(tableColumn);
            mainTable.getColumns().add(tableColumn);
        }

        mainTable.getItems().addAll(fixedExpenseController.getAllExpenses());
    }
}
