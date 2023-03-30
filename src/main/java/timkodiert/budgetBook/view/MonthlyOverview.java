package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
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

        // buttonCol.setCellFactory(col -> {
        //     Button btn = new Button();
        //     TableCell<TableData, TableData> cell = new TableCell<>() {

        //         {
        //             btn.setGraphic(new FontIcon(BootstrapIcons.ARROWS_COLLAPSE));
        //         }

        //         @Override
        //         protected void updateItem(TableData item, boolean empty) {
        //             super.updateItem(item, empty);

        //             if (empty || !item.groupRow()) {
        //                 this.setGraphic(null);
        //             } else {
        //                 //System.out.println(hashCode() + " updateItem isCollapsed " + isCollapsed);
        //                 this.setGraphic(btn);
        //             }
        //         }
        //     };

        //     btn.setOnAction(event -> {
        //         if (filteredData.getPredicate() == null) {
        //             filteredData.setPredicate(d -> {
        //                 return d.groupRow();
        //             });
        //         } else {
        //             filteredData.setPredicate(null);
        //         }
        //     });

        //     return cell;
        // });
        positionCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().position()));
        valueCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().value() + "€"));

        dataTable.setItems(filteredData);
    }

    public record TableData(String position, double value, boolean groupRow) {
    }
}
