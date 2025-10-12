package timkodiert.budgetbook.ui.control;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetbook.exception.TechnicalException;

import static timkodiert.budgetbook.util.ObjectUtils.nvl;

class MoneyTextFieldController {

    private static final Pattern VALID_PATTERN = Pattern.compile("\\d+,\\d\\d");

    @Getter
    private final DecimalFormat format = new DecimalFormat("0.00");

    private final StringProperty stringValue = new SimpleStringProperty("0,00");
    private final ObjectProperty<Integer> integerValue = new SimpleObjectProperty<>(0); // Keine IntegerProperty, da null-Werte nötig
    @Setter
    private boolean nullable;

    private boolean mute = false;

    public MoneyTextFieldController() {
        format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.GERMAN));

        stringValue.addListener((observable, oldValue, newValue) -> {
            if (mute) {
                return;
            }
            mute = true;
            integerValue.setValue(nvl(getValue(), v -> (int) (v * 100)));
            mute = false;
        });

        integerValue.addListener((observable, oldValue, newValue) -> {
            if (mute) {
                return;
            }
            mute = true;
            if (newValue == null) {
                setValue(nullable ? null : 0.0);
            } else {
                setValue(newValue.doubleValue() / 100.0);
            }
            mute = false;
        });
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
        if (StringUtils.isEmpty(value) || !isStringFormatValid()) {
            return nullable ? null : 0.0;
        }
        try {
            return format.parse(value).doubleValue();
        } catch (ParseException e) {
            throw TechnicalException.forProgrammingError(e.getMessage(), e);
        }
    }

    public boolean isStringFormatValid() {
        String value = stringValue.get();
        if (StringUtils.isEmpty(value)) {
            return nullable;
        }
        return VALID_PATTERN.matcher(value.trim()).matches();
    }

    public ObjectProperty<Integer> integerValueProperty() {
        return integerValue;
    }
}
