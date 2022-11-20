package timkodiert.budgetBook.controller;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Locale;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    @Getter
    private final ObservableList<FixedExpense> allExpenses;
    @Getter
    private final ObservableList<ExpenseAdapter> monthlyExpenses;
    @Getter
    private final ObservableList<ExpenseAdapter> nextMonthExpenses;

    private final StringProperty monthlyExpensesSum;

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
                    //.filter(expense -> expense.getPayments().keySet().contains(nextMonth))
                    .map(Expense::getAdapter)
                    .toList());
        });
        allExpenses.addListener((Change<? extends FixedExpense> change) -> {
            monthlyExpenses.setAll(allExpenses.stream()
                    .filter(expense -> expense.getType().equals(ExpenseType.MONTHLY))
                    .map(Expense::getAdapter)
                    .toList());
        });

        monthlyExpensesSum = new SimpleStringProperty();
        monthlyExpenses.addListener((Change<? extends ExpenseAdapter> change) -> {
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMAN);
            format.setCurrency(Currency.getInstance("EUR"));
            double sum = monthlyExpenses.stream().map(expAdapter -> expAdapter.getBean()).mapToDouble(expense -> expense.getCurrentMonthValue()).sum();
            monthlyExpensesSum.set(format.format(sum));
        });
    }

    public void loadAll() {
        allExpenses.setAll(entityManager.findAll(FixedExpense.class));
    }

    public StringProperty monthlyExpensesSumProperty() {
        return this.monthlyExpensesSum;
    }
}
