package timkodiert.budgetbook.view.billing;

import java.util.Optional;

import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jspecify.annotations.Nullable;

import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.BillingCrudService;
import timkodiert.budgetbook.domain.BillingDTO;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.view.FxmlResource;
import timkodiert.budgetbook.view.mdv_base.BaseListManageView;

public class BillingManageView extends BaseListManageView<BillingDTO> {

    @FXML
    private TableColumn<BillingDTO, String> titleColumn;

    private final BillingCrudService crudService;

    @Inject
    public BillingManageView(FXMLLoader fxmlLoader, DialogFactory dialogFactory, LanguageManager languageManager, BillingCrudService crudService) {
        super(fxmlLoader, dialogFactory, languageManager);
        this.crudService = crudService;
    }

    @Override
    public void displayEntityById(int id) {
        detailView.setBean(crudService.readById(id));
    }

    @Override
    protected void initControls() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    }

    @Override
    protected BillingDTO createEmptyEntity() {
        return new BillingDTO();
    }

    @Override
    protected void reloadTable(@Nullable BillingDTO updatedBean) {
        entityTable.getItems().setAll(crudService.readAll());
    }

    @FXML
    private void openNewBilling() {
        entityTable.getSelectionModel().clearSelection();
        lastSelectedRow = null;
        displayNewEntity();
    }

    @Override
    protected String getDetailViewFxmlLocation() {
        return FxmlResource.BILLING_DETAIL_VIEW.getPath();
    }

    @Override
    protected BillingDTO discardChanges(BillingDTO beanToDiscard) {
        return Optional.ofNullable(crudService.readById(beanToDiscard.getId())).orElseGet(this::createEmptyEntity);
    }
}
