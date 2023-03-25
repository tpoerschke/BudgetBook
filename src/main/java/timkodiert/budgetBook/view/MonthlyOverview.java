package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.repository.Repository;

public class MonthlyOverview implements Initializable, View {

    @FXML
    private TableView<UniqueExpense> dataTable;
    @FXML
    private TableColumn<UniqueExpense, String> positionCol, valueCol, buttonCol;

    private Repository<UniqueExpense> uniqueExpenseRepository;

    @Inject
    public MonthlyOverview(Repository<UniqueExpense> uniqueExpenseRepository) {
        this.uniqueExpenseRepository = uniqueExpenseRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
