package timkodiert.budgetBook.util;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;

public class AbstractSumChangeListener<T> {

    protected final DoubleProperty sumProp;
    protected final StringProperty sumTextProp;

    private final NumberFormat format;

    public AbstractSumChangeListener(DoubleProperty sumProp, StringProperty sumTextProp) {
        this.sumProp = sumProp;
        this.sumTextProp = sumTextProp;

        this.format = NumberFormat.getCurrencyInstance(Locale.GERMAN);
        this.format.setCurrency(Currency.getInstance("EUR"));
    }

    protected void setProperties(double sum) {
        sumProp.set(sum);
        sumTextProp.set(format.format(sum));
    }
}
