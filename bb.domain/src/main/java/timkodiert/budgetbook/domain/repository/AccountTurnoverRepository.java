package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.AccountTurnover;
import timkodiert.budgetbook.domain.util.EntityManager;

public class AccountTurnoverRepository extends Repository<AccountTurnover> {

    @Inject
    public AccountTurnoverRepository(EntityManager entityManager) {
        super(entityManager, AccountTurnover.class);
    }
}
