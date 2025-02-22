package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.UniqueTurnover;

public class UniqueExpensesRepository extends Repository<UniqueTurnover> {

    @Inject
    public UniqueExpensesRepository() {
        super(UniqueTurnover.class);
    }
}
