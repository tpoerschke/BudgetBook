package timkodiert.budgetBook.util;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import javafx.scene.control.TableCell;

public class CurrencyTableCell<S, T extends Number> extends TableCell<S, T> {

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMAN);
        format.setCurrency(Currency.getInstance("EUR"));

        if(empty) {
            setText("");
        }
        else {
            setText(item.doubleValue() == 0 ? "-" : format.format(item));
        }
    }
    
}
