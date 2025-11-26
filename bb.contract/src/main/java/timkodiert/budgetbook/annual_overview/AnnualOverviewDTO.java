package timkodiert.budgetbook.annual_overview;

import java.util.List;

public record AnnualOverviewDTO(List<TableRowData> expensesRowData, TableRowData incomeSum, TableRowData expenseSum) {
}
