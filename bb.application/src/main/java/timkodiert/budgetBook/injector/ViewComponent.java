package timkodiert.budgetBook.injector;

import javax.inject.Singleton;

import dagger.Component;

import timkodiert.budgetBook.converter.Converters;
import timkodiert.budgetBook.db.MigrationService;
import timkodiert.budgetBook.db.MigrationView;
import timkodiert.budgetBook.domain.repository.RepositoryModule;
import timkodiert.budgetBook.properties.PropertiesServiceImpl;
import timkodiert.budgetBook.view.AnnualOverviewView;
import timkodiert.budgetBook.view.ImportView;
import timkodiert.budgetBook.view.MainViewImpl;
import timkodiert.budgetBook.view.ManageCategoriesView;
import timkodiert.budgetBook.view.MonthlyOverview;
import timkodiert.budgetBook.view.NewCategoryView;
import timkodiert.budgetBook.view.fixed_turnover.FixedTurnoverDetailView;
import timkodiert.budgetBook.view.fixed_turnover.FixedTurnoverManageView;
import timkodiert.budgetBook.view.unique_expense.UniqueExpenseDetailView;
import timkodiert.budgetBook.view.unique_expense.UniqueExpensesManageView;

@Singleton
@Component(modules = {RepositoryModule.class, ServiceModule.class})
public interface ViewComponent {
    MainViewImpl getMainView();

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
    // Technische Ansichten & Services
    // -----------------------------------
    MigrationService getMigrationService();
    MigrationView getMigrationView();

    PropertiesServiceImpl getPropertiesService();

    // -----------------------------------
    // Sonstiges
    // -----------------------------------
    Converters getConverters();
}
