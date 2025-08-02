package timkodiert.budgetbook.view.category;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.CategoryCrudService;
import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.table.cell.ReferenceTableCell;
import timkodiert.budgetbook.view.FxmlResource;
import timkodiert.budgetbook.view.mdv_base.BaseListManageView;

public class CategoriesManageView extends BaseListManageView<CategoryDTO> {

    @FXML
    private TableColumn<CategoryDTO, String> nameColumn;
    @FXML
    private TableColumn<CategoryDTO, Reference<CategoryGroupDTO>> groupColumn;

    private final CategoryCrudService crudService;

    @Inject
    public CategoriesManageView(FXMLLoader fxmlLoader, LanguageManager languageManager, DialogFactory dialogFactory, CategoryCrudService crudService) {
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
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        groupColumn.setCellFactory(col -> new ReferenceTableCell<>());
    }

    @Override
    protected CategoryDTO createEmptyEntity() {
        return new CategoryDTO();
    }

    @Override
    protected void reloadTable(@Nullable CategoryDTO updatedBean) {
        entityTable.getItems().setAll(crudService.readAll());
    }

    @Override
    protected String getDetailViewFxmlLocation() {
        return FxmlResource.CATEGORY_DETAIL_VIEW.getPath();
    }

    @FXML
    private void openNewCategory() {
        displayNewEntity();
    }

    @Override
    protected CategoryDTO discardChanges(CategoryDTO beanToDiscard) {
        return crudService.readById(beanToDiscard.getId());
    }
}
