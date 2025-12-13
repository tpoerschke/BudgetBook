package timkodiert.budgetbook.analysis;

import timkodiert.budgetbook.representation.HasRowType;
import timkodiert.budgetbook.representation.RowType;

public record TableRowData(String position, int value, RowType rowType) implements HasRowType {

    @Override
    public RowType getRowType() {
        return rowType;
    }
}
