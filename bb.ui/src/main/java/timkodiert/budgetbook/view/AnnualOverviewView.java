package timkodiert.budgetbook.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
import javax.inject.Inject;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.theme.Styles;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import timkodiert.budgetbook.annual_overview.AnnualOverviewDTO;
import timkodiert.budgetbook.annual_overview.AnnualOverviewService;
import timkodiert.budgetbook.annual_overview.TableRowData;
import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.domain.model.CumulativeExpense;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.IFixedTurnover;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.PaymentType;
import timkodiert.budgetbook.domain.repository.Repository;
import timkodiert.budgetbook.exception.TechnicalException;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.table.cell.CurrencyTableCell;
import timkodiert.budgetbook.table.row.BoldTableRow;
import timkodiert.budgetbook.util.CollectionUtils;
import timkodiert.budgetbook.view.widget.ExpenseDetailWidget;

import static timkodiert.budgetbook.view.FxmlResource.EXPENSE_DETAIL_WIDGET;

public class AnnualOverviewView implements Initializable, View {

    private static final int CURRENT_YEAR = LocalDate.now().getYear();
    private static final int START_YEAR = CURRENT_YEAR - 5;
    private static final int END_YEAR = CURRENT_YEAR + 1;

    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<IFixedTurnover> mainTable;
    @FXML
    private TableColumn<IFixedTurnover, String> positionColumn;
    @FXML
    private ComboBox<Integer> displayYearComboBox;

    @FXML
    private TableView<TableRowData> topTable;
    @FXML
    private TableColumn<TableRowData, String> labelColumn;

    private List<TableColumn<IFixedTurnover, Number>> monthColumns = new ArrayList<>();
    private TableColumn<IFixedTurnover, Number> cumulativeColumn;

    private final ModalPane modalPane = new ModalPane(ModalPane.Z_FRONT);
    private final ExpenseDetailWidget expenseDetailWidget = new ExpenseDetailWidget();
    private Pane turnoverWidgetNode;

    private final ObservableList<FixedTurnover> fixedTurnovers = FXCollections.observableArrayList();

    private final LanguageManager languageManager;
    private final Repository<FixedTurnover> fixedTurnoverRepository;

    private final AnnualOverviewService annualOverviewService;

    @Inject
    public AnnualOverviewView(LanguageManager languageManager, Repository<FixedTurnover> fixedTurnoverRepository, AnnualOverviewService annualOverviewService) {
        this.languageManager = languageManager;
        this.fixedTurnoverRepository = fixedTurnoverRepository;
        this.annualOverviewService = annualOverviewService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        displayYearComboBox.getItems().addAll(IntStream.rangeClosed(START_YEAR, END_YEAR).boxed().toList());
        displayYearComboBox.getSelectionModel().select(Integer.valueOf(CURRENT_YEAR));

        labelColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(languageManager.get(cellData.getValue().label())));

        CollectionUtils.enumerate(LanguageManager.MONTH_NAMES).forEach(indexValue -> {
            int index = indexValue.i() + 1;
            String month = languageManager.get(indexValue.value());

            TableColumn<TableRowData, Number> tableColumn = new TableColumn<>(month);
            tableColumn.setPrefWidth(120);
            tableColumn.setResizable(false);
            tableColumn.setCellFactory(col -> new CurrencyTableCell<>());
            tableColumn.setCellValueFactory(cellData -> {
                TableRowData rowData = cellData.getValue();
                return new ReadOnlyDoubleWrapper(rowData.monthValueMap().getOrDefault(index, 0));
            });
            topTable.getColumns().add(tableColumn);
        });

        AnnualOverviewDTO viewData = annualOverviewService.generateOverview(displayYearComboBox.getSelectionModel().getSelectedItem());
        topTable.getItems().add(viewData.earningsSum());
        topTable.getItems().add(viewData.expensesSum());
        topTable.getItems().add(viewData.totalSum());
        topTable.getStyleClass().add(Styles.BORDERED);

        // ------------------------------------------------------
        fixedTurnovers.setAll(fixedTurnoverRepository.findAll());

        displayYearComboBox.getSelectionModel().selectedItemProperty()
                           .addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
                               mainTable.refresh();
                           });

        // Widget rechts einbinden
        try {
            FXMLLoader loader = FxmlResource.loadResourceIntoFxmlLoader(getClass(), EXPENSE_DETAIL_WIDGET);
            loader.setResources(languageManager.getResourceBundle());
            loader.setController(expenseDetailWidget);
            turnoverWidgetNode = loader.load();
        } catch (Exception e) {
            throw TechnicalException.forFxmlNotFound(e);
        }

        // ModalPane konfigurieren
        modalPane.setAlignment(Pos.TOP_RIGHT);
        modalPane.usePredefinedTransitionFactories(Side.RIGHT);
        rootPane.getChildren().add(modalPane);

        positionColumn.setCellValueFactory(cellData -> {
            IFixedTurnover turnover = cellData.getValue();
            if (turnover.getType() == PaymentType.CUMULATIVE) {
                return new ReadOnlyStringWrapper(languageManager.get("domain.term.total"));
            }
            return new ReadOnlyStringWrapper(turnover.getPosition());
        });
        // Hätte gerne sowas wie in Python:
        // for i, month in enumerate(monthNames):
        //   ...
        // Java machts hier umständlich :/
        ListIterator<String> iterator = LanguageManager.MONTH_NAMES.listIterator();
        while (iterator.hasNext()) {
            int index = iterator.nextIndex();
            String month = languageManager.get(iterator.next());

            TableColumn<IFixedTurnover, Number> tableColumn = new TableColumn<>(month);
            tableColumn.setPrefWidth(120);
            tableColumn.setResizable(false);
            tableColumn.setCellFactory(col -> new CurrencyTableCell<>());
            tableColumn.setCellValueFactory(cellData -> {
                IFixedTurnover expense = cellData.getValue();
                int selectedYear = displayYearComboBox.getValue();
                return new ReadOnlyDoubleWrapper(expense.getValueFor(MonthYear.of(index + 1, selectedYear)));
            });
            monthColumns.add(tableColumn);
            mainTable.getColumns().add(tableColumn);
        }

        // Kummulative Spalte
        StringConverter<PaymentType> paymentTypeConverter = Converters.get(PaymentType.class);
        cumulativeColumn = new TableColumn<>(paymentTypeConverter.toString(PaymentType.CUMULATIVE));
        cumulativeColumn.setPrefWidth(120);
        cumulativeColumn.setResizable(false);
        cumulativeColumn.setCellFactory(col -> new CurrencyTableCell<>(true));
        cumulativeColumn.setCellValueFactory(cellData -> {
            IFixedTurnover turnover = cellData.getValue();
            int selectedYear = displayYearComboBox.getValue();
            return new ReadOnlyDoubleWrapper(turnover.getValueForYear(selectedYear));
        });
        mainTable.getColumns().add(cumulativeColumn);
        //Kummulative Zeile
        CumulativeExpense cumulativeExpense = new CumulativeExpense(fixedTurnovers, START_YEAR, END_YEAR);

        mainTable.setRowFactory(tableView -> {
            TableRow<IFixedTurnover> row = new BoldTableRow<>(PaymentType.CUMULATIVE);
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty() && row.getItem() instanceof FixedTurnover expense) {
                    expenseDetailWidget.setExpense(expense);
                    modalPane.show(turnoverWidgetNode);
                }
            });
            return row;
        });

        mainTable.getStyleClass().add(Styles.BORDERED);
        mainTable.getItems().addAll(fixedTurnovers);
        mainTable.getItems().add(cumulativeExpense);
    }
}
