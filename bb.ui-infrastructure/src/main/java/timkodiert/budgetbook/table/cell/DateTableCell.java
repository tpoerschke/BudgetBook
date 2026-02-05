package timkodiert.budgetbook.table.cell;

import java.time.LocalDate;

import javafx.scene.control.TableCell;

import timkodiert.budgetbook.converter.Converters;

public class DateTableCell<S> extends TableCell<S, LocalDate> {

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        setText(item == null ? "" : Converters.get(LocalDate.class).toString(item));
    }
}
