package timkodiert.budgetbook.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static timkodiert.budgetbook.domain.model.TestDataProvider.createFixedTurnover;

class CumulativeExpenseTest {

    @Test
    @DisplayName("Grundlegender Test")
    void test() {
        List<FixedTurnover> expenses = new ArrayList<>();
        expenses.add(createFixedTurnover(1000, MonthYear.of(1, 2023), null));
        expenses.add(createFixedTurnover(4200, MonthYear.of(1, 2023), null));
        expenses.add(createFixedTurnover(10000, MonthYear.of(1, 2023), null));
        CumulativeExpense ce = new CumulativeExpense(expenses, 2022, 2023);

        assertEquals(-15200, ce.getValueFor(MonthYear.of(1, 2023)));
        assertEquals(-15200 * 12, ce.getValueForYear(2023));
    }
}
