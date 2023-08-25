package timkodiert.budgetBook.domain.repository;

import javax.inject.Inject;

import jakarta.persistence.criteria.CriteriaQuery;
import timkodiert.budgetBook.domain.model.FixedExpense;

public class FixedExpensesRepository extends Repository<FixedExpense> {

    @Inject
    public FixedExpensesRepository() {
        super(FixedExpense.class);
    }

    public int getNumberOfExpensesWithPosition(String position) {
        CriteriaQuery<FixedExpense> query = entityManager.criteriaQuery(entityType);
        //query.groupBy(quer);
        return -1;
    }
}
