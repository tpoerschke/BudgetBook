package timkodiert.budgetbook.injector;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.inject.Inject;

import timkodiert.budgetbook.db.MigrationView;
import timkodiert.budgetbook.view.AnnualOverviewView;
import timkodiert.budgetbook.view.MonthlyOverview;
import timkodiert.budgetbook.view.View;
import timkodiert.budgetbook.view.analysis.AnalysisView;
import timkodiert.budgetbook.view.category.CategoriesManageView;
import timkodiert.budgetbook.view.category.CategoryDetailView;
import timkodiert.budgetbook.view.category_group.CategoryGroupDetailView;
import timkodiert.budgetbook.view.category_group.CategoryGroupManageView;
import timkodiert.budgetbook.view.fixed_turnover.FixedTurnoverDetailView;
import timkodiert.budgetbook.view.fixed_turnover.FixedTurnoverManageView;
import timkodiert.budgetbook.view.importer.FixedTurnoverWizardView;
import timkodiert.budgetbook.view.importer.ImportView;
import timkodiert.budgetbook.view.unique_expense.UniqueExpenseDetailView;
import timkodiert.budgetbook.view.unique_expense.UniqueExpensesManageView;
import timkodiert.budgetbook.view.widget.BudgetWidget;

public class ControllerFactory {

    private final ViewComponent viewComponent;

    private final Map<Class<?>, Supplier<View>> viewControllerMap = new HashMap<>();

    @Inject
    public ControllerFactory(ViewComponent viewComponent) {
        this.viewComponent = viewComponent;
        registerController();
    }

    private void registerController() {
        // Daten-/Steuerungsansichten
        viewControllerMap.put(AnnualOverviewView.class, viewComponent::getAnnualOverviewView);
        viewControllerMap.put(MonthlyOverview.class, viewComponent::getMonthlyOverview);
        viewControllerMap.put(BudgetWidget.class, viewComponent::getBudgetWidget);
        viewControllerMap.put(ImportView.class, viewComponent::getImportView);
        viewControllerMap.put(FixedTurnoverWizardView.class, viewComponent::getFixedTurnoverWizardView);
        viewControllerMap.put(AnalysisView.class, viewComponent::getAnalysisView);
        // MDV / Stammdaten
        viewControllerMap.put(FixedTurnoverManageView.class, viewComponent::getFixedTurnoverManageView);
        viewControllerMap.put(FixedTurnoverDetailView.class, viewComponent::getFixedTurnoverDetailView);
        viewControllerMap.put(UniqueExpensesManageView.class, viewComponent::getUniqueExpensesManageView);
        viewControllerMap.put(UniqueExpenseDetailView.class, viewComponent::getUniqueExpenseDetailView);
        viewControllerMap.put(CategoriesManageView.class, viewComponent::getManageCategoriesView);
        viewControllerMap.put(CategoryDetailView.class, viewComponent::getCategoryDetailView);
        viewControllerMap.put(CategoryGroupManageView.class, viewComponent::getCategoryGroupManageView);
        viewControllerMap.put(CategoryGroupDetailView.class, viewComponent::getCategoryGroupDetailView);
        // Technische Ansichten
        viewControllerMap.put(MigrationView.class, viewComponent::getMigrationView);
    }

    public View create(Class<?> viewControllerClass) {
        return viewControllerMap.get(viewControllerClass).get();
    }
}
