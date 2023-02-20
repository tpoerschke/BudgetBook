package timkodiert.budgetBook.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

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
import timkodiert.budgetBook.domain.model.ExpenseAdapter;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.FixedExpenseAdapter;
import timkodiert.budgetBook.domain.repository.FixedExpensesRepository;

public class ManageExpensesView implements View, Initializable {

    @FXML
    private Pane detailViewContainer;

    @FXML
    private TableView<FixedExpenseAdapter> expensesTable;

    @FXML
    private TableColumn<FixedExpenseAdapter, String> positionCol;

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
        expensesTable.getItems().addAll(fixedExpensesRepository.findAll().stream().map(FixedExpense::getAdapter).map(expenseAdapter -> {
            return (FixedExpenseAdapter)expenseAdapter; // uff
        }).toList());

        editExpenseView = viewComponent.getEditExpenseView();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditExpense.fxml"));
        loader.setController(editExpenseView);
        try {
            detailViewContainer.getChildren().add(loader.load());
        }
        catch(IOException ioe) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }

        expensesTable.setRowFactory(tableView -> {
            TableRow<FixedExpenseAdapter> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 1 && !row.isEmpty()) {
                    editExpenseView.setExpense((FixedExpense)row.getItem().getBean()); // -> Uff, hässliches Casting :o
                }
            });
            return row;
        });
    }
}
