package timkodiert.budgetbook.monthly_overview;

import java.time.LocalDate;
import java.util.List;

import timkodiert.budgetbook.domain.table.RowType;
import timkodiert.budgetbook.domain.util.HasType;

public record TableRowData(int id, RowType rowType, String label, LocalDate date, int value, List<String> categories, boolean hasImport) implements HasType<RowType> {

    public static TableRowData forSum(String label, int value) {
        return new TableRowData(-1, RowType.SUM, label, null, value, List.of(), false);
    }

    public static TableRowData forTotalSum(String label, int value) {
        return new TableRowData(-1, RowType.TOTAL_SUM, label, null, value, List.of(), false);
    }

    public String categoriesString() {
        return String.join(", ", categories);
    }

    @Override
    public RowType getType() {
        return rowType;
    }
}
