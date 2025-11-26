package timkodiert.budgetbook.annual_overview;

import java.util.Map;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;

public record TableRowData(int id, String label, Map<Integer, Integer> monthValueMap, Reference<CategoryDTO> category) {
}
