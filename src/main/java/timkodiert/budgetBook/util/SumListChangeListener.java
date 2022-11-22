package timkodiert.budgetBook.util;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;

@AllArgsConstructor
public class SumListChangeListener<T extends ExpenseAdapter> implements ListChangeListener<T> {

    private final ObservableList<T> observableList;
    private final DoubleProperty sumProp;
    private final StringProperty sumTextProp;

    @Override
    public void onChanged(Change<? extends T> c) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMAN);
        format.setCurrency(Currency.getInstance("EUR"));
        double sum = observableList.stream().map(expAdapter -> expAdapter.getBean()).mapToDouble(expense -> expense.getNextMonthValue()).sum();
        sumProp.set(sum);
        sumTextProp.set(format.format(sum));
    }
}
