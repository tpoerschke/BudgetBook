package timkodiert.budgetBook.view.uniqueExpenses;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import timkodiert.budgetBook.domain.model.FixedExpenseAdapter;
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.model.UniqueExpenseAdapter;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.view.View;
import timkodiert.budgetBook.view.ViewComponent;

public class UniqueExpensesManageView implements View, Initializable {

    @FXML
    private Pane detailViewContainer;

    @FXML
    private TableView<UniqueExpenseAdapter> expensesTable;

    @FXML
    private TableColumn<UniqueExpenseAdapter, String> billerCol, dateCol;

    private UniqueExpenseDetailView detailView;
    private ViewComponent viewComponent;

    private Repository<UniqueExpense> repository;

    @Inject
    public UniqueExpensesManageView(Repository<UniqueExpense> repository, ViewComponent viewComponent) {
        this.repository = repository;
        this.viewComponent = viewComponent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        billerCol.setCellValueFactory(new PropertyValueFactory<UniqueExpenseAdapter, String>("biller"));
        // typeCol.setCellValueFactory(
        //         cellData -> new SimpleStringProperty(cellData.getValue().paymentTypeProperty().get().getType()));
        reloadTable();

        detailView = viewComponent.getUniqueExpenseDetailView();
        detailView.setOnUpdate(this::reloadTable);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UniqueExpenses/Detail.fxml"));
        loader.setController(detailView);
        try {
            detailViewContainer.getChildren().add(loader.load());
        } catch (IOException ioe) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }

        expensesTable.setRowFactory(tableView -> {
            TableRow<UniqueExpenseAdapter> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    detailView.setBean(row.getItem().getBean());
                }
            });
            return row;
        });
    }

    @FXML
    private void newUniqueExpense(ActionEvent event) {
        detailView.setBean(new UniqueExpense());
    }

    private void reloadTable() {
        expensesTable.getItems().setAll(repository.findAll().stream().map(UniqueExpense::getAdapter).toList());
    }
}
