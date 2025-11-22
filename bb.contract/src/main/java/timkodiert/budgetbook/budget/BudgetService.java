package timkodiert.budgetbook.budget;

import java.time.YearMonth;
import java.util.List;

import org.jspecify.annotations.Nullable;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;

public interface BudgetService {

    List<Reference<CategoryDTO>> findCategoriesWithActiveBudget();
    @Nullable BudgetState getBudgetState(Reference<CategoryDTO> category, YearMonth yearMonth);
}
