package timkodiert.budgetbook.domain.model;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static timkodiert.budgetbook.domain.model.TestDataProvider.createTurnover;

class FixedExpenseTest {

    @Test
    @DisplayName("Monatliche Ausgabe: Konfigurierte PaymentInformation")
    void monthlyFixedExpense() {
        FixedTurnover turnover = createTurnover(9.99, MonthYear.of(1, 2023), null);

        assertEquals(0.0, turnover.getValueFor(MonthYear.of(12, 2022)));
        assertEquals(-9.99, turnover.getValueFor(MonthYear.of(1, 2023)));
        assertEquals(0.0, turnover.getValueForYear(2022));
        assertEquals(-9.99 * 12, turnover.getValueForYear(2023));
    }

    @Test
    @DisplayName("Monatliche Ausgabe: Betrag der Importe verwenden")
    void monthlyFixedExpenseImportedAmounts() {
        FixedTurnover turnover = createTurnover(9.5, MonthYear.of(1, 2023), null);
        // Import für Februar vorliegend
        turnover.getAccountTurnover().add(new AccountTurnover(LocalDate.of(2023, 2, 1), "Test", "Test", "Test", -20));
        // Zwei Importe für März vorliegend
        turnover.getAccountTurnover().add(new AccountTurnover(LocalDate.of(2023, 3, 1), "Test", "Test", "Test1", -5));
        turnover.getAccountTurnover().add(new AccountTurnover(LocalDate.of(2023, 3, 2), "Test", "Test", "Test2", -5));

        assertEquals(0.0, turnover.getValueFor(MonthYear.of(2, 2022)));
        assertEquals(-20.0, turnover.getValueFor(MonthYear.of(2, 2023)));
        assertEquals(2 * -5, turnover.getValueFor(MonthYear.of(3, 2023)));
        assertEquals(0.0, turnover.getValueForYear(2022));
        assertEquals(-9.5 * 10 + 1 * -20 + 2 * -5, turnover.getValueForYear(2023));
    }

    @Test
    @DisplayName("Monatliche Ausgabe: Konfigurierte PaymentInformation, werden nicht für die Vergangenheit angewendet")
    void monthlyFixedExpenseNotForPast() {
        // Arrange
        YearMonth current = YearMonth.now();
        YearMonth start = current.minusMonths(1);
        YearMonth end = current.plusMonths(1);
        FixedTurnover turnover = createTurnover(9.99, MonthYear.of(start), MonthYear.of(end));
        turnover.setUsePaymentInfoForFutureOnly(true);

        // Act & Assert
        assertEquals(0, turnover.getValueFor(MonthYear.of(start)));
        assertEquals(-9.99, turnover.getValueFor(MonthYear.of(current)));
        assertEquals(-9.99, turnover.getValueFor(MonthYear.of(end)));
    }
}
