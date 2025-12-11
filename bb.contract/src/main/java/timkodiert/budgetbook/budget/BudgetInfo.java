package timkodiert.budgetbook.budget;

import timkodiert.budgetbook.domain.model.BudgetType;

public record BudgetInfo(int budgetValue, BudgetType budgetType) {
}
