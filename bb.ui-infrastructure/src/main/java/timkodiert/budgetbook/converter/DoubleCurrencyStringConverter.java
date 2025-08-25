package timkodiert.budgetbook.converter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import javax.inject.Inject;

import javafx.util.StringConverter;

import static timkodiert.budgetbook.util.ObjectUtils.nvl;

public class DoubleCurrencyStringConverter extends StringConverter<Double> {

    private final NumberFormat format;

    @Inject
    public DoubleCurrencyStringConverter() {
        format = NumberFormat.getCurrencyInstance(Locale.GERMAN);
        format.setCurrency(Currency.getInstance("EUR"));
    }

    @Override
    public String toString(Double obj) {
        return nvl(obj, format::format, "");
    }

    @Override
    public Double fromString(String str) {
        try {
            return str.isEmpty() ? null : format.parse(str).doubleValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @deprecated Stattdessen sollte die Methode toString verwendet werden
     */
    @Deprecated(forRemoval = true)
    public String format(double value) {
        return format.format(value);
    }
}
