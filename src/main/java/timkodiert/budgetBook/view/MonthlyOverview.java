package timkodiert.budgetBook.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.util.CurrencyTableCell;
import timkodiert.budgetBook.util.DateTableCell;
import timkodiert.budgetBook.util.GroupTableCell;

public class MonthlyOverview implements Initializable, View {

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

    private final Repository<UniqueExpense> uniqueExpenseRepository;
    private final Repository<FixedExpense> fixedExpenseRepository;

    @Inject
    public MonthlyOverview(Repository<UniqueExpense> uniqueExpenseRepository,
            Repository<FixedExpense> fixedExpenseRepository) {
        this.uniqueExpenseRepository = uniqueExpenseRepository;
        this.fixedExpenseRepository = fixedExpenseRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<TableData> data = FXCollections.observableArrayList();

        List<FixedExpense> fixedExpenses = fixedExpenseRepository.findAll();
        data.add(new TableData("Regelmäßige Ausgaben",
                fixedExpenses.stream().mapToDouble(FixedExpense::getCurrentMonthValue).sum(),
                null, null,
                RowType.FIXED_EXPENSE_GROUP));
        data.addAll(fixedExpenses.stream()
                .map(exp -> new TableData(exp.getPosition(), exp.getCurrentMonthValue(), null,
                        String.join(", ", exp.getCategories().stream().map(Category::getName).toList()),
                        RowType.FIXED_EXPENSE))
                .toList());

        List<UniqueExpense> uniqueExpenses = uniqueExpenseRepository.findAll();
        data.add(new TableData("Einzigartige Ausgaben",
                uniqueExpenses.stream().mapToDouble(UniqueExpense::getTotalValue).sum(),
                null, null,
                RowType.UNIQUE_EXPENSE_GROUP));
        data.addAll(uniqueExpenses.stream().map(exp -> {
            String categories = String.join(", ", exp.getPaymentInformations().stream()
                    .flatMap(info -> info.getCategories().stream()).map(Category::getName).toList());
            return new TableData(exp.getBiller(), exp.getTotalValue(), exp.getDate(), categories,
                    RowType.UNIQUE_EXPENSE);
        }).toList());

        FilteredList<TableData> filteredData = new FilteredList<>(data);

        SimpleBooleanProperty isUniqueCollapsedProperty = new SimpleBooleanProperty(false);
        SimpleBooleanProperty isFixedCollapsedProperty = new SimpleBooleanProperty(true);
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
        buttonCol.setCellFactory(col -> new GroupTableCell(isUniqueCollapsedProperty, isFixedCollapsedProperty));
        isUniqueCollapsedProperty.addListener((observable, oldValue, newValue) -> {
            predicator.run();
        });
        isFixedCollapsedProperty.addListener((observable, oldValue, newValue) -> {
            predicator.run();
        });
        predicator.run();

        positionCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().position()));
        valueCol.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().value()));
        valueCol.setCellFactory(col -> new CurrencyTableCell<>());
        dateCol.setCellValueFactory(cell -> new SimpleObjectProperty<LocalDate>(cell.getValue().date()));
        dateCol.setCellFactory(col -> new DateTableCell<>());
        categoriesCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().categories()));

        dataTable.getColumns().forEach(col -> col.setReorderable(false));
        dataTable.setItems(filteredData);

        sumTableCol0.prefWidthProperty().bind(buttonCol.widthProperty());
        sumTableCol1.prefWidthProperty().bind(positionCol.widthProperty());
        sumTableCol2.prefWidthProperty().bind(valueCol.widthProperty());

        double totalSum = uniqueExpenses.stream().mapToDouble(UniqueExpense::getTotalValue).sum()
                + fixedExpenses.stream().mapToDouble(FixedExpense::getCurrentMonthValue).sum();
        sumTable.getItems().add(new TableData("Summe", totalSum, null, null, RowType.SUM));
        sumTableCol1.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().position()));
        sumTableCol2.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().value()));
        sumTableCol2.setCellFactory(col -> new CurrencyTableCell<>());
    }

    public enum RowType {
        UNIQUE_EXPENSE, FIXED_EXPENSE, UNIQUE_EXPENSE_GROUP, FIXED_EXPENSE_GROUP, SUM;

        public static List<RowType> getGroupTypes() {
            return List.of(UNIQUE_EXPENSE_GROUP, FIXED_EXPENSE_GROUP);
        }
    }

    public record TableData(String position, double value, LocalDate date, String categories, RowType type) {
    }
}
