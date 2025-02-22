package timkodiert.budgetbook.calc;

import java.util.function.ToDoubleFunction;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import timkodiert.budgetbook.domain.adapter.FixedTurnoverAdapter;
import timkodiert.budgetbook.domain.model.FixedTurnover;

public class SumListChangeListener<T extends FixedTurnoverAdapter> extends AbstractSumChangeListener<T> implements ListChangeListener<T> {

    protected final ObservableList<T> observableList;
    private final ToDoubleFunction<FixedTurnover> doubleMapper;

    public SumListChangeListener(ObservableList<T> observableList, DoubleProperty sumProp, StringProperty sumTextProp,
            ToDoubleFunction<FixedTurnover> doubleMapper) {
        super(sumProp, sumTextProp);
        this.observableList = observableList;
        this.doubleMapper = doubleMapper;
    }

    @Override
    public void onChanged(Change<? extends T> c) {
        double sum = observableList.stream().map(FixedTurnoverAdapter::getBean).mapToDouble(this.doubleMapper).sum();
        setProperties(sum);
    }
}
