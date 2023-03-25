package timkodiert.budgetBook.util;

import javafx.scene.control.TableCell;

public class CurrencyTableCell<S, T extends Number> extends TableCell<S, T> {

    private DoubleCurrencyStringConverter converter = new DoubleCurrencyStringConverter();

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText("");
        } else {
            setText(item.doubleValue() == 0 ? "-" : converter.format(item.doubleValue()));
        }
    }

}
