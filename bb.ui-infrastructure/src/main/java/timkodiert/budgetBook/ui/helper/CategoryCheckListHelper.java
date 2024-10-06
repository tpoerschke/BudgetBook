package timkodiert.budgetBook.ui.helper;

import java.util.List;

import org.controlsfx.control.CheckListView;

import timkodiert.budgetBook.domain.model.Categorizable;
import timkodiert.budgetBook.domain.model.Category;

public class CategoryCheckListHelper {

    private final CheckListView<Category> checkListView;
    private final List<Category> categories;


    public CategoryCheckListHelper(CheckListView<Category> checkListView, List<Category> categories) {
        this.checkListView = checkListView;
        this.categories = categories;

        checkListView.getItems().setAll(categories);
    }

    public void selectCategories(Categorizable categorizable) {
        checkListView.getCheckModel().clearChecks();
        categorizable.getCategories().forEach(checkListView.getCheckModel()::check);
    }

    public List<Category> getCheckedCategories() {
        return checkListView.getCheckModel().getCheckedItems();
    }
}
