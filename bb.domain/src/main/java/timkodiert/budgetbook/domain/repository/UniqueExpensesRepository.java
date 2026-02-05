package timkodiert.budgetbook.domain.repository;

import java.util.List;
import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.model.UniqueTurnover_;
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

    public List<UniqueTurnover> findAllWithoutFixedExpense(int year) {
        return findAll().stream()
                        .filter(exp -> exp.getFixedTurnover() == null)
                        .filter(exp -> exp.getDate().getYear() == year)
                        .toList();
    }

    public List<UniqueTurnover> findByLimitSortedByDateDesc(int limit) {
        var context = getQueryContext();
        var root = context.root();
        var query = context.query();
        query.select(root).orderBy(context.criteriaBuilder().desc(root.get(UniqueTurnover_.date)));
        return executeQuery(query, limit);
    }
}
