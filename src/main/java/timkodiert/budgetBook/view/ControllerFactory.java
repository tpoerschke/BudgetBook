package timkodiert.budgetBook.view;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.inject.Inject;

import timkodiert.budgetBook.view.fixedExpenses.FixedExpensesManageView;
import timkodiert.budgetBook.view.uniqueExpenses.UniqueExpensesManageView;

public class ControllerFactory {

    private final ViewComponent viewComponent;

    private final Map<Class<?>, Supplier<View>> viewControllerMap = new HashMap<>();

    @Inject
    public ControllerFactory(ViewComponent viewComponent) {
        this.viewComponent = viewComponent;
        registerController();
    }

    private void registerController() {
        viewControllerMap.put(AnnualOverviewView.class, viewComponent::getAnnualOverviewView);
        viewControllerMap.put(MonthlyOverview.class, viewComponent::getMonthlyOverview);
        viewControllerMap.put(FixedExpensesManageView.class, viewComponent::getFixedExpenseManageView);
        viewControllerMap.put(UniqueExpensesManageView.class, viewComponent::getUniqueExpensesManageView);
        viewControllerMap.put(ImportView.class, viewComponent::getImportView);
    }

    public View create(Class<?> viewControllerClass) {
        return viewControllerMap.get(viewControllerClass).get();
    }
}
