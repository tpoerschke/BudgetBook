package timkodiert.budgetBook.view;

import javax.inject.Singleton;

import dagger.Component;

import timkodiert.budgetBook.domain.repository.RepositoryModule;
import timkodiert.budgetBook.view.fixed_expense.FixedExpenseDetailView;
import timkodiert.budgetBook.view.fixed_expense.FixedExpensesManageView;
import timkodiert.budgetBook.view.unique_expense.UniqueExpenseDetailView;
import timkodiert.budgetBook.view.unique_expense.UniqueExpensesManageView;

@Singleton
@Component(modules = RepositoryModule.class)
public interface ViewComponent {
    MainView getMainView();

    // -----------------------------------
    // Übersichten Ausgaben
    // -----------------------------------
    AnnualOverviewView getAnnualOverviewView();
    MonthlyOverview getMonthlyOverview();

    // -----------------------------------
    // Kategorien Ausgaben
    // -----------------------------------
    ManageCategoriesView getManageCategoriesView();
    NewCategoryView getNewCategoryView();

    // -----------------------------------
    // Regelmäßige Ausgaben
    // -----------------------------------
    FixedExpensesManageView getFixedExpenseManageView();
    NewExpenseView getNewExpenseView();
    FixedExpenseDetailView getEditExpenseView();

    // -----------------------------------
    // Einzigartige Ausgaben
    // -----------------------------------
    UniqueExpensesManageView getUniqueExpensesManageView();
    UniqueExpenseDetailView getUniqueExpenseDetailView();

    // -----------------------------------
    // Umsätze importieren
    // -----------------------------------
    ImportView getImportView();
}
