package timkodiert.budgetBook.view.category;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import timkodiert.budgetBook.dialog.DialogFactory;
import timkodiert.budgetBook.domain.adapter.CategoryAdapter;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.CategoryGroup;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.table.cell.StringConverterTableCell;
import timkodiert.budgetBook.view.FxmlResource;
import timkodiert.budgetBook.view.mdv_base.BaseListManageView;

public class CategoriesManageView extends BaseListManageView<Category, CategoryAdapter> {

    @FXML
    private TableColumn<CategoryAdapter, String> nameColumn;
    @FXML
    private TableColumn<CategoryAdapter, CategoryGroup> groupColumn;

    @Inject
    public CategoriesManageView(FXMLLoader fxmlLoader, Repository<Category> repository, LanguageManager languageManager, DialogFactory dialogFactory) {
        super(Category::new, repository, fxmlLoader, dialogFactory, languageManager);
    }

    @Override
    protected void initControls() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        groupColumn.setCellFactory(col -> new StringConverterTableCell<>(CategoryGroup.class));
    }

    @Override
    protected String getDetailViewFxmlLocation() {
        return FxmlResource.CATEGORY_DETAIL_VIEW.getPath();
    }

    @FXML
    private void openNewCategory() {
        displayNewEntity();
    }
}
