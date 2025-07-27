package timkodiert.budgetbook.view.category_group;

import javax.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.CategoryGroupCrudService;
import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.view.FxmlResource;
import timkodiert.budgetbook.view.mdv_base.BaseListManageView;

public class CategoryGroupManageView extends BaseListManageView<CategoryGroupDTO> {

    @FXML
    private TableColumn<CategoryGroupDTO, String> nameColumn;

    private final CategoryGroupCrudService crudService;

    @Inject
    public CategoryGroupManageView(FXMLLoader fxmlLoader, DialogFactory dialogFactory, LanguageManager languageManager, CategoryGroupCrudService crudService) {
        super(fxmlLoader, dialogFactory, languageManager);
        this.crudService = crudService;
    }

    @Override
    public void displayEntityById(int id) {
        detailView.setBean(crudService.readById(id));
    }

    @Override
    protected void initControls() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @Override
    protected CategoryGroupDTO createEmptyEntity() {
        return new CategoryGroupDTO();
    }

    @Override
    protected void reloadTable(@Nullable CategoryGroupDTO updatedBean) {
        entityTable.getItems().setAll(crudService.readAll());
    }

    @FXML
    private void openNewCategoryGroup(ActionEvent actionEvent) {
        displayNewEntity();
    }

    @Override
    protected String getDetailViewFxmlLocation() {
        return FxmlResource.CATEGORY_GROUP_DETAIL_VIEW.getPath();
    }

    @Override
    protected CategoryGroupDTO discardChanges(CategoryGroupDTO beanToDiscard) {
        return crudService.readById(beanToDiscard.getId());
    }
}
