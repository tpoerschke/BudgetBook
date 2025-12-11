package timkodiert.budgetbook.analysis;

import timkodiert.budgetbook.domain.table.RowType;
import timkodiert.budgetbook.domain.util.HasType;

public record TableRowData(String position, int value, RowType rowType) implements HasType<RowType> {

    @Override
    public RowType getType() {
        return rowType;
    }
}
