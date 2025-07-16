package timkodiert.budgetbook.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import jakarta.inject.Inject;

import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.MonthYear;

public class CategorySeriesGeneratorImpl implements CategorySeriesGenerator {

    @Inject
    public CategorySeriesGeneratorImpl() {
        // noop
    }

    public List<Double> generateCumulativeCategorySeries(AnalysisPeriod period, Category category) {
        List<MonthYear> monthYearList = period.getMonths();
        List<Number> items = new ArrayList<>(monthYearList.size());
        AtomicReference<Double> lastValue = new AtomicReference<>(0.0);
        IntStream.range(0, monthYearList.size()).forEach(i -> {
            double categoryVal = category.sumTurnoversForMonth(monthYearList.get(i));
            items.add(lastValue.updateAndGet(v -> v + Math.min(categoryVal, 0)));
        });
        return items.stream().map(this::round).map(Math::abs).toList();
    }

    public List<Double> generateCategorySeries(AnalysisPeriod period, Category category) {
        List<MonthYear> monthYearList = period.getMonths();
        List<Double> items = new ArrayList<>(monthYearList.size());
        IntStream.range(0, monthYearList.size()).forEach(i -> {
            double categoryVal = category.sumTurnoversForMonth(monthYearList.get(i));
            items.add(categoryVal);
        });
        return items.stream().map(item -> Math.min(item, 0)).map(this::round).map(Math::abs).toList();
    }

    private double round(Number value) {
        return Math.round(value.doubleValue() * 100) / 100.0;
    }
}
