package timkodiert.budgetbook.view.category;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.view.mdv_base.DtoAdapterBase;

public class CategoryDtoAdapter implements DtoAdapterBase<CategoryDTO> {

    private final CategoryDTO categoryDTO;

    public CategoryDtoAdapter(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public CategoryDTO getDto() {
        return categoryDTO;
    }
}
