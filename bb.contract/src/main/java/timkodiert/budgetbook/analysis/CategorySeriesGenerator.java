package timkodiert.budgetbook.analysis;

import java.util.List;

import timkodiert.budgetbook.domain.model.Category;

public interface CategorySeriesGenerator {
    List<Double> generateCumulativeCategorySeries(AnalysisPeriod period, Category category);
    List<Double> generateCategorySeries(AnalysisPeriod period, Category category);
}
