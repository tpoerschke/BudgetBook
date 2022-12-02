package timkodiert.budgetBook.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timkodiert.budgetBook.controller.FixedExpenseController;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.util.BoldTableColumn;
import timkodiert.budgetBook.util.BoldTableRow;
import timkodiert.budgetBook.util.CurrencyTableCell;
import timkodiert.budgetBook.util.StageBuilder;

public class AnnualOverviewView implements Initializable {

    private static List<String> MONTH_NAMES = List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");
    private static int CURRENT_YEAR = LocalDate.now().getYear();

    @FXML
    private TableView<FixedExpense> mainTable;
    @FXML
    private TableColumn<FixedExpense, String> positionColumn;
    @FXML
    private ComboBox<Integer> displayYearComboBox;

    private List<TableColumn<FixedExpense, Number>> monthColumns = new ArrayList<>();
    private TableColumn<FixedExpense, Number> cumulativeColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FixedExpenseController fixedExpenseController = new FixedExpenseController();
        fixedExpenseController.loadAll();

        displayYearComboBox.getItems().addAll(IntStream.rangeClosed(CURRENT_YEAR - 5, CURRENT_YEAR + 1).boxed().toList());
        displayYearComboBox.getSelectionModel().select(Integer.valueOf(CURRENT_YEAR));
        displayYearComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            if(LocalDate.now().plusYears(1).getYear() == newValue) {
                fixedExpenseController.addNextYearToAllExpenses();
            }
            mainTable.refresh();
        });

        positionColumn.setCellValueFactory(cellData -> cellData.getValue().getAdapter().positionProperty());
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
                int selectedYear = displayYearComboBox.getValue();
                return new ReadOnlyDoubleWrapper(expense.getValueFor(selectedYear, index+1));
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
            int selectedYear = displayYearComboBox.getValue();
            return new ReadOnlyDoubleWrapper(expense.getValueForYear(selectedYear));
        });
        mainTable.getColumns().add(cumulativeColumn);
        // Kummulative Zeile
        // FIXME: passt sich nicht, wenn das Jahr gewechselt wird
        FixedExpense cumulativeExpense = new FixedExpense("Gesamt", 0, PaymentType.CUMULATIVE, IntStream.rangeClosed(1, 12).boxed().toList());
        IntStream.rangeClosed(1, 12).forEach(i -> {
            for(FixedExpense expense : fixedExpenseController.getAllExpenses()) {
                // TODO: Überarbeiten mit vernünftiger Schnittstelle
                int selectedYear = displayYearComboBox.getValue();
                double added = cumulativeExpense.getValueFor(selectedYear, i) + expense.getValueFor(selectedYear, i);
                cumulativeExpense.getPaymentInformations().get(0).getPayments().put(i, added);
            }
        });

        mainTable.setRowFactory(tableView -> {
            TableRow<FixedExpense> row = new BoldTableRow(PaymentType.CUMULATIVE);
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && !row.isEmpty()) {
                    try {
                        Stage stage = StageBuilder.create()
                            .withModality(Modality.APPLICATION_MODAL)
                            .withFXMLResource("/fxml/EditExpense.fxml")
                            .withView(new EditExpenseView(row.getItem()))
                            .build();
                        stage.show();
                    } catch(Exception e) {
                        Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
                        alert.showAndWait();
                    }
                }
            });
            return row;
        });

        mainTable.getItems().addAll(fixedExpenseController.getAllExpenses());
        mainTable.getItems().add(cumulativeExpense);
    }
}
