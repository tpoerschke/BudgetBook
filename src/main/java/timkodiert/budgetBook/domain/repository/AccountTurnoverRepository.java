package timkodiert.budgetBook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetBook.domain.model.AccountTurnover;

public class AccountTurnoverRepository extends Repository<AccountTurnover> {

    @Inject
    public AccountTurnoverRepository() {
        super(AccountTurnover.class);
    }
}
