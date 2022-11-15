package timkodiert.budgetBook.util;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import javafx.scene.control.TableCell;

public class CurrencyTableCell<S> extends TableCell<S, Double> {

    @Override
    protected void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMAN);
        format.setCurrency(Currency.getInstance("EUR"));

        setText(empty ? "" : format.format(item));
    }
    
}
