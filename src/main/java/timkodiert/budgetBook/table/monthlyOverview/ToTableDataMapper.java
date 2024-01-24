package timkodiert.budgetBook.table.monthlyOverview;

import static java.util.stream.Collectors.toSet;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.FixedTurnover;
import timkodiert.budgetBook.domain.model.MonthYear;
import timkodiert.budgetBook.domain.model.UniqueTurnover;
import timkodiert.budgetBook.view.MonthlyOverview.RowType;
import timkodiert.budgetBook.view.MonthlyOverview.TableData;

public class ToTableDataMapper {

    public static TableData mapUniqueExpense(UniqueTurnover expense) {
        String categories = String.join(", ", expense.getPaymentInformations().stream()
                .flatMap(info -> info.getCategories().stream()).map(Category::getName).collect(toSet()));
        return new TableData(expense.getBiller(), expense.getTotalValue(), expense.getDate(),
                categories, RowType.UNIQUE_EXPENSE);
    }

    public static TableData mapFixedExpense(FixedTurnover expense, MonthYear monthYear) {
        return new TableData(expense.getPosition(), expense.getValueFor(monthYear), null,
                String.join(", ", expense.getCategories().stream().map(Category::getName).collect(toSet())),
                RowType.FIXED_EXPENSE);
    }
}
