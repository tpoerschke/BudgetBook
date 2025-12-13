package timkodiert.budgetbook.table.cell.style;

import javafx.scene.control.TableCell;

import timkodiert.budgetbook.representation.RowType;

public interface CellStyle {

    void apply(TableCell<?, ?> cell, RowType rowType);
    void reset(TableCell<?, ?> cell);
}
