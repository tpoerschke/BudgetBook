package timkodiert.budgetbook.ui.control;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

import static timkodiert.budgetbook.util.MoneyEssentials.ROUNDING_MODE;
import static timkodiert.budgetbook.util.MoneyEssentials.ZERO;
import static timkodiert.budgetbook.util.MoneyEssentials.asBigDecimal;
import static timkodiert.budgetbook.util.ObjectUtils.nvl;

class MoneyTextFieldController {

    static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);
    static final BigDecimal FACTOR_100 = new BigDecimal("100");
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
            integerValue.setValue(nvl(getValue(), v -> v.multiply(FACTOR_100).intValueExact()));
            mute = false;
        });

        integerValue.addListener((observable, oldValue, newValue) -> {
            if (mute) {
                return;
            }
            mute = true;
            if (newValue == null) {
                setValue(nullable ? null : ZERO);
            } else {
                setValue(asBigDecimal(newValue).divide(FACTOR_100, ROUNDING_MODE));
            }
            mute = false;
        });
    }

    public StringProperty stringValueProperty() {
        return stringValue;
    }

    public void setValue(@Nullable BigDecimal value) {
        if (value == null) {
            stringValue.set(null);
        } else {
            stringValue.set(format.format(value));
        }
    }

    @Nullable BigDecimal getValue() {
        String value = stringValue.get();
        if (StringUtils.isEmpty(value) || !isStringFormatValid()) {
            return nullable ? null : ZERO;
        }
        try {
            return asBigDecimal(value.replace(",", "."));
        } catch (NumberFormatException e) {
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
