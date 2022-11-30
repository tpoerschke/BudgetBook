package timkodiert.budgetBook.util;

import java.util.function.ToDoubleFunction;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import timkodiert.budgetBook.domain.model.Expense;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;

public class SumListChangeListener<T extends ExpenseAdapter> extends AbstractSumChangeListener<T> implements ListChangeListener<T> {

    protected final ObservableList<T> observableList;
    private ToDoubleFunction<? super Expense> doubleMapper;
    
    public SumListChangeListener(ObservableList<T> observableList, DoubleProperty sumProp, StringProperty sumTextProp, ToDoubleFunction<? super Expense> doubleMapper) {
        super(sumProp, sumTextProp);
        this.observableList = observableList;
        this.doubleMapper = doubleMapper;
    }

    @Override
    public void onChanged(Change<? extends T> c) {
        double sum = observableList.stream().map(expAdapter -> expAdapter.getBean()).mapToDouble(this.doubleMapper).sum();
        setProperties(sum);
    }
}
