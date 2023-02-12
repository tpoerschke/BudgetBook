package timkodiert.budgetBook.view;

import javax.inject.Singleton;

import dagger.Component;
import timkodiert.budgetBook.domain.repository.RepositoryModule;

@Singleton
@Component(modules = RepositoryModule.class)
public interface ViewComponent {
    MainView getMainView();
    AnnualOverviewView getAnnualOverviewView();
    CompactOverviewView getCompactOverviewView();
    ManageCategoriesView getManageCategoriesView();
    NewCategoryView getNewCategoryView();
    ManageExpensesView getManageExpensesView();
    NewExpenseView getNewExpenseView();
}
