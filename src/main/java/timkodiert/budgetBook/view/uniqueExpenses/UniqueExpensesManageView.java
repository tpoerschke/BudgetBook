package timkodiert.budgetBook.view.uniqueExpenses;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.model.UniqueExpenseAdapter;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.table.cell.CurrencyTableCell;
import timkodiert.budgetBook.table.cell.DateTableCell;
import timkodiert.budgetBook.util.DialogFactory;
import timkodiert.budgetBook.view.BaseManageView;
import timkodiert.budgetBook.view.ViewComponent;

public class UniqueExpensesManageView extends BaseManageView<UniqueExpense, UniqueExpenseAdapter> {

    @FXML
    private TableColumn<UniqueExpenseAdapter, String> billerCol;
    @FXML
    private TableColumn<UniqueExpenseAdapter, LocalDate> dateCol;
    @FXML
    private TableColumn<UniqueExpenseAdapter, Number> valueCol;

    @Inject
    public UniqueExpensesManageView(Repository<UniqueExpense> repository, DialogFactory dialogFactory,
            ViewComponent viewComponent) {
        super(() -> new UniqueExpense(), repository, viewComponent.getUniqueExpenseDetailView(), dialogFactory);
    }

    @Override
    public void initControls() {
        billerCol.setCellValueFactory(new PropertyValueFactory<UniqueExpenseAdapter, String>("biller"));
        dateCol.setCellValueFactory(new PropertyValueFactory<UniqueExpenseAdapter, LocalDate>("date"));
        dateCol.setCellFactory(col -> new DateTableCell<>());
        valueCol.setCellValueFactory(
                cellData -> new SimpleDoubleProperty(cellData.getValue().getBean().getTotalValue()));
        valueCol.setCellFactory(col -> new CurrencyTableCell<>());

        entityTable.getSortOrder().add(dateCol);
    }

    @FXML
    private void newUniqueExpense(ActionEvent event) {
        displayNewEntity();
    }
}
