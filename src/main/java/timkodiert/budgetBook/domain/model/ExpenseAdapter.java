package timkodiert.budgetBook.domain.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanDoublePropertyBuilder;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

// Das ViewModel
public abstract class ExpenseAdapter {
    private final StringProperty position;
    private final DoubleProperty value;
    private final ObjectProperty<ExpenseType> type;

    public ExpenseAdapter(Expense expense) throws NoSuchMethodException {
        this.position = JavaBeanStringPropertyBuilder.create().bean(expense).name("position").build();
        this.value = JavaBeanDoublePropertyBuilder.create().bean(expense).name("value").build();
        this.type = JavaBeanObjectPropertyBuilder.create().bean(expense).name("type").build();
    }

    public StringProperty positionProperty() {
        return this.position;
    }

    public DoubleProperty valueProperty() {
        return this.value;
    }

    public ObjectProperty<ExpenseType> typeProperty() {
        return this.type;
    }
}
