package timkodiert.budgetbook.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyEssentials {


    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    private static final int SCALE = 2;

    public static final BigDecimal ZERO = BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE);

    private MoneyEssentials() {
        // Statische Klasse
    }

    public static BigDecimal asBigDecimal(String stringValue) {
        return new BigDecimal(stringValue).setScale(SCALE, ROUNDING_MODE);
    }

    public static BigDecimal asBigDecimal(int intValue) {
        return new BigDecimal(intValue).setScale(SCALE, ROUNDING_MODE);
    }

}
