package timkodiert.budgetBook.view;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import lombok.Getter;
import timkodiert.budgetBook.domain.model.Expense;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;
import timkodiert.budgetBook.domain.model.ExpenseType;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.util.EntityManager;

public class FixedExpenseController {

    private final ObservableList<FixedExpense> allExpenses;
    @Getter
    private final ObservableList<ExpenseAdapter> monthlyExpenses;
    @Getter
    private final ObservableList<ExpenseAdapter> nextMonthExpenses;

    private EntityManager entityManager;

    public FixedExpenseController() {
        this.entityManager = EntityManager.getInstance();
        this.allExpenses = FXCollections.observableArrayList();
        this.monthlyExpenses = FXCollections.observableArrayList();
        this.nextMonthExpenses = FXCollections.observableArrayList();

        allExpenses.addListener((Change<? extends FixedExpense> change) -> {
            int nextMonth = LocalDate.now().plusMonths(1).getMonth().getValue();
            nextMonthExpenses.setAll(allExpenses.stream()
                    .filter(expense -> !expense.getType().equals(ExpenseType.MONTHLY))
                    .filter(expense -> expense.getDatesOfPayment().contains(nextMonth))
                    .map(Expense::getAdapter)
                    .toList());
        });
        allExpenses.addListener((Change<? extends FixedExpense> change) -> {
            monthlyExpenses.setAll(allExpenses.stream()
                    .filter(expense -> expense.getType().equals(ExpenseType.MONTHLY))
                    .map(Expense::getAdapter)
                    .toList());
        });
    }

    public void loadAll() {
        allExpenses.setAll(entityManager.findAll(FixedExpense.class));
    }
}
