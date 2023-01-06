package timkodiert.budgetBook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetBook.domain.model.FixedExpense;

public class FixedExpensesRepository extends Repository<FixedExpense> {

    @Inject
    public FixedExpensesRepository() {
        super(FixedExpense.class);
    }
}
