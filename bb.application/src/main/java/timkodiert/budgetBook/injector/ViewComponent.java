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
import timkodiert.budgetBook.view.MonthlyOverview;
import timkodiert.budgetBook.view.category.CategoriesManageView;
import timkodiert.budgetBook.view.category.CategoryDetailView;
import timkodiert.budgetBook.view.category_group.CategoryGroupDetailView;
import timkodiert.budgetBook.view.category_group.CategoryGroupManageView;
import timkodiert.budgetBook.view.fixed_turnover.FixedTurnoverDetailView;
import timkodiert.budgetBook.view.fixed_turnover.FixedTurnoverManageView;
import timkodiert.budgetBook.view.unique_expense.UniqueExpenseDetailView;
import timkodiert.budgetBook.view.unique_expense.UniqueExpensesManageView;
import timkodiert.budgetBook.view.widget.BudgetWidget;

@Singleton
@Component(modules = {RepositoryModule.class, ServiceModule.class})
public interface ViewComponent {
    MainViewImpl getMainView();

    // -----------------------------------
    // Übersichten Ausgaben
    // -----------------------------------
    AnnualOverviewView getAnnualOverviewView();
    MonthlyOverview getMonthlyOverview();
    BudgetWidget getBudgetWidget();

    // -----------------------------------
    // Kategorien Ausgaben
    // -----------------------------------
    CategoriesManageView getManageCategoriesView();
    CategoryDetailView getCategoryDetailView();

    // -----------------------------------
    // Kategoriegruppen
    // -----------------------------------
    CategoryGroupManageView getCategoryGroupManageView();
    CategoryGroupDetailView getCategoryGroupDetailView();

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
