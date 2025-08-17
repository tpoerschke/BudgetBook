package timkodiert.budgetbook.ui.helper;

import java.util.List;

import org.controlsfx.control.CheckListView;

import timkodiert.budgetbook.domain.CategorizableDTO;
import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;

public class CategoryCheckListHelper {

    private final CheckListView<Reference<CategoryDTO>> checkListView;

    public CategoryCheckListHelper(CheckListView<Reference<CategoryDTO>> checkListView, List<Reference<CategoryDTO>> categories) {
        this.checkListView = checkListView;
        checkListView.getItems().setAll(categories);
    }

    public void checkCategories(CategorizableDTO categorizable) {
        checkListView.getCheckModel().clearChecks();
        categorizable.getCategories().forEach(checkListView.getCheckModel()::check);
    }

    public List<Reference<CategoryDTO>> getCheckedCategories() {
        return checkListView.getCheckModel().getCheckedItems();
    }
}
