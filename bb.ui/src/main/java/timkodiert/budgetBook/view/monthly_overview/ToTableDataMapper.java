package timkodiert.budgetBook.view.monthly_overview;

import static java.util.stream.Collectors.toSet;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.FixedTurnover;
import timkodiert.budgetBook.domain.model.MonthYear;
import timkodiert.budgetBook.domain.model.UniqueTurnover;
import timkodiert.budgetBook.table.RowType;

public class ToTableDataMapper {

    public static TableData mapUniqueExpense(UniqueTurnover expense) {
        String categories = String.join(", ",
                                        expense.getPaymentInformations().stream().flatMap(info -> info.getCategories().stream()).map(Category::getName).collect(toSet()));
        return new TableData(expense.getId(), expense.getBiller(), expense.getTotalValue(), expense.getDate(), categories, expense.hasImport(), RowType.UNIQUE_EXPENSE);
    }

    public static TableData mapFixedExpense(FixedTurnover expense, MonthYear monthYear) {
        return new TableData(expense.getId(),
                             expense.getPosition(),
                             expense.getValueFor(monthYear),
                             expense.getImportDate(monthYear),
                             String.join(", ", expense.getCategories().stream().map(Category::getName).collect(toSet())),
                             expense.hasImport(monthYear),
                             RowType.FIXED_EXPENSE);
    }
}
