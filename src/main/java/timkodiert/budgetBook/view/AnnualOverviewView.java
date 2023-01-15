package timkodiert.budgetBook.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javax.inject.Inject;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timkodiert.budgetBook.controller.FixedExpenseController;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.model.CumulativeExpense;
import timkodiert.budgetBook.domain.model.Expense;
import timkodiert.budgetBook.util.BoldTableColumn;
import timkodiert.budgetBook.util.BoldTableRow;
import timkodiert.budgetBook.util.CurrencyTableCell;
import timkodiert.budgetBook.util.StageBuilder;
import timkodiert.budgetBook.view.factory.EditExpenseViewFactory;
import timkodiert.budgetBook.view.widget.ExpenseDetailWidget;

public class AnnualOverviewView implements Initializable, View {

    private static List<String> MONTH_NAMES = List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");
    private static int CURRENT_YEAR = LocalDate.now().getYear();
    private static int START_YEAR = CURRENT_YEAR - 5;
    private static int END_YEAR = CURRENT_YEAR + 1;

    @FXML
    private BorderPane rootPane;
    @FXML
    private TableView<Expense> mainTable;
    @FXML
    private TableColumn<Expense, String> positionColumn;
    @FXML
    private ComboBox<Integer> displayYearComboBox;

    private List<TableColumn<Expense, Number>> monthColumns = new ArrayList<>();
    private TableColumn<Expense, Number> cumulativeColumn;

    private ExpenseDetailWidget expenseDetailWidget = new ExpenseDetailWidget();

    
    private FixedExpenseController fixedExpenseController;
    private EditExpenseViewFactory editExpenseViewFactory;

    @Inject
    public AnnualOverviewView(FixedExpenseController fixedExpenseController, EditExpenseViewFactory editExpenseViewFactory) {
        this.fixedExpenseController = fixedExpenseController;
        this.editExpenseViewFactory = editExpenseViewFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fixedExpenseController.loadAll();

        displayYearComboBox.getItems().addAll(IntStream.rangeClosed(START_YEAR, END_YEAR).boxed().toList());
        displayYearComboBox.getSelectionModel().select(Integer.valueOf(CURRENT_YEAR));
        displayYearComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            if(LocalDate.now().plusYears(1).getYear() == newValue) {
                fixedExpenseController.addNextYearToAllExpenses();
            }
            mainTable.refresh();
        });

        // Widget rechts einbinden
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ExpenseDetailWidget.fxml"));
            loader.setController(expenseDetailWidget);
            rootPane.setRight(loader.load());
        }
        catch(Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Widget konnte nicht geöffnet werden!");
            alert.showAndWait();
        }

        positionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPosition()));
        // Hätte gerne sowas wie in Python:
        // for i, month in enumerate(monthNames):
        //   ...
        // Java machts hier umständlich :/
        ListIterator<String> iterator = MONTH_NAMES.listIterator();
        while(iterator.hasNext()) {
            int index = iterator.nextIndex();
            String month = iterator.next();

            TableColumn<Expense, Number> tableColumn = new TableColumn<>(month);
            tableColumn.setPrefWidth(90);
            tableColumn.setResizable(false);
            tableColumn.getStyleClass().add("annual-overview-tablecolumn");
            tableColumn.setCellFactory(col -> new CurrencyTableCell<>());
            tableColumn.setCellValueFactory(cellData -> {
                Expense expense = cellData.getValue();
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
            Expense expense = cellData.getValue();
            int selectedYear = displayYearComboBox.getValue();
            return new ReadOnlyDoubleWrapper(expense.getValueForYear(selectedYear));
        });
        mainTable.getColumns().add(cumulativeColumn);
        // Kummulative Zeile
        Expense cumulativeExpense = new CumulativeExpense(fixedExpenseController.getAllExpenses(), START_YEAR, END_YEAR);

        mainTable.setRowFactory(tableView -> {
            TableRow<Expense> row = new BoldTableRow(PaymentType.CUMULATIVE);
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 1 && !row.isEmpty()) {
                    expenseDetailWidget.setExpense(row.getItem());
                }
                else if(event.getClickCount() == 2 && !row.isEmpty()) {
                    try {
                        Stage stage = StageBuilder.create()
                            .withModality(Modality.APPLICATION_MODAL)
                            .withFXMLResource("/fxml/EditExpense.fxml")
                            .withView(editExpenseViewFactory.create(fixedExpenseController.getExpense(row.getItem().getId())))
                            .build();
                        stage.show();
                    } catch(Exception e) {
                        e.printStackTrace();
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
