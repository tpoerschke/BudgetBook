package timkodiert.budgetBook.ui.helper;

import java.util.List;

import org.controlsfx.control.CheckListView;

import timkodiert.budgetBook.domain.model.Categorizable;
import timkodiert.budgetBook.domain.model.Category;

public class CategoryCheckListHelper {

    private final CheckListView<Category> checkListView;

    public CategoryCheckListHelper(CheckListView<Category> checkListView, List<Category> categories) {
        this.checkListView = checkListView;
        checkListView.getItems().setAll(categories);
    }

    public void checkCategories(Categorizable categorizable) {
        checkListView.getCheckModel().clearChecks();
        categorizable.getCategories().forEach(checkListView.getCheckModel()::check);
    }

    public List<Category> getCheckedCategories() {
        return checkListView.getCheckModel().getCheckedItems();
    }
}
