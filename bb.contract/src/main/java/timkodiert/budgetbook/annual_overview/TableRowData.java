package timkodiert.budgetbook.annual_overview;

import java.util.Map;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.table.RowType;
import timkodiert.budgetbook.domain.util.HasType;

public record TableRowData(int id, String label, Map<Integer, Integer> monthValueMap, Reference<CategoryDTO> category, RowType type) implements HasType<RowType> {

    public static TableRowData forCategory(Reference<CategoryDTO> category) {
        return new TableRowData(category.id(), category.name(), Map.of(), category, RowType.CATEGORY_GROUP);
    }

    @Override
    public RowType getType() {
        return type;
    }
}
