package timkodiert.budgetbook.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
@Getter
public class MonthYear implements Comparable<MonthYear> {

    @Column(nullable = true)
    private int month;
    @Column(nullable = true)
    private int year;

    @Override
    public int compareTo(MonthYear other) {
        if (this.equals(other)) {
            return 0;
        }

        // Kleiner
        if (this.getYear() < other.getYear()) {
            return -1;
        }
        if (this.getYear() == other.getYear() && this.getMonth() < other.getMonth()) {
            return -1;
        }

        // Größer
        if (this.getYear() > other.getYear()) {
            return 1;
        }
        if (this.getYear() == other.getYear() && this.getMonth() > other.getMonth()) {
            return 1;
        }

        // Um den Compiler glücklich zu machen
        return 0;
    }

    public MonthYear plusMonths(int count) {
        LocalDate monthYearDate = LocalDate.of(this.year, this.month, 1).plusMonths(count);
        return MonthYear.of(monthYearDate.getMonthValue(), monthYearDate.getYear());
    }

    public boolean containsDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return month == date.getMonthValue() && year == date.getYear();
    }

    public static MonthYear now() {
        LocalDate now = LocalDate.now();
        return new MonthYear(now.getMonthValue(), now.getYear());
    }

    @Override
    public String toString() {
        return this.month + "." + this.year;
    }
}
