package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.repository.FixedExpensesRepository;

public class ManageExpensesView implements View, Initializable {

    @FXML
    private Pane detailViewContainer;

    @FXML
    private TableView<ExpenseAdapter> expensesTable;

    @FXML
    private TableColumn<ExpenseAdapter, String> positionCol;

    private FixedExpensesRepository fixedExpensesRepository;

    @Inject
    public ManageExpensesView(FixedExpensesRepository fixedExpensesRepository) {
        this.fixedExpensesRepository = fixedExpensesRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        expensesTable.getItems().addAll(fixedExpensesRepository.findAll().stream().map(FixedExpense::getAdapter).toList());
    }
}
