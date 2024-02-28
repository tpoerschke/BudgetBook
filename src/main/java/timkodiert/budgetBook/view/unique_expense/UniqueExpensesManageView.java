package timkodiert.budgetBook.view.unique_expense;

import static timkodiert.budgetBook.view.FxmlResource.UNIQUE_TURNOVER_DETAIL_VIEW;

import java.time.LocalDate;
import javax.inject.Inject;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import timkodiert.budgetBook.domain.model.UniqueTurnover;
import timkodiert.budgetBook.domain.model.UniqueTurnoverAdapter;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.table.cell.CurrencyTableCell;
import timkodiert.budgetBook.table.cell.DateTableCell;
import timkodiert.budgetBook.util.dialog.DialogFactory;
import timkodiert.budgetBook.view.ControllerFactory;
import timkodiert.budgetBook.view.mdv_base.BaseManageView;

public class UniqueExpensesManageView extends BaseManageView<UniqueTurnover, UniqueTurnoverAdapter> {

    @FXML
    private TableColumn<UniqueTurnoverAdapter, String> billerCol;
    @FXML
    private TableColumn<UniqueTurnoverAdapter, LocalDate> dateCol;
    @FXML
    private TableColumn<UniqueTurnoverAdapter, Number> valueCol;

    @Inject
    public UniqueExpensesManageView(Repository<UniqueTurnover> repository, DialogFactory dialogFactory, ControllerFactory controllerFactory) {
        super(UniqueTurnover::new, repository, controllerFactory, dialogFactory);
    }

    @Override
    public void initControls() {
        billerCol.setCellValueFactory(new PropertyValueFactory<UniqueTurnoverAdapter, String>("biller"));
        dateCol.setCellValueFactory(new PropertyValueFactory<UniqueTurnoverAdapter, LocalDate>("date"));
        dateCol.setCellFactory(col -> new DateTableCell<>());
        valueCol.setCellValueFactory(
                cellData -> new SimpleDoubleProperty(cellData.getValue().getBean().getTotalValue()));
        valueCol.setCellFactory(col -> new CurrencyTableCell<>());

        entityTable.getSortOrder().add(dateCol);
    }

    @Override
    public String getDetailViewFxmlLocation() {
        return UNIQUE_TURNOVER_DETAIL_VIEW.toString();
    }

    @FXML
    private void newUniqueExpense(ActionEvent event) {
        displayNewEntity();
    }
}
