package timkodiert.budgetBook.util;

import java.util.List;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import timkodiert.budgetBook.domain.model.Categorizable;
import timkodiert.budgetBook.domain.model.Category;

public class CategoryTreeHelper {

    private List<CheckBoxTreeItem<Category>> allTreeItems;

    private CategoryTreeHelper(TreeView<Category> categoriesTreeView, List<Category> categories) {
        allTreeItems = categories.stream().map(Category::asTreeItem).toList();
        List<? extends TreeItem<Category>> roots = allTreeItems.stream()
                .filter(ti -> ti.getValue().getParent() == null)
                .toList();
        TreeItem<Category> root = new TreeItem<>(new Category("ROOT"));
        root.getChildren().addAll(roots);
        categoriesTreeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        categoriesTreeView.setRoot(root);
        categoriesTreeView.setShowRoot(false);
    }

    public void selectCategories(Categorizable entity) {
        allTreeItems.forEach(ti -> ti.setSelected(entity.getCategories().contains(ti.getValue())));
    }

    public List<Category> getSelectedCategories() {
        return allTreeItems.stream()
                .filter(CheckBoxTreeItem::isSelected).map(TreeItem::getValue)
                .toList();
    }

    public static CategoryTreeHelper from(TreeView<Category> treeView, List<Category> categories) {
        return new CategoryTreeHelper(treeView, categories);
    }
}
