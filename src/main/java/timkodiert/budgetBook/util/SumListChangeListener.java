package timkodiert.budgetBook.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;

public class SumListChangeListener<T extends ExpenseAdapter> extends AbstractSumChangeListener<T> implements ListChangeListener<T> {

    protected final ObservableList<T> observableList;

    public SumListChangeListener(ObservableList<T> observableList, DoubleProperty sumProp, StringProperty sumTextProp) {
        super(sumProp, sumTextProp);
        this.observableList = observableList;
    }

    @Override
    public void onChanged(Change<? extends T> c) {
        double sum = observableList.stream().map(expAdapter -> expAdapter.getBean()).mapToDouble(expense -> expense.getNextMonthValue()).sum();
        setProperties(sum);
    }
}
