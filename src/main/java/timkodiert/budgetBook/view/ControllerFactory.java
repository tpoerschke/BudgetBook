package timkodiert.budgetBook.view;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.inject.Inject;

import timkodiert.budgetBook.view.fixed_expense.FixedExpenseDetailView;
import timkodiert.budgetBook.view.fixed_expense.FixedExpensesManageView;
import timkodiert.budgetBook.view.unique_expense.UniqueExpenseDetailView;
import timkodiert.budgetBook.view.unique_expense.UniqueExpensesManageView;

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
        viewControllerMap.put(ImportView.class, viewComponent::getImportView);
        // MDV / Stammdaten
        viewControllerMap.put(FixedExpensesManageView.class, viewComponent::getFixedExpenseManageView);
        viewControllerMap.put(FixedExpenseDetailView.class, viewComponent::getEditExpenseView);
        viewControllerMap.put(UniqueExpensesManageView.class, viewComponent::getUniqueExpensesManageView);
        viewControllerMap.put(UniqueExpenseDetailView.class, viewComponent::getUniqueExpenseDetailView);
    }

    public View create(Class<?> viewControllerClass) {
        return viewControllerMap.get(viewControllerClass).get();
    }
}
