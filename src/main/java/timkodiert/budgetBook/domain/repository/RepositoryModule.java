package timkodiert.budgetBook.domain.repository;

import dagger.Binds;
import dagger.Module;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.FixedExpense;

@Module
public interface RepositoryModule {
    @Binds Repository<Category> provideCategoriesRepository(CategoriesRepository impl);
    @Binds Repository<FixedExpense> provideFixedExpensesRepository(FixedExpensesRepository impl);
}
