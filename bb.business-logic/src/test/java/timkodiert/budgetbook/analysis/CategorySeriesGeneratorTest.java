package timkodiert.budgetbook.analysis;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.model.UniqueTurnoverInformation;
import timkodiert.budgetbook.domain.repository.CategoriesRepository;

@ExtendWith(MockitoExtension.class)
class CategorySeriesGeneratorTest {

    @Mock
    private CategoriesRepository categoriesRepository;
    private CategorySeriesGenerator generator;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        generator = new CategorySeriesGenerator(categoriesRepository);

        category1 = createCategory("Test category 1",
                                   createTurnover(LocalDate.of(2025, 1, 1), -100),
                                   createTurnover(LocalDate.of(2025, 2, 1), -101));
        category2 = createCategory("Test category 2", createTurnover(LocalDate.of(2025, 1, 1), -200));

        when(categoriesRepository.findAll()).thenAnswer(invocation -> Stream.of(category1, category2).filter(Objects::nonNull).toList());
    }

    @Test
    @DisplayName("1 Kategorie, 1 Monat")
    void testSingleCategorySingleMonthWithExpenses() {
        // Arrange
        category2 = null;
        MonthYear from = MonthYear.of(1, 2025);
        MonthYear to = MonthYear.of(1, 2025);

        // Act
        List<XYChart.Series<String, Number>> seriesList = generator.generateSeries(from, to);

        // Assert
        assertEquals(1, seriesList.size());
        XYChart.Series<String, Number> series = seriesList.getFirst();
        assertSeries(series, "Test category 1", new ExpectedDataPoint("1.2025", -100.0));
    }

    @Test
    @DisplayName("1 Kategorie, 2 Monate")
    void testSingleCategoryManyMonthWithExpenses() {
        // Arrange
        category2 = null;
        MonthYear from = MonthYear.of(1, 2025);
        MonthYear to = MonthYear.of(2, 2025);

        // Act
        List<XYChart.Series<String, Number>> seriesList = generator.generateSeries(from, to);

        // Assert
        assertEquals(1, seriesList.size());
        XYChart.Series<String, Number> series = seriesList.getFirst();
        assertSeries(series, "Test category 1", new ExpectedDataPoint("1.2025", -100.0), new ExpectedDataPoint("2.2025", -101.0));
    }

    @Test
    @DisplayName("2 Kategorie, 2 Monate")
    void testManyCategoryManyMonthWithExpenses() {
        // Arrange
        MonthYear from = MonthYear.of(1, 2025);
        MonthYear to = MonthYear.of(2, 2025);

        // Act
        List<XYChart.Series<String, Number>> seriesList = generator.generateSeries(from, to);

        // Assert
        assertEquals(2, seriesList.size());
        XYChart.Series<String, Number> series1 = seriesList.getFirst();
        XYChart.Series<String, Number> series2 = seriesList.get(1);
        assertSeries(series1, "Test category 1", new ExpectedDataPoint("1.2025", -100.0), new ExpectedDataPoint("2.2025", -101.0));
        assertSeries(series2, "Test category 2", new ExpectedDataPoint("1.2025", -200.0), new ExpectedDataPoint("2.2025", 0.0));
    }

    private void assertSeries(XYChart.Series<String, Number> series, String label, ExpectedDataPoint... expectedDataPoints) {
        assertEquals(label, series.getName());
        assertEquals(expectedDataPoints.length, series.getData().size());
        IntStream.range(0, expectedDataPoints.length).forEach(i -> {
            assertEquals(expectedDataPoints[i].label(), series.getData().get(i).getXValue());
            assertEquals(expectedDataPoints[i].value(), series.getData().get(i).getYValue());
        });
    }

    private Category createCategory(String name, UniqueTurnoverInformation... turnoverInformation) {
        Category category = new Category();
        category.setName(name);
        Arrays.stream(turnoverInformation).forEach(category.getUniqueExpenseInformation()::add);
        return category;
    }

    private UniqueTurnoverInformation createTurnover(LocalDate date, double value) {
        UniqueTurnover ut = new UniqueTurnover();
        ut.setDate(date);
        return UniqueTurnoverInformation.total(ut, value);
    }

    record ExpectedDataPoint(String label, double value) {}
}