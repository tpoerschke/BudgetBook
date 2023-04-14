package timkodiert.budgetBook.view;

import javax.inject.Singleton;

import dagger.Component;
import timkodiert.budgetBook.domain.repository.RepositoryModule;
import timkodiert.budgetBook.view.uniqueExpenses.UniqueExpenseDetailView;
import timkodiert.budgetBook.view.uniqueExpenses.UniqueExpensesManageView;

@Singleton
@Component(modules = RepositoryModule.class)
public interface ViewComponent {
    MainView getMainView();

    // -----------------------------------
    // Übersichten Ausgaben
    // -----------------------------------

    AnnualOverviewView getAnnualOverviewView();

    CompactOverviewView getCompactOverviewView();

    MonthlyOverview getMonthlyOverview();

    // -----------------------------------
    // Kategorien Ausgaben
    // -----------------------------------

    ManageCategoriesView getManageCategoriesView();

    NewCategoryView getNewCategoryView();

    // -----------------------------------
    // Regelmäßige Ausgaben
    // -----------------------------------

    ManageExpensesView getManageExpensesView();

    NewExpenseView getNewExpenseView();

    EditExpenseView getEditExpenseView();

    // -----------------------------------
    // Einzigartige Ausgaben
    // -----------------------------------

    UniqueExpensesManageView getUniqueExpensesManageView();

    UniqueExpenseDetailView getUniqueExpenseDetailView();
}
