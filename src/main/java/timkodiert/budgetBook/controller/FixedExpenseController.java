package timkodiert.budgetBook.controller;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Locale;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import lombok.Getter;
import timkodiert.budgetBook.Constants;
import timkodiert.budgetBook.domain.model.Expense;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;
import timkodiert.budgetBook.domain.model.ExpenseType;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.util.SumChangeListener;
import timkodiert.budgetBook.util.SumListChangeListener;

public class FixedExpenseController {

    @Getter
    private final ObservableList<FixedExpense> allExpenses;
    @Getter
    private final ObservableList<ExpenseAdapter> monthlyExpenses;
    @Getter
    private final ObservableList<ExpenseAdapter> currentMonthExpenses;
    @Getter
    private final ObservableList<ExpenseAdapter> nextMonthExpenses;

    private final DoubleProperty monthlyExpensesSum;
    private final StringProperty monthlyExpensesSumText;

    private final DoubleProperty nextMonthExpensesSum;
    private final StringProperty nextMonthExpensesSumText;

    private final DoubleProperty nextMonthExpensesTotalSum;
    private final StringProperty nextMonthExpensesTotalSumText;

    private EntityManager entityManager;

    public FixedExpenseController() {
        this.entityManager = EntityManager.getInstance();
        this.allExpenses = FXCollections.observableArrayList();
        this.monthlyExpenses = FXCollections.observableArrayList();
        this.currentMonthExpenses = FXCollections.observableArrayList();
        this.nextMonthExpenses = FXCollections.observableArrayList();

        allExpenses.addListener((Change<? extends FixedExpense> change) -> {
            int nextMonth = LocalDate.now().plusMonths(1).getMonth().getValue();
            nextMonthExpenses.setAll(allExpenses.stream()
                    .filter(expense -> !expense.getType().equals(ExpenseType.MONTHLY))
                    // TODO: Überarbeiten mit vernünftiger Schnittstelle
                    .filter(expense -> expense.getPayments().get(0).getMonthsOfPayment().contains(nextMonth))
                    .map(Expense::getAdapter)
                    .toList());
        });
        allExpenses.addListener((Change<? extends FixedExpense> change) -> {
            int currentMonth = LocalDate.now().getMonth().getValue();
            currentMonthExpenses.setAll(allExpenses.stream()
                    .filter(expense -> !expense.getType().equals(ExpenseType.MONTHLY))
                    // TODO: Überarbeiten mit vernünftiger Schnittstelle
                    .filter(expense -> expense.getPayments().get(0).getMonthsOfPayment().contains(currentMonth))
                    .map(Expense::getAdapter)
                    .toList());
        });
        allExpenses.addListener((Change<? extends FixedExpense> change) -> {
            monthlyExpenses.setAll(allExpenses.stream()
                    .filter(expense -> expense.getType().equals(ExpenseType.MONTHLY))
                    .map(Expense::getAdapter)
                    .toList());
        });

        // TODO: Ins View? oder eigene Klasse
        monthlyExpensesSum = new SimpleDoubleProperty();
        monthlyExpensesSumText = new SimpleStringProperty(Constants.INITIAL_AMOUNT_STRING);
        monthlyExpenses.addListener(new SumListChangeListener<>(monthlyExpenses, monthlyExpensesSum, monthlyExpensesSumText));
        nextMonthExpensesSum = new SimpleDoubleProperty();
        nextMonthExpensesSumText = new SimpleStringProperty(Constants.INITIAL_AMOUNT_STRING);
        nextMonthExpenses.addListener(new SumListChangeListener<>(nextMonthExpenses, nextMonthExpensesSum, nextMonthExpensesSumText));
        nextMonthExpensesTotalSum = new SimpleDoubleProperty();
        nextMonthExpensesTotalSumText = new SimpleStringProperty(Constants.INITIAL_AMOUNT_STRING);
        ChangeListener<Number> totalListener = new SumChangeListener<>(nextMonthExpensesTotalSum, nextMonthExpensesTotalSumText, nextMonthExpensesSum, monthlyExpensesSum);
        nextMonthExpensesSum.addListener(totalListener);
        monthlyExpensesSum.addListener(totalListener);
    }

    public void loadAll() {
        allExpenses.setAll(entityManager.findAll(FixedExpense.class));
    }

    public DoubleProperty monthlyExpensesSumProperty() {
        return this.monthlyExpensesSum;
    }

    public StringProperty monthlyExpensesSumTextProperty() {
        return this.monthlyExpensesSumText;
    }

    public DoubleProperty nextMonthExpensesSumProperty() {
        return this.nextMonthExpensesSum;
    }

    public StringProperty nextMonthExpensesSumTextProperty() {
        return this.nextMonthExpensesSumText;
    }

    public DoubleProperty nextMonthExpensesTotalSumProperty() {
        return this.nextMonthExpensesTotalSum;
    }

    public StringProperty nextMonthExpensesTotalSumTextProperty() {
        return this.nextMonthExpensesTotalSumText;
    }
}
