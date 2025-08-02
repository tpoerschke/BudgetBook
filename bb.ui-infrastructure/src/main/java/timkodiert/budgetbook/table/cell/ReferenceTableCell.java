package timkodiert.budgetbook.table.cell;

import javafx.scene.control.TableCell;

import timkodiert.budgetbook.converter.ReferenceStringConverter;
import timkodiert.budgetbook.domain.Reference;

public class ReferenceTableCell<S, T> extends TableCell<S, Reference<T>> {

    private final ReferenceStringConverter<T> converter;

    public ReferenceTableCell() {
        converter = new ReferenceStringConverter<>();
    }

    @Override
    protected void updateItem(Reference<T> item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? "" : converter.toString(item));
    }
}
