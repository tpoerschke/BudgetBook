package timkodiert.budgetBook.view;

import javax.inject.Singleton;

import dagger.Component;

import timkodiert.budgetBook.domain.repository.RepositoryModule;
import timkodiert.budgetBook.util.MigrationService;
import timkodiert.budgetBook.view.fixed_turnover.FixedTurnoverDetailView;
import timkodiert.budgetBook.view.fixed_turnover.FixedTurnoverManageView;
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
    // Regelmäßige Umsätze
    // -----------------------------------
    FixedTurnoverManageView getFixedTurnoverManageView();
    FixedTurnoverDetailView getFixedTurnoverDetailView();

    // -----------------------------------
    // Einzigartige Ausgaben
    // -----------------------------------
    UniqueExpensesManageView getUniqueExpensesManageView();
    UniqueExpenseDetailView getUniqueExpenseDetailView();

    // -----------------------------------
    // Umsätze importieren
    // -----------------------------------
    ImportView getImportView();

    // -----------------------------------
    // Technische Ansichten
    // -----------------------------------
    MigrationService getMigrationService();
    MigrationView getMigrationView();
}
