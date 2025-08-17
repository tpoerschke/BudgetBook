package timkodiert.budgetbook.table.cell;

import javafx.scene.control.TableCell;
import javafx.util.StringConverter;

import timkodiert.budgetbook.converter.Converters;

public class StringConverterTableCell<S, T> extends TableCell<S, T> {

    private final StringConverter<T> converter;

    public StringConverterTableCell(Class<T> typeToConvert) {
        converter = Converters.get(typeToConvert);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        setText(converter.toString(item));
    }
}
