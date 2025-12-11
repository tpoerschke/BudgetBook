package timkodiert.budgetbook.analysis;

import java.time.YearMonth;
import java.util.List;

import jakarta.inject.Inject;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.model.UniqueTurnoverInformation;
import timkodiert.budgetbook.domain.repository.CategoriesRepository;
import timkodiert.budgetbook.domain.table.RowType;
import timkodiert.budgetbook.util.CollectionUtils;

public class AnalysisServiceImpl implements AnalysisService {

    private final CategoriesRepository categoryRepository;

    @Inject
    public AnalysisServiceImpl(CategoriesRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<TableRowData> getTurnoverList(Reference<CategoryDTO> categoryRef, YearMonth yearMonth) {
        MonthYear monthYear = MonthYear.of(yearMonth);
        Category category = categoryRepository.findById(categoryRef.id());
        List<TableRowData> fixedTurnovers = findRelevantFixedTurnovers(category, monthYear).stream()
                                                                                           .map(turnover -> map(turnover, monthYear))
                                                                                           .toList();
        List<TableRowData> uniqueTurnovers = category.getUniqueTurnoverInformation(monthYear).
                                                     stream()
                                                     .map(UniqueTurnoverInformation::getExpense)
                                                     .distinct()
                                                     .map(this::map)
                                                     .toList();

        return CollectionUtils.union(fixedTurnovers, uniqueTurnovers);
    }

    private TableRowData map(FixedTurnover fixedTurnover, MonthYear monthYear) {
        return new TableRowData(fixedTurnover.getPosition(), fixedTurnover.getValueFor(monthYear), RowType.FIXED_EXPENSE);
    }

    private TableRowData map(UniqueTurnover turnover) {
        return new TableRowData(turnover.getBiller(), turnover.getTotalValue(), RowType.UNIQUE_EXPENSE);
    }

    private List<FixedTurnover> findRelevantFixedTurnovers(Category category, MonthYear monthYear) {
        return category.getFixedExpenses()
                       .stream()
                       .filter(turnover -> turnover.getValueFor(monthYear) != 0.0)
                       .toList();
    }
}
