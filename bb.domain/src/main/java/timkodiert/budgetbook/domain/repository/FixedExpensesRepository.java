package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.util.EntityManager;

public class FixedExpensesRepository extends Repository<FixedTurnover> {

    @Inject
    public FixedExpensesRepository(EntityManager entityManager) {
        super(entityManager, FixedTurnover.class);
    }
}
