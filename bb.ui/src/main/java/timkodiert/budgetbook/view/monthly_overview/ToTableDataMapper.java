package timkodiert.budgetbook.view.monthly_overview;

import java.util.Objects;
import static java.util.stream.Collectors.toSet;

import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.model.UniqueTurnoverInformation;
import timkodiert.budgetbook.domain.table.RowType;

import static timkodiert.budgetbook.util.ObjectUtils.nvl;

public class ToTableDataMapper {

    private ToTableDataMapper() {
        // Statische Klasse
    }

    public static TableData mapUniqueExpense(UniqueTurnover expense) {
        String category = String.join(", ", expense.getPaymentInformations()
                                                   .stream()
                                                   .map(UniqueTurnoverInformation::getCategory)
                                                   .filter(Objects::nonNull)
                                                   .map(Category::getName)
                                                   .collect(toSet()));
        return new TableData(expense.getId(), expense.getBiller(), expense.getTotalValue(), expense.getDate(), category, expense.hasImport(), RowType.UNIQUE_EXPENSE);
    }

    public static TableData mapFixedExpense(FixedTurnover expense, MonthYear monthYear) {
        return new TableData(expense.getId(),
                             expense.getPosition(),
                             expense.getValueFor(monthYear),
                             expense.getImportDate(monthYear),
                             nvl(expense.getCategory(), Category::getName),
                             expense.hasImport(monthYear),
                             RowType.FIXED_EXPENSE);
    }
}
