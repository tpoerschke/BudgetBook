package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.util.EntityManager;

public class UniqueExpensesRepository extends Repository<UniqueTurnover> {

    @Inject
    public UniqueExpensesRepository(EntityManager entityManager) {
        super(entityManager, UniqueTurnover.class);
    }
}
