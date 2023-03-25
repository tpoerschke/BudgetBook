package timkodiert.budgetBook.util;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class DoubleCurrencyStringConverter {

    private NumberFormat format;

    public DoubleCurrencyStringConverter() {
        format = NumberFormat.getCurrencyInstance(Locale.GERMAN);
        format.setCurrency(Currency.getInstance("EUR"));
    }

    public String format(double value) {
        return format.format(value);
    }
}
