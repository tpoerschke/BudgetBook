package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.util.GroupTableCell;

public class MonthlyOverview implements Initializable, View {

    @FXML
    private TableView<TableData> dataTable, sumTable;
    @FXML
    private TableColumn<TableData, String> positionCol, valueCol, sumTableCol0, sumTableCol1, sumTableCol2;
    @FXML
    private TableColumn<TableData, TableData> buttonCol;

    private Repository<UniqueExpense> uniqueExpenseRepository;

    @Inject
    public MonthlyOverview(Repository<UniqueExpense> uniqueExpenseRepository) {
        this.uniqueExpenseRepository = uniqueExpenseRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<TableData> data = FXCollections.observableArrayList();
        data.add(new TableData("Einzigartige Ausgaben", 9.99, true));
        data.addAll(uniqueExpenseRepository.findAll().stream()
                .map(exp -> new TableData(exp.getBiller(), exp.getTotalValue(), false))
                .toList());
        FilteredList<TableData> filteredData = new FilteredList<>(data);

        SimpleBooleanProperty isCollapsedProperty = new SimpleBooleanProperty(false);
        buttonCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        buttonCol.setCellFactory(col -> new GroupTableCell(isCollapsedProperty));
        isCollapsedProperty.addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(d -> {
                if (newValue) {
                    return d.groupRow();
                }
                return true;
            });
        });

        positionCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().position()));
        valueCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().value() + "€"));

        dataTable.setItems(filteredData);

        sumTableCol0.prefWidthProperty().bind(buttonCol.widthProperty());
        sumTableCol1.prefWidthProperty().bind(positionCol.widthProperty());
        sumTableCol2.prefWidthProperty().bind(valueCol.widthProperty());

        sumTable.getItems().add(new TableData("Summe", 99.99, false));
        sumTableCol1.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().position()));
        sumTableCol2.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().value() + "€"));
    }

    public record TableData(String position, double value, boolean groupRow) {
    }
}
