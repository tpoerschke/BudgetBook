package timkodiert.budgetbook;

import java.time.Year;
import java.time.YearMonth;

public class SystemClock {

    private SystemClock() {
        // Statische Klasse
    }

    public static YearMonth getYearMonthNow() {
        return YearMonth.of(2025, 8); //YearMonth.now();
    }

    public static Year getYearNow() {
        return Year.of(2025); //Year.now();
    }
}
