package timkodiert.budgetbook.domain;

import java.util.List;

public interface CategorizableDTO {
    List<Reference<CategoryDTO>> getCategories();
}
