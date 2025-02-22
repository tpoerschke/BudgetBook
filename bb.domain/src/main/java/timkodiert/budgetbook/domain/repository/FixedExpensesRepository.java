package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.FixedTurnover;

public class FixedExpensesRepository extends Repository<FixedTurnover> {

    @Inject
    public FixedExpensesRepository() {
        super(FixedTurnover.class);
    }
}
