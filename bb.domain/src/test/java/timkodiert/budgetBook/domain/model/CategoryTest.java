package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    @ParameterizedTest
    @DisplayName("Budget aktiv: Jede Mögliche Kombination")
    @CsvSource(value = {"null, false, false", "null, true, false", "100, false, false", "100, true, true"}, nullValues = "null")
    void testHasActiveBudget(Double value, boolean active, boolean expected) {
        // Arrange
        Category category = new Category();
        category.setBudgetValue(value);
        category.setBudgetActive(active);
        // Act
        boolean hasActiveBudget = category.hasActiveBudget();
        // Assert
        assertEquals(expected, hasActiveBudget);
    }

    @Nested
    @DisplayName("Monatliches Budget")
    class MonthlyBudgetTests {

        @Test
        @DisplayName("Kategorie ohne Ausgaben: Summe von 0.0")
        void testTurnoverSumWithoutExpenses() {
            // Arrange
            MonthYear monthYear = MonthYear.now();
            Category category = new Category();
            category.setBudgetType(BudgetType.MONTHLY);
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            assertEquals(0.0, sum);
        }

        @Test
        @DisplayName("Kategorie mit Ausgaben: Summiert Regelmäßige Ausgaben und Einzigartige Ausgaben (bzw. Informationen)")
        void testTurnoverSumWithExpenses() {
            // Arrange
            MonthYear monthYear = MonthYear.now();
            Category category = new Category();
            category.setBudgetType(BudgetType.MONTHLY);
            category.getFixedExpenses().addAll(generateFixedExpenses(monthYear, 10.0, 10.0));
            category.getUniqueExpenseInformation().addAll(generateUniqueTurnoverInformation(monthYear, -15.1, -15.1));
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            assertEquals(2 * -10 + 2 * -15.1, sum);
        }

        @Test
        @DisplayName("Kategorie mit Ausgaben: Bei Einzigartigen Ausgaben wird die Richtung beachtet")
        void testTurnoverSumWithDirection() {
            // Arrange
            MonthYear monthYear = MonthYear.now();
            Category category = new Category();
            category.setBudgetType(BudgetType.MONTHLY);
            category.getUniqueExpenseInformation().addAll(generateUniqueTurnoverInformation(monthYear, -20.0, 30.5));
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            assertEquals(10.5, sum);
        }

        @Test
        @DisplayName("Kategorie mit Ausgaben: Bei Einzigartigen Ausgaben wird der Monat beachtet")
        void testTurnoverSumWithMonthFilter() {
            // Arrange
            MonthYear monthYear = MonthYear.now();
            Category category = new Category();
            category.setBudgetType(BudgetType.MONTHLY);
            category.getUniqueExpenseInformation().addAll(generateUniqueTurnoverInformation(monthYear, -20.0, 30.5));
            category.getUniqueExpenseInformation().add(createUniqueTurnover(monthYear.plusMonths(1), 100.0).getPaymentInformations().get(0));
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            assertEquals(10.5, sum);
        }
    }

    @Nested
    @DisplayName("Jährliches Budget")
    class AnnualBudgetTests {

        @Test
        @DisplayName("Kategorie ohne Ausgaben: Summe von 0.0")
        void testTurnoverSumWithoutExpenses() {
            // Arrange
            MonthYear monthYear = MonthYear.now();
            Category category = new Category();
            category.setBudgetType(BudgetType.ANNUAL);
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            assertEquals(0.0, sum);
        }

        @Test
        @DisplayName("Kategorie mit Ausgaben: Summiert Regelmäßige Ausgaben und Einzigartige Ausgaben für ein Jahr")
        void testTurnoverSumWithExpenses() {
            // Arrange
            MonthYear monthYear = MonthYear.of(11, 2024);
            Category category = new Category();
            category.setBudgetType(BudgetType.ANNUAL);
            category.getFixedExpenses().addAll(generateFixedExpenses(monthYear, 10.0, 10.0));
            category.getFixedExpenses().addAll(generateFixedExpenses(monthYear.plusMonths(1), 10.0, 10.0));
            category.getFixedExpenses().addAll(generateFixedExpenses(monthYear.plusMonths(12), 100.0));
            category.getUniqueExpenseInformation().addAll(generateUniqueTurnoverInformation(monthYear, -15.1, -15.1));
            category.getUniqueExpenseInformation().add(createUniqueTurnover(monthYear.plusMonths(12), 100.0).getPaymentInformations().get(0));
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            // Regelmäßige Ausgaben werden innerhalb eines Jahres aufsummiert
            assertEquals(6 * -10 + 2 * -15.1, sum);
        }

        @Test
        @DisplayName("Kategorie mit Ausgaben: Bei Einzigartigen Ausgaben wird die Richtung beachtet")
        void testTurnoverSumWithDirection() {
            // Arrange
            MonthYear monthYear = MonthYear.now();
            Category category = new Category();
            category.setBudgetType(BudgetType.ANNUAL);
            category.getUniqueExpenseInformation().addAll(generateUniqueTurnoverInformation(monthYear, -20.0, 30.5));
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            assertEquals(10.5, sum);
        }
    }

    private List<FixedTurnover> generateFixedExpenses(MonthYear monthYear, Double... values) {
        return Stream.of(values).map(v -> {
            FixedTurnover t = new FixedTurnover();
            t.setDirection(TurnoverDirection.OUT);
            PaymentInformation pi1 = new PaymentInformation(t, v, IntStream.rangeClosed(1, 12).boxed().toList(), PaymentType.MONTHLY, monthYear, null);
            t.getPaymentInformations().add(pi1);
            return t;
        }).toList();
    }

    private List<UniqueTurnoverInformation> generateUniqueTurnoverInformation(MonthYear monthYear, Double... values) {
        return Stream.of(values)
                     .map(v -> createUniqueTurnover(monthYear, v))
                     .map(UniqueTurnover::getPaymentInformations)
                     .flatMap(List::stream)
                     .toList();
    }

    private UniqueTurnover createUniqueTurnover(MonthYear monthYear, double value) {
        UniqueTurnover turnover = new UniqueTurnover();
        turnover.setDate(LocalDate.of(monthYear.getYear(), monthYear.getMonth(), 1));
        turnover.getPaymentInformations().add(UniqueTurnoverInformation.total(turnover, value));
        return turnover;
    }
}