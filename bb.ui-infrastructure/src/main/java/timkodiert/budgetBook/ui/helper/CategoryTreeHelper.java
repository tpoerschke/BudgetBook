package timkodiert.budgetBook.ui.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;

import timkodiert.budgetBook.domain.model.Categorizable;
import timkodiert.budgetBook.domain.model.Category;

public class CategoryTreeHelper {

    private final List<CheckBoxTreeItem<Category>> allTreeItems;

    private CategoryTreeHelper(TreeView<Category> categoriesTreeView, List<Category> categories, boolean checkboxCells) {
        List<CheckBoxTreeItem<Category>> roots = categories.stream().filter(Predicate.not(Category::hasParent)).map(Category::asTreeItem).toList();
        allTreeItems = roots.stream().flatMap(root -> identifyAllTreeItems(root).stream()).toList();
        TreeItem<Category> root = new TreeItem<>(new Category("ROOT"));
        root.getChildren().addAll(roots);
        if (checkboxCells) {
            categoriesTreeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        }
        categoriesTreeView.setRoot(root);
        categoriesTreeView.setShowRoot(false);
    }

    private List<CheckBoxTreeItem<Category>> identifyAllTreeItems(CheckBoxTreeItem<Category> root) {
        List<CheckBoxTreeItem<Category>> items = new ArrayList<>();
        items.add(root);
        root.getChildren().forEach(child -> items.addAll(identifyAllTreeItems((CheckBoxTreeItem<Category>) child)));
        return items;
    }

    public void selectCategories(Categorizable entity) {
        allTreeItems.forEach(ti -> ti.setSelected(entity.getCategories().contains(ti.getValue())));
    }

    public List<Category> getSelectedCategories() {
        return allTreeItems.stream().filter(CheckBoxTreeItem::isSelected).map(TreeItem::getValue).toList();
    }

    public static CategoryTreeHelper from(TreeView<Category> treeView, List<Category> categories) {
        return new CategoryTreeHelper(treeView, categories, true);
    }

    public static CategoryTreeHelper from(TreeView<Category> treeView, List<Category> categories, boolean checkboxCells) {
        return new CategoryTreeHelper(treeView, categories, checkboxCells);
    }
}
