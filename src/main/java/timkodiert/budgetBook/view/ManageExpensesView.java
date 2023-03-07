package timkodiert.budgetBook.view;

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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.FixedExpenseAdapter;
import timkodiert.budgetBook.domain.repository.FixedExpensesRepository;
import timkodiert.budgetBook.util.StageBuilder;

public class ManageExpensesView implements View, Initializable {

    @FXML
    private Pane detailViewContainer;

    @FXML
    private TableView<FixedExpenseAdapter> expensesTable;

    @FXML
    private TableColumn<FixedExpenseAdapter, String> positionCol, typeCol;

    private FixedExpensesRepository fixedExpensesRepository;
    private EditExpenseView editExpenseView;
    private ViewComponent viewComponent;

    @Inject
    public ManageExpensesView(FixedExpensesRepository fixedExpensesRepository, ViewComponent viewComponent) {
        this.fixedExpensesRepository = fixedExpensesRepository;
        this.viewComponent = viewComponent;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positionCol.setCellValueFactory(new PropertyValueFactory<FixedExpenseAdapter, String>("position"));
        typeCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().paymentTypeProperty().get().getType()));
        reloadTable();

        editExpenseView = viewComponent.getEditExpenseView();
        editExpenseView.setOnDelete(this::reloadTable);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditExpense.fxml"));
        loader.setController(editExpenseView);
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
                    editExpenseView.setExpense((FixedExpense) row.getItem().getBean()); // -> Uff, hässliches Casting :o
                }
            });
            return row;
        });
    }

    @FXML
    private void openNewExpense(ActionEvent event) {
        Window primaryStage = ((Button) event.getSource()).getScene().getWindow();
        try {
            Stage stage = StageBuilder.create()
                    .withModality(Modality.APPLICATION_MODAL)
                    .withOwner(primaryStage)
                    .withFXMLResource("/fxml/NewExpense.fxml")
                    .withView(viewComponent.getNewExpenseView())
                    .build();
            stage.setOnCloseRequest(closeEvent -> reloadTable());
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    private void reloadTable() {
        expensesTable.getItems()
                .setAll(fixedExpensesRepository.findAll().stream().map(FixedExpense::getAdapter).map(expenseAdapter -> {
                    return (FixedExpenseAdapter) expenseAdapter; // uff
                }).toList());
    }
}
