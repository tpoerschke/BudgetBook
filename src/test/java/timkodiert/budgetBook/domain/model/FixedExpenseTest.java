package timkodiert.budgetBook.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FixedExpenseTest {

    @Test
    @DisplayName("Monatliche Ausgabe")
    void monthlyFixedExpense() {
        FixedExpense expense = new FixedExpense("Test-Ausgabe", 9.99, PaymentType.MONTHLY, IntStream.rangeClosed(1, 12).boxed().toList(), MonthYear.of(1, 2023), null);

        assertEquals(0.0, expense.getValueFor(MonthYear.of(12, 2022)));
        assertEquals(9.99, expense.getValueFor(MonthYear.of(1, 2023)));
        assertEquals(0.0, expense.getValueForYear(2022));
        assertEquals(9.99 * 12, expense.getValueForYear(2023));
    }
}
