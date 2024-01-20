package timkodiert.budgetBook.view;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.inject.Inject;

import timkodiert.budgetBook.view.fixed_turnover.FixedTurnoverDetailView;
import timkodiert.budgetBook.view.fixed_turnover.FixedTurnoverManageView;
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
        viewControllerMap.put(FixedTurnoverManageView.class, viewComponent::getFixedTurnoverManageView);
        viewControllerMap.put(FixedTurnoverDetailView.class, viewComponent::getFixedTurnoverDetailView);
        viewControllerMap.put(UniqueExpensesManageView.class, viewComponent::getUniqueExpensesManageView);
        viewControllerMap.put(UniqueExpenseDetailView.class, viewComponent::getUniqueExpenseDetailView);
    }

    public View create(Class<?> viewControllerClass) {
        return viewControllerMap.get(viewControllerClass).get();
    }
}
