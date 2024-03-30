package timkodiert.budgetBook.table.monthlyOverview;

import java.time.LocalDate;

import timkodiert.budgetBook.util.HasType;
import timkodiert.budgetBook.view.MonthlyOverview.RowType;

public record TableData(String position, double value, LocalDate date, String categories, boolean hasImport, RowType type) implements HasType<RowType> {

    @Override
    public RowType getType() {
        return type;
    }
}
