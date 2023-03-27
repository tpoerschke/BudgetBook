package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
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
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.util.GroupTableCell;

public class MonthlyOverview implements Initializable, View {

    @FXML
    private TableView<TableData> dataTable;
    @FXML
    private TableColumn<TableData, String> positionCol, valueCol;
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
        data.add(new TableData("Einzigartige Ausgaben", 9.99, true, new SimpleBooleanProperty(false)));
        data.addAll(uniqueExpenseRepository.findAll().stream()
                .map(exp -> new TableData(exp.getBiller(), exp.getTotalValue(), false,
                        new SimpleBooleanProperty(false)))
                .toList());
        FilteredList<TableData> filteredData = new FilteredList<>(data);

        buttonCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        buttonCol.setCellFactory(col -> new GroupTableCell((isCollapsed) -> {
            filteredData.setPredicate(d -> {
                if (isCollapsed) {
                    return d.groupRow();
                }
                return true;
            });
        }));
        positionCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().position()));
        valueCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().value() + "€"));

        dataTable.setItems(filteredData);
    }

    public record TableData(String position, double value, boolean groupRow, BooleanProperty hide) {
    }
}
