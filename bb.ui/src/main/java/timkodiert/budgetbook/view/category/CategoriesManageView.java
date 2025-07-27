package timkodiert.budgetbook.view.category;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.adapter.CategoryAdapter;
import timkodiert.budgetbook.domain.model.CategoryGroup;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.table.cell.StringConverterTableCell;
import timkodiert.budgetbook.view.FxmlResource;
import timkodiert.budgetbook.view.mdv_base.BaseListManageView;

public class CategoriesManageView extends BaseListManageView<CategoryDTO> {

    @FXML
    private TableColumn<CategoryAdapter, String> nameColumn;
    @FXML
    private TableColumn<CategoryAdapter, CategoryGroup> groupColumn;

    @Inject
    public CategoriesManageView(FXMLLoader fxmlLoader, LanguageManager languageManager, DialogFactory dialogFactory) {
        super(fxmlLoader, dialogFactory, languageManager);
    }

    @Override
    public void displayEntityById(int id) {

    }

    @Override
    protected void initControls() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        groupColumn.setCellFactory(col -> new StringConverterTableCell<>(CategoryGroup.class));
    }

    @Override
    protected CategoryDTO createEmptyEntity() {
        return null;
    }

    @Override
    protected void reloadTable(@Nullable CategoryDTO updatedBean) {

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
        return null;
    }
}
