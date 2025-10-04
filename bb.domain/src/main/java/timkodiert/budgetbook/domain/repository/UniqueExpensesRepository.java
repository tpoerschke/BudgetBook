package timkodiert.budgetbook.domain.repository;

import java.util.List;
import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.util.EntityManager;

public class UniqueExpensesRepository extends Repository<UniqueTurnover> {

    @Inject
    public UniqueExpensesRepository(EntityManager entityManager) {
        super(entityManager, UniqueTurnover.class);
    }

    public List<UniqueTurnover> findAllWithoutFixedExpense(MonthYear monthYear) {
        return findAll().stream()
                        .filter(exp -> exp.getFixedTurnover() == null)
                        .filter(exp -> monthYear.containsDate(exp.getDate()))
                        .toList();
    }
}
