package timkodiert.budgetBook.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CumulativeExpenseTest {

    @Test
    @DisplayName("Grundlegender Test")
    void test() {
        List<FixedTurnover> expenses = new ArrayList<>();
        expenses.add(new FixedTurnover("Test-Ausgabe 1", 10, PaymentType.MONTHLY, IntStream.rangeClosed(1, 12).boxed().toList(), MonthYear.of(1, 2023), null));
        expenses.add(new FixedTurnover("Test-Ausgabe 2", 42, PaymentType.MONTHLY, IntStream.rangeClosed(1, 12).boxed().toList(), MonthYear.of(1, 2023), null));
        expenses.add(new FixedTurnover("Test-Ausgabe 3", 100, PaymentType.MONTHLY, IntStream.rangeClosed(1, 12).boxed().toList(), MonthYear.of(1, 2023), null));
        CumulativeExpense ce = new CumulativeExpense(expenses, 2022, 2023);

        assertEquals(152.0, ce.getValueFor(MonthYear.of(1, 2023)));
        assertEquals(152.0 * 12, ce.getValueForYear(2023));
    }
}
