package timkodiert.budgetBook.table.cell;

import javafx.scene.control.TableCell;
import timkodiert.budgetBook.domain.model.MonthYear;

public class MonthYearTableCell<S> extends TableCell<S, MonthYear> {

    @Override
    protected void updateItem(MonthYear item, boolean empty) {
        super.updateItem(item, empty);
        setText(item == null ? "" : item.toString());
    }
}
