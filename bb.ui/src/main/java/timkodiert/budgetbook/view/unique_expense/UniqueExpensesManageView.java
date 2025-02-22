package timkodiert.budgetbook.view.unique_expense;

import java.time.LocalDate;
import javax.inject.Inject;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.adapter.UniqueTurnoverAdapter;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.repository.Repository;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.table.cell.CurrencyTableCell;
import timkodiert.budgetbook.table.cell.DateTableCell;
import timkodiert.budgetbook.view.mdv_base.BaseListManageView;

import static timkodiert.budgetbook.view.FxmlResource.UNIQUE_TURNOVER_DETAIL_VIEW;

public class UniqueExpensesManageView extends BaseListManageView<UniqueTurnover, UniqueTurnoverAdapter> {

    @FXML
    private TableColumn<UniqueTurnoverAdapter, String> billerCol;
    @FXML
    private TableColumn<UniqueTurnoverAdapter, LocalDate> dateCol;
    @FXML
    private TableColumn<UniqueTurnoverAdapter, Number> valueCol;

    @Inject
    public UniqueExpensesManageView(Repository<UniqueTurnover> repository, DialogFactory dialogFactory, FXMLLoader fxmlLoader, LanguageManager languageManager) {
        super(UniqueTurnover::new, repository, fxmlLoader, dialogFactory, languageManager);
    }

    @Override
    public void initControls() {
        billerCol.setCellValueFactory(new PropertyValueFactory<>("biller"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(col -> new DateTableCell<>());
        valueCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getBean().getTotalValue()));
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
