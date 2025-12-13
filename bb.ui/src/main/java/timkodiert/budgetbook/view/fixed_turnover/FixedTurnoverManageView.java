package timkodiert.budgetbook.view.fixed_turnover;

import java.util.Optional;
import javax.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.FixedTurnoverCrudService;
import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.PaymentType;
import timkodiert.budgetbook.domain.TurnoverDirection;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.table.cell.StringConverterTableCell;
import timkodiert.budgetbook.view.mdv_base.BaseListManageView;

import static timkodiert.budgetbook.view.FxmlResource.FIXED_TURNOVER_DETAIL_VIEW;

public class FixedTurnoverManageView extends BaseListManageView<FixedTurnoverDTO> {

    @FXML
    private TableColumn<FixedTurnoverDTO, String> positionCol;
    @FXML
    private TableColumn<FixedTurnoverDTO, PaymentType> typeCol;
    @FXML
    private TableColumn<FixedTurnoverDTO, TurnoverDirection> directionCol;

    private final FixedTurnoverCrudService crudService;

    @Inject
    public FixedTurnoverManageView(DialogFactory dialogFactory, FXMLLoader fxmlLoader, LanguageManager languageManager, FixedTurnoverCrudService crudService) {
        super(fxmlLoader, dialogFactory, languageManager);
        this.crudService = crudService;
    }

    @Override
    protected void initControls() {
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        typeCol.setCellFactory(col -> new StringConverterTableCell<>(PaymentType.class));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        directionCol.setCellFactory(col -> new StringConverterTableCell<>(TurnoverDirection.class));
        directionCol.setCellValueFactory(new PropertyValueFactory<>("direction"));
    }

    @Override
    public String getDetailViewFxmlLocation() {
        return FIXED_TURNOVER_DETAIL_VIEW.toString();
    }

    @FXML
    private void openNewExpense(ActionEvent event) {
        entityTable.getSelectionModel().clearSelection();
        lastSelectedRow = null;
        displayNewEntity();
    }

    @Override
    public void displayEntityById(int id) {
        detailView.setBean(crudService.readById(id));
    }

    @Override
    protected void reloadTable(@Nullable FixedTurnoverDTO updatedBean) {
        entityTable.getItems().setAll(crudService.readAll());
        entityTable.sort();
    }

    @Override
    protected FixedTurnoverDTO createEmptyEntity() {
        return new FixedTurnoverDTO();
    }

    @Override
    protected FixedTurnoverDTO discardChanges(FixedTurnoverDTO beanToDiscard) {
        return Optional.ofNullable(crudService.readById(beanToDiscard.getId())).orElseGet(this::createEmptyEntity);
    }
}
