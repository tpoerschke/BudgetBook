package timkodiert.budgetBook.table;

import lombok.Getter;

import timkodiert.budgetBook.util.HasType;

@Getter
public abstract class BaseTableData implements HasType<RowType> {

    private final Integer id;
    private final RowType type;

    protected BaseTableData(Integer id, RowType type) {
        this.id = id;
        this.type = type;
    }
}
