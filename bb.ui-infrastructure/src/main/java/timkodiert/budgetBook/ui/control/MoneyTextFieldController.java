package timkodiert.budgetBook.ui.control;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

class MoneyTextFieldController {

    private static final Pattern VALID_PATTERN = Pattern.compile("\\d+,\\d\\d");

    @Getter
    private final DecimalFormat format = new DecimalFormat("0.00");

    private final StringProperty stringValue = new SimpleStringProperty();

    public MoneyTextFieldController() {
        format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.GERMAN));
    }

    public StringProperty stringValueProperty() {
        return stringValue;
    }

    public void setValue(@Nullable Double value) {
        if (value == null) {
            stringValue.set(null);
        } else {
            stringValue.set(format.format(value));
        }
    }

    public @Nullable Double getValue() {
        String value = stringValue.get();
        if (value == null || !isStringFormatValid()) {
            return null;
        }
        try {
            return format.parse(value).doubleValue();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isStringFormatValid() {
        String value = stringValue.get();
        if(value == null || value.isEmpty()) {
            return true;
        }
        return VALID_PATTERN.matcher(value.trim()).matches();
    }
}
