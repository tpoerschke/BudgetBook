package timkodiert.budgetbook.converter;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import javax.inject.Inject;

public class DoubleCurrencyStringConverter {

    private NumberFormat format;

    @Inject
    public DoubleCurrencyStringConverter() {
        format = NumberFormat.getCurrencyInstance(Locale.GERMAN);
        format.setCurrency(Currency.getInstance("EUR"));
    }

    public String format(double value) {
        return format.format(value);
    }
}
