package timkodiert.budgetBook.view.category;

import java.util.List;
import javax.inject.Inject;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetBook.domain.adapter.CategoryAdapter;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.domain.util.EntityManager;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.table.cell.ButtonTreeTableCell;
import timkodiert.budgetBook.view.FxmlResource;
import timkodiert.budgetBook.view.mdv_base.BaseManageView;

import static timkodiert.budgetBook.util.ObjectUtils.nvl;

public class CategoriesManageView extends BaseManageView<Category, CategoryAdapter> {

    @FXML
    private TreeTableView<Category> categoriesTable;

    @FXML
    private TreeTableColumn<Category, String> nameColumn;
    @FXML
    private TreeTableColumn<Category, Category> actionColumn;

    private final Repository<Category> repository;
    private final LanguageManager languageManager;
    private final EntityManager entityManager;

    @Inject
    public CategoriesManageView(FXMLLoader fxmlLoader, Repository<Category> repository, LanguageManager languageManager, EntityManager entityManager) {
        super(fxmlLoader, languageManager, repository, Category::new);
        this.repository = repository;
        this.languageManager = languageManager;
        this.entityManager = entityManager;
    }

    @Override
    protected void initControls() {
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().getName()));
        actionColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Category>(cellData.getValue().getValue()));
        actionColumn.setCellFactory(col -> new ButtonTreeTableCell<>(languageManager.get("button.delete"), this::deleteCategory));

        categoriesTable.getSelectionModel()
                       .selectedItemProperty()
                       .addListener((ObservableValue<? extends TreeItem<Category>> observable, TreeItem<Category> oldValue, TreeItem<Category> newValue) -> {
                           detailView.setEntity(newValue.getValue());
                       });
    }

    @Override
    protected String getDetailViewFxmlLocation() {
        return FxmlResource.CATEGORY_DETAIL_VIEW.getPath();
    }

    private void deleteCategory(Category category) {
        if (!category.getFixedExpenses().isEmpty() || !category.getUniqueExpenseInformation().isEmpty()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText(languageManager.get("manageCategories.alert.expensesAreAssignedToThisCategory"));

            if (alert.showAndWait().filter(ButtonType.CANCEL::equals).isPresent()) {
                return;
            }
        }

        repository.remove(category);
        reloadTable(null);
    }

    @Override
    protected void reloadTable(@Nullable Category updatedEntity) {

        Category parent = nvl(updatedEntity, Category::getParent);
        if (parent != null) {
            entityManager.refresh(parent);
        }

        List<Category> categories = repository.findAll();
        List<? extends TreeItem<Category>> treeItems = categories.stream().map(Category::asTreeItem).toList();
        List<? extends TreeItem<Category>> roots = treeItems.stream().filter(ti -> ti.getValue().getParent() == null).toList();

        // Dummy-Wurzel
        TreeItem<Category> root = new TreeItem<>(new Category("ROOT"));
        root.getChildren().addAll(roots);

        categoriesTable.setRoot(root);
    }

    @FXML
    private void openNewCategory() {
        displayNewEntity();
    }
}
