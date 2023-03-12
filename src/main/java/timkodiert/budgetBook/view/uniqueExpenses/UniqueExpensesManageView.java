package timkodiert.budgetBook.view.uniqueExpenses;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.beans.property.SimpleStringProperty;
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
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.FixedExpenseAdapter;
import timkodiert.budgetBook.domain.repository.FixedExpensesRepository;
import timkodiert.budgetBook.view.View;
import timkodiert.budgetBook.view.ViewComponent;

public class UniqueExpensesManageView implements View, Initializable {

    @FXML
    private Pane detailViewContainer;

    @FXML
    private TableView<FixedExpenseAdapter> expensesTable;

    @FXML
    private TableColumn<String, String> billerCol, dateCol;

    private UniqueExpenseDetailView expenseDetailView;
    private ViewComponent viewComponent;

    @Inject
    public UniqueExpensesManageView(ViewComponent viewComponent) {
        this.viewComponent = viewComponent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // positionCol.setCellValueFactory(new PropertyValueFactory<FixedExpenseAdapter, String>("position"));
        // typeCol.setCellValueFactory(
        //         cellData -> new SimpleStringProperty(cellData.getValue().paymentTypeProperty().get().getType()));
        // reloadTable();

        expenseDetailView = viewComponent.getUniqueExpenseDetailView();
        expenseDetailView.setOnDelete(this::reloadTable);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UniqueExpenses/Detail.fxml"));
        loader.setController(expenseDetailView);
        try {
            detailViewContainer.getChildren().add(loader.load());
        } catch (IOException ioe) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }

        expensesTable.setRowFactory(tableView -> {
            TableRow<FixedExpenseAdapter> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    // editExpenseView.setExpense((FixedExpense) row.getItem().getBean()); // ->
                    // Uff, hässliches Casting :o
                }
            });
            return row;
        });
    }

    @FXML
    private void newUniqueExpense(ActionEvent event) {
        // Window primaryStage = ((Button) event.getSource()).getScene().getWindow();
        // try {
        //     Stage stage = StageBuilder.create()
        //             .withModality(Modality.APPLICATION_MODAL)
        //             .withOwner(primaryStage)
        //             .withFXMLResource("/fxml/NewExpense.fxml")
        //             .withView(viewComponent.getNewExpenseView())
        //             .build();
        //     stage.setOnCloseRequest(closeEvent -> reloadTable());
        //     stage.show();
        // } catch (Exception e) {
        //     Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
        //     alert.showAndWait();
        // }
    }

    private void reloadTable() {
        // expensesTable.getItems()
        //         .setAll(fixedExpensesRepository.findAll().stream().map(FixedExpense::getAdapter).map(expenseAdapter -> {
        //             return (FixedExpenseAdapter) expenseAdapter; // uff
        //         }).toList());
    }
}
