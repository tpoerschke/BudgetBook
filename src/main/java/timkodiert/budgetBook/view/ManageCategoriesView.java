package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.util.ButtonTreeTableCell;
import timkodiert.budgetBook.util.EntityManager;

public class ManageCategoriesView implements Initializable {

    @FXML
    private TreeTableView<Category> categoriesTable;

    @FXML
    private TreeTableColumn<Category, String> nameColumn;
    @FXML
    private TreeTableColumn<Category, Category> actionColumn;

    private EntityManager em = EntityManager.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoriesTable.setShowRoot(false);
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().getName()));
        actionColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Category>(cellData.getValue().getValue()));
        actionColumn.setCellFactory(col -> new ButtonTreeTableCell<>("Löschen", this::deleteCategory));
        
        List<Category> categories = em.findAll(Category.class);
        fillTable(categories);
    }

    private void deleteCategory(Category category) {
        // TODO: Checken, ob es Ausgaben mit dieser Kategorie gibt.
        // Dann um Bestätigung bitten und die Ausgaben der Elternkategorie hinzufügen.
        if(category.getParent() != null) {
            category.getParent().getChildren().remove(category);
        }
        em.remove(category);
        fillTable(em.findAll(Category.class));
    }

    private void fillTable(List<Category> categories) {
        List<TreeItem<Category>> treeItems = categories.stream().map(Category::asTreeItem).toList();
        List<TreeItem<Category>> roots = treeItems.stream().filter(ti -> ti.getParent() == null).toList();

        // Dummy-Wurzel
        TreeItem<Category> root = new TreeItem<>(new Category("ROOT"));
        root.getChildren().addAll(roots);

        categoriesTable.setRoot(root);
    }
}
