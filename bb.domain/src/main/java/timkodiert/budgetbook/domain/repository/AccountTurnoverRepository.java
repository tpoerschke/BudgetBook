package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.AccountTurnover;

public class AccountTurnoverRepository extends Repository<AccountTurnover> {

    @Inject
    public AccountTurnoverRepository() {
        super(AccountTurnover.class);
    }
}
