package timkodiert.budgetbook.analysis;

import java.time.Month;
import java.time.YearMonth;
import java.util.List;

import timkodiert.budgetbook.domain.model.MonthYear;

public enum AnalysisPeriod {
    LAST_12_MONTH, THIS_YEAR;

    public List<MonthYear> getMonths() {
        YearMonth now = YearMonth.now();
        if (this == AnalysisPeriod.LAST_12_MONTH) {
            return MonthYear.range(MonthYear.of(now.minusMonths(12)), MonthYear.now());
        }
        return MonthYear.range(MonthYear.of(Month.JANUARY.getValue(), now.getYear()), MonthYear.of(Month.DECEMBER.getValue(), now.getYear()));
    }
}
