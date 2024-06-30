package timkodiert.budgetBook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetBook.domain.model.UniqueTurnover;

public class UniqueExpensesRepository extends Repository<UniqueTurnover> {

    @Inject
    public UniqueExpensesRepository() {
        super(UniqueTurnover.class);
    }
}
