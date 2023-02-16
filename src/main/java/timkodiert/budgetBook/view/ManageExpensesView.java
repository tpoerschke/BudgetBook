package timkodiert.budgetBook.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.repository.FixedExpensesRepository;
import timkodiert.budgetBook.view.factory.EditExpenseViewFactory;

public class ManageExpensesView implements View, Initializable {

    @FXML
    private Pane detailViewContainer;

    @FXML
    private TableView<ExpenseAdapter> expensesTable;

    @FXML
    private TableColumn<ExpenseAdapter, String> positionCol;

    private FixedExpensesRepository fixedExpensesRepository;
    private EditExpenseViewFactory editExpenseViewFactory;

    @Inject
    public ManageExpensesView(FixedExpensesRepository fixedExpensesRepository, EditExpenseViewFactory editExpenseViewFactory) {
        this.fixedExpensesRepository = fixedExpensesRepository;
        this.editExpenseViewFactory = editExpenseViewFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        expensesTable.getItems().addAll(fixedExpensesRepository.findAll().stream().map(FixedExpense::getAdapter).toList());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditExpense.fxml"));
        loader.setController(editExpenseViewFactory.create(fixedExpensesRepository.findAll().get(0)));
        try {
            detailViewContainer.getChildren().add(loader.load());
        }
        catch(IOException ioe) {

        }
    }
}
