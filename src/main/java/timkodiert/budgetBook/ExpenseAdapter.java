package timkodiert.budgetBook;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanDoublePropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

// Das ViewModel
public class ExpenseAdapter {
    private final StringProperty position;
    private final DoubleProperty value;
    private final StringProperty type;

    public ExpenseAdapter(Expense expense) throws NoSuchMethodException {
        this.position = JavaBeanStringPropertyBuilder.create().bean(expense).name("position").build();
        this.value = JavaBeanDoublePropertyBuilder.create().bean(expense).name("value").build();
        this.type = JavaBeanStringPropertyBuilder.create().bean(expense).name("type").build();
    }

    public String getPosition() {
        return position.get();
    }

    public double getValue() {
        return value.get();
    }

    public String getType() {
        return type.get();
    }
}
