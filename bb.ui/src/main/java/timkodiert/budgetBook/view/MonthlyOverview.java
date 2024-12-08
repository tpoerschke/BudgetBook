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
import javax.inject.Provider;

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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.FixedTurnover;
import timkodiert.budgetBook.domain.model.MonthYear;
import timkodiert.budgetBook.domain.model.UniqueTurnover;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.table.RowType;
import timkodiert.budgetBook.table.cell.CurrencyTableCell;
import timkodiert.budgetBook.table.cell.DateTableCell;
import timkodiert.budgetBook.table.cell.GroupTableCell;
import timkodiert.budgetBook.table.row.BoldTableRow;
import timkodiert.budgetBook.table.row.ShortcutTableRow;
import timkodiert.budgetBook.view.monthly_overview.IconTableCell;
import timkodiert.budgetBook.view.monthly_overview.MonthlyOverviewCurrencyTableCell;
import timkodiert.budgetBook.view.monthly_overview.TableData;
import timkodiert.budgetBook.view.monthly_overview.ToTableDataMapper;
import timkodiert.budgetBook.view.widget.BudgetWidget;

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
    @FXML
    private TableColumn<TableData, TableData> iconCol;

    // BUDGETS
    @FXML
    private VBox budgetBox;

    private final ObservableList<TableData> data = FXCollections.observableArrayList();
    private final ObservableList<Category> categories = FXCollections.observableArrayList();

    private final Provider<FXMLLoader> fxmlLoader;
    private final LanguageManager languageManager;
    private final Repository<UniqueTurnover> uniqueExpenseRepository;
    private final Repository<FixedTurnover> fixedExpenseRepository;
    private final Repository<Category> categoryRepository;
    private final Provider<ShortcutTableRow<TableData>> shortcutTableRowProvider;
    private final MonthFilterFactory monthFilterFactory;

    @Inject
    public MonthlyOverview(Provider<FXMLLoader> fxmlLoader,
                           LanguageManager languageManager,
                           Repository<UniqueTurnover> uniqueExpenseRepository,
                           Repository<FixedTurnover> fixedExpenseRepository,
                           Repository<Category> categoryRepository,
                           Provider<ShortcutTableRow<TableData>> shortcutTableRowProvider,
                           MonthFilterFactory monthFilterFactory) {
        this.fxmlLoader = fxmlLoader;
        this.languageManager = languageManager;
        this.uniqueExpenseRepository = uniqueExpenseRepository;
        this.fixedExpenseRepository = fixedExpenseRepository;
        this.categoryRepository = categoryRepository;
        this.shortcutTableRowProvider = shortcutTableRowProvider;
        this.monthFilterFactory = monthFilterFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        monthFilter = monthFilterFactory.create(selectedMonthBox, selectedYearBox, nextMonthBtn, prevMonthBtn);
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
            double totalSum = data.stream().filter(d -> !RowType.getGroupTypes().contains(d.getType())).mapToDouble(TableData::value).sum();
            sumTable.getItems().clear();
            sumTable.getItems().add(new TableData(null,
                                                  languageManager.get("monthlyOverview.label.sumExpenses"),
                                                  totalSum,
                                                  null,
                                                  null,
                                                  false,
                                                  RowType.SUM));

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

            sumTable.getItems().addAll(new TableData(null, languageManager.get("monthlyOverview.label.sumEarnings"), incomeSum, null, null, false, RowType.SUM),
                                       new TableData(null, languageManager.get("monthlyOverview.label.sum"), incomeSum + totalSum, null, null, false, RowType.TOTAL_SUM));
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
            filteredData.setPredicate(d -> toShow.contains(d.getType()));
        };

        buttonCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        buttonCol.setCellFactory(col -> new GroupTableCell<>(dataGroupProperties));
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
        iconCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        iconCol.setCellFactory(col -> new IconTableCell(languageManager));

        dataTable.setRowFactory(tv -> shortcutTableRowProvider.get());
        dataTable.getColumns().forEach(col -> col.setReorderable(false));
        dataTable.setItems(filteredData);

        //
        // INIT FILTER
        //
        monthFilter.addListener((observable, oldValue, newValue) -> initDataGroups(newValue));

        //
        // INIT BUDGETS
        //
        categories.setAll(categoryRepository.findAll().stream().filter(Category::hasActiveBudget).toList());
        categories.forEach(cat -> {
            FXMLLoader loader = fxmlLoader.get();
            loader.setLocation(getClass().getResource(FxmlResource.BUDGET_WIDGET.getPath()));
            try {
                loader.load();
                BudgetWidget budgetWidget = loader.getController();
                budgetWidget.getCategoryProperty().set(cat);
                budgetWidget.getSelectedMonthYearProperty().bind(monthFilter);
                budgetBox.getChildren().add(budgetWidget.getRoot());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
                      languageManager.get("monthlyOverview.label.uniqueExpenses"), RowType.UNIQUE_EXPENSE_GROUP);
    }

    private void initFixedExpenseGroup(List<FixedTurnover> expenses) {
        MonthYear monthYear = monthFilter.getValue();
        initDataGroup(expenses,
                      exp -> ToTableDataMapper.mapFixedExpense(exp, monthYear),
                      exp -> exp.getValueFor(monthYear),
                      languageManager.get("monthlyOverview.label.fixedExpenses"), RowType.FIXED_EXPENSE_GROUP);
    }

    private <T> void initDataGroup(List<T> expenses, Function<T, TableData> expToData, ToDoubleFunction<T> expToTotalValue, String groupName, RowType groupRowType) {
        data.add(new TableData(null, groupName, expenses.stream().mapToDouble(expToTotalValue).sum(), null, null, false, groupRowType));
        data.addAll(expenses.stream().map(expToData).toList());
    }

    private class BudgetListCell extends ListCell<Category> {

        private BudgetWidget budgetWidget;

        BudgetListCell() {
            super();
            FXMLLoader loader = fxmlLoader.get();
            loader.setLocation(getClass().getResource(FxmlResource.BUDGET_WIDGET.getPath()));
            try {
                loader.load();
                budgetWidget = loader.getController();
                budgetWidget.getSelectedMonthYearProperty().bind(monthFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void updateItem(Category item, boolean empty) {

            if (empty || item == null || budgetWidget == null) {
                setGraphic(null);
                return;
            }

            budgetWidget.getCategoryProperty().set(item);
            setGraphic(budgetWidget.getRoot());
            //setHeight(budgetWidget.getRoot().getHeight());
        }
    }
}
