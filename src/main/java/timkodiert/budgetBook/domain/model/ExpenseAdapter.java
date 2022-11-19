package timkodiert.budgetBook.domain.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.property.adapter.ReadOnlyJavaBeanDoublePropertyBuilder;
import lombok.Getter;

// Das ViewModel
public abstract class ExpenseAdapter {
    private final StringProperty position;
    private final ReadOnlyDoubleProperty value;
    private final ObjectProperty<ExpenseType> type;

    @Getter
    private final Expense bean;

    public ExpenseAdapter(Expense expense) throws NoSuchMethodException {
        this.position = JavaBeanStringPropertyBuilder.create().bean(expense).name("position").build();
        // ReadOnly reicht hier, da die Expense nicht über Tabellen o.ä. bearbeitet werden sollen
        this.value = ReadOnlyJavaBeanDoublePropertyBuilder.create().bean(expense).name("value").build();
        this.type = JavaBeanObjectPropertyBuilder.create().bean(expense).name("type").build();

        this.bean = expense;
    }

    public StringProperty positionProperty() {
        return this.position;
    }

    public ReadOnlyDoubleProperty valueProperty() {
        return this.value;
    }

    public ObjectProperty<ExpenseType> typeProperty() {
        return this.type;
    }
}
