package timkodiert.budgetbook.injector;

import javax.inject.Singleton;

import dagger.Component;

import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.db.MigrationService;
import timkodiert.budgetbook.db.MigrationView;
import timkodiert.budgetbook.domain.repository.RepositoryModule;
import timkodiert.budgetbook.exception.BbUncaughtExceptionHandler;
import timkodiert.budgetbook.properties.PropertiesServiceImpl;
import timkodiert.budgetbook.view.AnnualOverviewView;
import timkodiert.budgetbook.view.MainViewImpl;
import timkodiert.budgetbook.view.MonthlyOverviewView;
import timkodiert.budgetbook.view.about.AboutView;
import timkodiert.budgetbook.view.analysis.AnalysisView;
import timkodiert.budgetbook.view.category.CategoriesManageView;
import timkodiert.budgetbook.view.category.CategoryDetailView;
import timkodiert.budgetbook.view.category_group.CategoryGroupDetailView;
import timkodiert.budgetbook.view.category_group.CategoryGroupManageView;
import timkodiert.budgetbook.view.fixed_turnover.FixedTurnoverDetailView;
import timkodiert.budgetbook.view.fixed_turnover.FixedTurnoverManageView;
import timkodiert.budgetbook.view.importer.FixedTurnoverWizardView;
import timkodiert.budgetbook.view.importer.ImportView;
import timkodiert.budgetbook.view.unique_turnover.UniqueTurnoverDetailView;
import timkodiert.budgetbook.view.unique_turnover.UniqueTurnoverManageView;
import timkodiert.budgetbook.view.widget.BudgetWidget;

@Singleton
@Component(modules = {RepositoryModule.class, ServiceModule.class, AppModule.class, DbPathModule.class})
public interface ViewComponent {
    MainViewImpl getMainView();
    AboutView getAboutView();

    // -----------------------------------
    // Übersichten Ausgaben
    // -----------------------------------
    AnnualOverviewView getAnnualOverviewView();
    MonthlyOverviewView getMonthlyOverview();
    BudgetWidget getBudgetWidget();

    // -----------------------------------
    // Analyse
    // -----------------------------------
    AnalysisView getAnalysisView();

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
    UniqueTurnoverManageView getUniqueExpensesManageView();
    UniqueTurnoverDetailView getUniqueExpenseDetailView();

    // -----------------------------------
    // Umsätze importieren
    // -----------------------------------
    ImportView getImportView();
    FixedTurnoverWizardView getFixedTurnoverWizardView();

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
    BbUncaughtExceptionHandler getUncaughtExceptionHandler();
}
