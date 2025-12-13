package timkodiert.budgetbook.table.cell.style;

import javafx.scene.control.TableCell;
import org.jspecify.annotations.Nullable;

import timkodiert.budgetbook.representation.RowType;

public abstract class BaseCellStyle implements CellStyle {

    protected @Nullable CellStyle wrappedCellStyle;

    protected BaseCellStyle(@Nullable CellStyle wrappedCellStyle) {
        this.wrappedCellStyle = wrappedCellStyle;
    }

    protected void applyWrappedStyle(TableCell<?, ?> cell, RowType rowType) {
        if (wrappedCellStyle == null) {
            return;
        }
        wrappedCellStyle.apply(cell, rowType);
    }

    protected void resetWrappedStyle(TableCell<?, ?> cell) {
        if (wrappedCellStyle == null) {
            return;
        }
        wrappedCellStyle.reset(cell);
    }
}
