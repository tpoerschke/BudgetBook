package timkodiert.budgetbook.view.category_group;

import javax.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.adapter.CategoryAdapter;
import timkodiert.budgetbook.domain.adapter.CategoryGroupAdapter;
import timkodiert.budgetbook.domain.model.CategoryGroup;
import timkodiert.budgetbook.domain.repository.Repository;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.view.FxmlResource;
import timkodiert.budgetbook.view.mdv_base.BaseListManageView;

public class CategoryGroupManageView extends BaseListManageView<CategoryGroup, CategoryGroupAdapter> {

    @FXML
    private TableColumn<CategoryAdapter, String> nameColumn;

    @Inject
    public CategoryGroupManageView(Repository<CategoryGroup> repository, FXMLLoader fxmlLoader, DialogFactory dialogFactory, LanguageManager languageManager) {
        super(CategoryGroup::new, repository, fxmlLoader, dialogFactory, languageManager);
    }

    @Override
    protected void initControls() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @FXML
    private void openNewCategoryGroup(ActionEvent actionEvent) {
        displayNewEntity();
    }

    @Override
    protected String getDetailViewFxmlLocation() {
        return FxmlResource.CATEGORY_GROUP_DETAIL_VIEW.getPath();
    }
}
