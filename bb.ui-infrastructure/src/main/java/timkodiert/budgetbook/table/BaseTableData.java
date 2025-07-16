package timkodiert.budgetbook.table;

import lombok.Getter;

import timkodiert.budgetbook.domain.table.RowType;
import timkodiert.budgetbook.domain.util.HasType;

@Getter
public abstract class BaseTableData implements HasType<RowType> {

    private final Integer id;
    private final RowType type;

    protected BaseTableData(Integer id, RowType type) {
        this.id = id;
        this.type = type;
    }
}
