package timkodiert.budgetBook.table.cell;

import javafx.scene.control.TableCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTableCell<S, T extends LocalDate> extends TableCell<S, T> {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        setText(item == null ? "" : formatter.format(item));
    }
}
