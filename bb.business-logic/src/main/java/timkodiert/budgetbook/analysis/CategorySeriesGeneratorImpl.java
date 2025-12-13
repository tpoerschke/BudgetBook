package timkodiert.budgetbook.analysis;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import jakarta.inject.Inject;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.repository.CategoriesRepository;

public class CategorySeriesGeneratorImpl implements CategorySeriesGenerator {

    private final CategoriesRepository categoryRepository;

    @Inject
    public CategorySeriesGeneratorImpl(CategoriesRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Double> generateCumulativeCategorySeries(AnalysisPeriod period, Reference<CategoryDTO> categoryRef) {
        Category category = loadCategory(categoryRef);
        List<YearMonth> monthYearList = period.getMonths();
        List<Number> items = new ArrayList<>(monthYearList.size());
        AtomicReference<Double> lastValue = new AtomicReference<>(0.0);
        IntStream.range(0, monthYearList.size()).forEach(i -> {
            int categoryVal = category.sumTurnoversForMonth(MonthYear.of(monthYearList.get(i)));
            items.add(lastValue.updateAndGet(v -> v + Math.min(categoryVal, 0)));
        });
        return items.stream().map(this::asEuro).map(Math::abs).toList();
    }

    @Override
    public List<Double> generateCategorySeries(AnalysisPeriod period, Reference<CategoryDTO> categoryRef) {
        Category category = loadCategory(categoryRef);
        List<YearMonth> monthYearList = period.getMonths();
        List<Double> items = new ArrayList<>(monthYearList.size());
        IntStream.range(0, monthYearList.size()).forEach(i -> {
            double categoryVal = category.sumTurnoversForMonth(MonthYear.of(monthYearList.get(i)));
            items.add(categoryVal);
        });
        return items.stream().map(item -> Math.min(item, 0)).map(this::asEuro).map(Math::abs).toList();
    }

    private double asEuro(Number value) {
        return value.intValue() / 100.0;
    }

    private Category loadCategory(Reference<CategoryDTO> ref) {
        return categoryRepository.findById(ref.id());
    }
}
