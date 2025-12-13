package timkodiert.budgetbook.annual_overview;

import java.util.Map;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.representation.HasRowType;
import timkodiert.budgetbook.representation.RowType;

public record TableRowData(int id, String label, Map<Integer, Integer> monthValueMap, Reference<CategoryDTO> category, RowType type) implements HasRowType {

    public static TableRowData forCategory(Reference<CategoryDTO> category) {
        return new TableRowData(category.id(), category.name(), Map.of(), category, RowType.CATEGORY_GROUP);
    }

    @Override
    public RowType getRowType() {
        return type;
    }
}
