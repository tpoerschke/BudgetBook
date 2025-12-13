package timkodiert.budgetbook.monthly_overview;

import java.time.LocalDate;
import java.util.List;

import timkodiert.budgetbook.representation.HasRowType;
import timkodiert.budgetbook.representation.RowType;

public record TableRowData(int id, RowType rowType, String label, LocalDate date, int value, List<String> categories, boolean hasImport) implements HasRowType {

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
    public RowType getRowType() {
        return rowType;
    }
}
