package timkodiert.budgetBook.domain.repository;

import dagger.Binds;
import dagger.Module;

import timkodiert.budgetBook.domain.model.AccountTurnover;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.FixedTurnover;
import timkodiert.budgetBook.domain.model.ImportRule;
import timkodiert.budgetBook.domain.model.PaymentInformation;
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.model.UniqueExpenseInformation;

@Module
public interface RepositoryModule {

    @Binds
    Repository<Category> provideCategoriesRepository(CategoriesRepository impl);

    @Binds
    Repository<FixedTurnover> provideFixedExpensesRepository(FixedExpensesRepository impl);

    @Binds
    Repository<PaymentInformation> providePaymentInformationsRepository(PaymentInformationsRepository impl);

    @Binds
    Repository<UniqueExpense> provideUniqueExpensesRepository(UniqueExpensesRepository impl);

    @Binds
    Repository<UniqueExpenseInformation> provideUniqueExpenseInformationRepository(UniqueExpenseInformationRepository impl);

    @Binds
    Repository<AccountTurnover> provideAccountTurnoverRepository(AccountTurnoverRepository impl);

    @Binds
    Repository<ImportRule> provideImportRulesRepository(ImportRulesRepository impl);
}
