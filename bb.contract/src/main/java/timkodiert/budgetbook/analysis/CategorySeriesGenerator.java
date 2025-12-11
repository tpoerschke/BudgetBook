package timkodiert.budgetbook.analysis;

import java.util.List;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;

public interface CategorySeriesGenerator {
    List<Double> generateCumulativeCategorySeries(AnalysisPeriod period, Reference<CategoryDTO> categoryRef);
    List<Double> generateCategorySeries(AnalysisPeriod period, Reference<CategoryDTO> category);
}
