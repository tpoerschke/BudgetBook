package timkodiert.budgetBook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetBook.domain.model.FixedTurnover;

public class FixedExpensesRepository extends Repository<FixedTurnover> {

    @Inject
    public FixedExpensesRepository() {
        super(FixedTurnover.class);
    }
}
