package timkodiert.budgetbook.domain.model;

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

    @Test
    @DisplayName("getUniqueTurnoverInformation(MonthYear)")
    void testGetUniqueTurnoverInformationByMonthYear() {
        // Arrange
        MonthYear monthYear = MonthYear.of(1, 2024);
        LocalDate date = LocalDate.of(2024, 1, 1);
        Category category = new Category();
        category.setBudgetType(BudgetType.MONTHLY);
        FixedTurnover fixedTurnover = TestDataProvider.createFixedTurnover(-9999, monthYear, null);
        UniqueTurnover uniqueTurnover1 = TestDataProvider.createUniqueTurnover(date, "TEST 1", -9999);
        fixedTurnover.getUniqueTurnovers().add(uniqueTurnover1);
        uniqueTurnover1.setFixedTurnover(fixedTurnover);
        UniqueTurnover uniqueTurnover2 = TestDataProvider.createUniqueTurnover(date, "TEST 2", -1);
        UniqueTurnover uniqueTurnover3 = TestDataProvider.createUniqueTurnover(date.plusMonths(1), "TEST 3", -9999);
        category.getUniqueTurnoverInformation().add(uniqueTurnover1.getPaymentInformations().getFirst());
        category.getUniqueTurnoverInformation().add(uniqueTurnover2.getPaymentInformations().getFirst());
        category.getUniqueTurnoverInformation().add(uniqueTurnover3.getPaymentInformations().getFirst());
        // Act
        List<UniqueTurnoverInformation> infoList = category.getUniqueTurnoverInformation(monthYear);
        // Assert
        assertEquals(1, infoList.size());
        assertEquals("TEST 2", infoList.getFirst().getExpense().getBiller());
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
        @DisplayName("Kategorie mit Ausgaben: Konkrete Umsätze, die zu einem wiederkehrenden Umsatz zugeordnet sind, werden ignoriert")
        void testTurnoverSumIgnoreUniqueTurnoverLinkedToFixedTurnover() {
            // Arrange
            MonthYear monthYear = MonthYear.of(1, 2024);
            LocalDate date = LocalDate.of(2024, 1, 1);
            Category category = new Category();
            category.setBudgetType(BudgetType.MONTHLY);
            FixedTurnover fixedTurnover = TestDataProvider.createFixedTurnover(-1, monthYear, null);
            UniqueTurnover uniqueTurnover = TestDataProvider.createUniqueTurnoverWithAccountTurnover(date, "TEST 1", -2);
            fixedTurnover.getUniqueTurnovers().add(uniqueTurnover);
            uniqueTurnover.setFixedTurnover(fixedTurnover);
            category.getFixedExpenses().add(fixedTurnover);
            category.getUniqueTurnoverInformation().add(uniqueTurnover.getPaymentInformations().getFirst());
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            assertEquals(-2, sum);
        }

        @Test
        @DisplayName("Kategorie mit Ausgaben: Summiert wiederkehrende Umsätze und konkrete Umsätze (bzw. Informationen)")
        void testTurnoverSumWithExpenses() {
            // Arrange
            MonthYear monthYear = MonthYear.now();
            Category category = new Category();
            category.setBudgetType(BudgetType.MONTHLY);
            category.getFixedExpenses().addAll(generateFixedExpenses(monthYear, 10.0, 10.0));
            category.getUniqueTurnoverInformation().addAll(generateUniqueTurnoverInformation(monthYear, -15.1, -15.1));
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            assertEquals(2 * -10 + 2 * -15.1, sum);
        }

        @Test
        @DisplayName("Kategorie mit Ausgaben: Bei konkreten Umsätzen wird die Richtung beachtet")
        void testTurnoverSumWithDirection() {
            // Arrange
            MonthYear monthYear = MonthYear.now();
            Category category = new Category();
            category.setBudgetType(BudgetType.MONTHLY);
            category.getUniqueTurnoverInformation().addAll(generateUniqueTurnoverInformation(monthYear, -20.0, 30.5));
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            assertEquals(10.5, sum);
        }

        @Test
        @DisplayName("Kategorie mit Ausgaben: Bei konkreten Umsätzen wird der Monat beachtet")
        void testTurnoverSumWithMonthFilter() {
            // Arrange
            MonthYear monthYear = MonthYear.now();
            Category category = new Category();
            category.setBudgetType(BudgetType.MONTHLY);
            category.getUniqueTurnoverInformation().addAll(generateUniqueTurnoverInformation(monthYear, -20.0, 30.5));
            category.getUniqueTurnoverInformation().add(createUniqueTurnover(monthYear.plusMonths(1), 100.0).getPaymentInformations().get(0));
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
        @DisplayName("Kategorie mit Ausgaben: Konkrete Umsätze, die zu einem wiederkehrenden Umsatz zugeordnet sind, werden ignoriert")
        void testTurnoverSumIgnoreUniqueTurnoverLinkedToFixedTurnover() {
            // Arrange
            MonthYear monthYear = MonthYear.of(1, 2024);
            LocalDate date = LocalDate.of(2024, 1, 1);
            Category category = new Category();
            category.setBudgetType(BudgetType.ANNUAL);
            FixedTurnover fixedTurnover = TestDataProvider.createFixedTurnover(-1, monthYear, monthYear);
            UniqueTurnover uniqueTurnover = TestDataProvider.createUniqueTurnoverWithAccountTurnover(date, "TEST 1", -2);
            fixedTurnover.getUniqueTurnovers().add(uniqueTurnover);
            uniqueTurnover.setFixedTurnover(fixedTurnover);
            category.getFixedExpenses().add(fixedTurnover);
            category.getUniqueTurnoverInformation().add(uniqueTurnover.getPaymentInformations().getFirst());
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            assertEquals(-2, sum);
        }

        @Test
        @DisplayName("Kategorie mit Ausgaben: Summiert wiederkehrende Umsätze und konkrete Umsätze für ein Jahr")
        void testTurnoverSumWithExpenses() {
            // Arrange
            MonthYear monthYear = MonthYear.of(11, 2024);
            Category category = new Category();
            category.setBudgetType(BudgetType.ANNUAL);
            category.getFixedExpenses().addAll(generateFixedExpenses(monthYear, 10.0, 10.0));
            category.getFixedExpenses().addAll(generateFixedExpenses(monthYear.plusMonths(1), 10.0, 10.0));
            category.getFixedExpenses().addAll(generateFixedExpenses(monthYear.plusMonths(12), 100.0));
            category.getUniqueTurnoverInformation().addAll(generateUniqueTurnoverInformation(monthYear, -15.1, -15.1));
            category.getUniqueTurnoverInformation().add(createUniqueTurnover(monthYear.plusMonths(12), 100.0).getPaymentInformations().get(0));
            // Act
            double sum = category.sumTurnovers(monthYear);
            // Assert
            // Regelmäßige Ausgaben werden innerhalb eines Jahres aufsummiert
            assertEquals(6 * -10 + 2 * -15.1, sum);
        }

        @Test
        @DisplayName("Kategorie mit Ausgaben: Bei konkreten Umsätzen wird die Richtung beachtet")
        void testTurnoverSumWithDirection() {
            // Arrange
            MonthYear monthYear = MonthYear.now();
            Category category = new Category();
            category.setBudgetType(BudgetType.ANNUAL);
            category.getUniqueTurnoverInformation().addAll(generateUniqueTurnoverInformation(monthYear, -20.0, 30.5));
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