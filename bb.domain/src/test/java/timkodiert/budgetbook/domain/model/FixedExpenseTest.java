package timkodiert.budgetbook.domain.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixedExpenseTest {

    @Test
    @DisplayName("Monatliche Ausgabe: Konfigurierte PaymentInformation")
    void monthlyFixedExpense() {
        FixedTurnover expense = new FixedTurnover("Test-Ausgabe", 9.99, PaymentType.MONTHLY, IntStream.rangeClosed(1, 12).boxed().toList(), MonthYear.of(1, 2023), null);

        assertEquals(0.0, expense.getValueFor(MonthYear.of(12, 2022)));
        assertEquals(-9.99, expense.getValueFor(MonthYear.of(1, 2023)));
        assertEquals(0.0, expense.getValueForYear(2022));
        assertEquals(-9.99 * 12, expense.getValueForYear(2023));
    }

    @Test
    @DisplayName("Monatliche Ausgabe: Betrag der Importe verwenden")
    void monthlyFixedExpenseImportedAmounts() {
        FixedTurnover expense = new FixedTurnover("Test-Ausgabe", 9.5, PaymentType.MONTHLY, IntStream.rangeClosed(1, 12).boxed().toList(), MonthYear.of(1, 2023), null);
        // Import für Februar vorliegend
        expense.getAccountTurnover().add(new AccountTurnover(LocalDate.of(2023, 2, 1), "Test", "Test", "Test", -20));
        // Zwei Importe für März vorliegend
        expense.getAccountTurnover().add(new AccountTurnover(LocalDate.of(2023, 3, 1), "Test", "Test", "Test1", -5));
        expense.getAccountTurnover().add(new AccountTurnover(LocalDate.of(2023, 3, 2), "Test", "Test", "Test2", -5));

        assertEquals(0.0, expense.getValueFor(MonthYear.of(2, 2022)));
        assertEquals(-20.0, expense.getValueFor(MonthYear.of(2, 2023)));
        assertEquals(2 * -5, expense.getValueFor(MonthYear.of(3, 2023)));
        assertEquals(0.0, expense.getValueForYear(2022));
        assertEquals(-9.5 * 10 + 1 * -20 + 2 * -5, expense.getValueForYear(2023));
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

    private FixedTurnover createTurnover(double value, MonthYear start, @Nullable MonthYear end) {
        PaymentInformation info = new PaymentInformation();
        info.setType(PaymentType.MONTHLY);
        info.setMonthsOfPayment(Arrays.stream(Month.values()).map(Month::getValue).toList());
        info.setValue(value);
        info.setStart(start);
        info.setEnd(end);
        FixedTurnover turnover = new FixedTurnover();
        turnover.setPosition("Test-Ausgabe");
        turnover.setDirection(TurnoverDirection.OUT);
        turnover.getPaymentInformations().add(info);
        return turnover;
    }
}
