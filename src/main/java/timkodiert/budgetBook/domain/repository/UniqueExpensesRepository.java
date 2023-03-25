package timkodiert.budgetBook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetBook.domain.model.UniqueExpense;

public class UniqueExpensesRepository extends Repository<UniqueExpense> {

    @Inject
    public UniqueExpensesRepository() {
        super(UniqueExpense.class);
    }
}
