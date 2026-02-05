package timkodiert.budgetbook.domain.repository;

import dagger.Binds;
import dagger.Module;

import timkodiert.budgetbook.domain.model.AccountTurnover;
import timkodiert.budgetbook.domain.model.Billing;
import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.CategoryGroup;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.ImportRule;
import timkodiert.budgetbook.domain.model.PaymentInformation;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.model.UniqueTurnoverInformation;

@Module
public interface RepositoryModule {

    @Binds
    Repository<Category> provideCategoriesRepository(CategoriesRepository impl);

    @Binds
    Repository<CategoryGroup> provideCategoryGroupsRepository(CategoryGroupsRepository impl);

    @Binds
    Repository<FixedTurnover> provideFixedExpensesRepository(FixedExpensesRepository impl);

    @Binds
    Repository<PaymentInformation> providePaymentInformationsRepository(PaymentInformationsRepository impl);

    @Binds
    Repository<UniqueTurnover> provideUniqueExpensesRepository(UniqueExpensesRepository impl);

    @Binds
    Repository<UniqueTurnoverInformation> provideUniqueExpenseInformationRepository(UniqueExpenseInformationRepository impl);

    @Binds
    Repository<AccountTurnover> provideAccountTurnoverRepository(AccountTurnoverRepository impl);

    @Binds
    Repository<ImportRule> provideImportRulesRepository(ImportRulesRepository impl);

    @Binds
    Repository<Billing> provideBillingRepository(BillingRepository impl);
}
