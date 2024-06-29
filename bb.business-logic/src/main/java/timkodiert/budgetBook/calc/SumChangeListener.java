package timkodiert.budgetBook.calc;

import java.util.Arrays;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SumChangeListener<T extends Number> extends AbstractSumChangeListener<T> implements ChangeListener<T> {

    private final List<DoubleProperty> propsToSum;
    
    public SumChangeListener(DoubleProperty sumProp, StringProperty sumTextProp, DoubleProperty... propsToSum) {
        super(sumProp, sumTextProp);
        this.propsToSum = Arrays.asList(propsToSum);
    }

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        double sum = this.propsToSum.stream().mapToDouble(DoubleProperty::get).sum();
        setProperties(sum);
    }
}
