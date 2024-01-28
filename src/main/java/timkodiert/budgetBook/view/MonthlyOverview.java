package timkodiert.budgetBook.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import javax.inject.Inject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import timkodiert.budgetBook.domain.model.FixedTurnover;
import timkodiert.budgetBook.domain.model.MonthYear;
import timkodiert.budgetBook.domain.model.UniqueTurnover;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.table.cell.CurrencyTableCell;
import timkodiert.budgetBook.table.cell.DateTableCell;
import timkodiert.budgetBook.table.cell.GroupTableCell;
import timkodiert.budgetBook.table.monthlyOverview.MonthlyOverviewCurrencyTableCell;
import timkodiert.budgetBook.table.monthlyOverview.TableData;
import timkodiert.budgetBook.table.monthlyOverview.ToTableDataMapper;
import timkodiert.budgetBook.table.row.BoldTableRow;

public class MonthlyOverview implements Initializable, View {

    // FILTER
    @FXML
    private Button nextMonthBtn, prevMonthBtn;
    @FXML
    private ComboBox<String> selectedMonthBox;
    @FXML
    private ComboBox<Integer> selectedYearBox;

    private MonthFilter monthFilter;

    // TABELLEN
    @FXML
    private TableView<TableData> dataTable, sumTable;
    @FXML
    private TableColumn<TableData, String> positionCol, categoriesCol, sumTableCol0, sumTableCol1;
    @FXML
    private TableColumn<TableData, Number> valueCol, sumTableCol2;
    @FXML
    private TableColumn<TableData, LocalDate> dateCol;
    @FXML
    private TableColumn<TableData, TableData> buttonCol;

    private ObservableList<TableData> data = FXCollections.observableArrayList();

    private final Repository<UniqueTurnover> uniqueExpenseRepository;
    private final Repository<FixedTurnover> fixedExpenseRepository;

    @Inject
    public MonthlyOverview(Repository<UniqueTurnover> uniqueExpenseRepository, Repository<FixedTurnover> fixedExpenseRepository) {
        this.uniqueExpenseRepository = uniqueExpenseRepository;
        this.fixedExpenseRepository = fixedExpenseRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthFilter = new MonthFilter(selectedMonthBox, selectedYearBox, nextMonthBtn, prevMonthBtn);
        //
        // INIT TABELLEN
        //
        sumTableCol0.prefWidthProperty().bind(buttonCol.widthProperty());
        sumTableCol1.prefWidthProperty().bind(positionCol.widthProperty());
        sumTableCol2.prefWidthProperty().bind(valueCol.widthProperty());

        sumTableCol1.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().position()));
        sumTableCol2.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().value()));
        sumTableCol2.setCellFactory(col -> new CurrencyTableCell<>());

        sumTable.setRowFactory(tableView -> new BoldTableRow<>(RowType.TOTAL_SUM));

        data.addListener((ListChangeListener.Change<? extends TableData> c) -> {
            double totalSum = data.stream()
                    .filter(d -> !RowType.getGroupTypes().contains(d.type()))
                    .mapToDouble(TableData::value).sum();
            sumTable.getItems().clear();
            sumTable.getItems().add(new TableData(LanguageManager.getInstance().getLocString("monthlyOverview.label.sum"), totalSum, null, null, RowType.SUM));

            // Summe der Einnahmen für den Monat
            double incomeSum = fixedExpenseRepository.findAll()
                                                     .stream()
                                                     .filter(t -> t.getValueFor(monthFilter.getValue()) > 0)
                                                     .mapToDouble(t -> t.getValueFor(monthFilter.getValue()))
                                                     .sum();
            incomeSum += uniqueExpenseRepository.findAll()
                                                .stream()
                                                .filter(t -> monthFilter.getValue().containsDate(t.getDate()))
                                                .filter(t -> t.getTotalValue() > 0)
                                                .mapToDouble(UniqueTurnover::getTotalValue)
                                                .sum();
            sumTable.getItems().add(new TableData("Einnahmen (Summe)", incomeSum, null, null, RowType.SUM));
            sumTable.getItems().add(new TableData("Summe", incomeSum + totalSum, null, null, RowType.TOTAL_SUM));
        });

        initDataGroups(MonthYear.now());
        FilteredList<TableData> filteredData = new FilteredList<>(data);

        SimpleBooleanProperty isUniqueCollapsedProperty = new SimpleBooleanProperty(false);
        SimpleBooleanProperty isFixedCollapsedProperty = new SimpleBooleanProperty(true);
        Map<RowType, BooleanProperty> dataGroupProperties = new HashMap<>();
        dataGroupProperties.put(RowType.FIXED_EXPENSE_GROUP, isFixedCollapsedProperty);
        dataGroupProperties.put(RowType.UNIQUE_EXPENSE_GROUP, isUniqueCollapsedProperty);
        Runnable predicator = () -> {
            List<RowType> toShow = new ArrayList<>(RowType.getGroupTypes());
            if (!isUniqueCollapsedProperty.get()) {
                toShow.add(RowType.UNIQUE_EXPENSE);
            }
            if (!isFixedCollapsedProperty.get()) {
                toShow.add(RowType.FIXED_EXPENSE);
            }
            filteredData.setPredicate(d -> toShow.contains(d.type()));
        };

        buttonCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        buttonCol.setCellFactory(col -> new GroupTableCell(dataGroupProperties));
        isUniqueCollapsedProperty.addListener((observable, oldValue, newValue) -> {
            predicator.run();
        });
        isFixedCollapsedProperty.addListener((observable, oldValue, newValue) -> {
            predicator.run();
        });
        predicator.run();

        positionCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().position()));
        valueCol.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().value()));
        valueCol.setCellFactory(col -> new MonthlyOverviewCurrencyTableCell(dataGroupProperties));
        dateCol.setCellValueFactory(cell -> new SimpleObjectProperty<LocalDate>(cell.getValue().date()));
        dateCol.setCellFactory(col -> new DateTableCell<>());
        categoriesCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().categories()));

        dataTable.getColumns().forEach(col -> col.setReorderable(false));
        dataTable.setItems(filteredData);

        //
        // INIT FILTER
        //
        monthFilter.addListener((observable, oldValue, newValue) -> initDataGroups(newValue));
    }

    private void initDataGroups(MonthYear monthYear) {
        data.clear();
        List<FixedTurnover> fixedExpensesForMonth = fixedExpenseRepository.findAll()
                                                                          .stream()
                                                                          .filter(exp -> exp.getValueFor(monthYear) < 0)
                                                                          .toList();
        initFixedExpenseGroup(fixedExpensesForMonth);

        List<UniqueTurnover> uniqueExpensesForMonth = uniqueExpenseRepository.findAll()
                                                                             .stream()
                                                                             .filter(exp -> monthYear.containsDate(exp.getDate()))
                                                                             .filter(exp -> exp.getTotalValue() < 0)
                                                                             .toList();
        initUniqueExpenseGroup(uniqueExpensesForMonth);
    }

    private void initUniqueExpenseGroup(List<UniqueTurnover> expenses) {
        initDataGroup(expenses, ToTableDataMapper::mapUniqueExpense, UniqueTurnover::getTotalValue,
                      LanguageManager.getInstance().getLocString("monthlyOverview.label.uniqueExpenses"), RowType.UNIQUE_EXPENSE_GROUP);
    }

    private void initFixedExpenseGroup(List<FixedTurnover> expenses) {
        MonthYear monthYear = monthFilter.getValue();
        initDataGroup(expenses,
                exp -> ToTableDataMapper.mapFixedExpense(exp, monthYear),
                exp -> exp.getValueFor(monthYear),
                LanguageManager.getInstance().getLocString("monthlyOverview.label.fixedExpenses"), RowType.FIXED_EXPENSE_GROUP);
    }

    private <T> void initDataGroup(List<T> expenses, Function<T, TableData> expToData,
            ToDoubleFunction<T> expToTotalValue, String groupName, RowType groupRowType) {
        data.add(new TableData(groupName, expenses.stream().mapToDouble(expToTotalValue).sum(),
                null, null, groupRowType));
        data.addAll(expenses.stream().map(expToData).toList());
    }

    public enum RowType {
        UNIQUE_EXPENSE, FIXED_EXPENSE, UNIQUE_EXPENSE_GROUP, FIXED_EXPENSE_GROUP, IMPORT, SUM, TOTAL_SUM;

        public static List<RowType> getGroupTypes() {
            return List.of(UNIQUE_EXPENSE_GROUP, FIXED_EXPENSE_GROUP);
        }
    }
}
