package timkodiert.budgetBook.domain.model;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.property.adapter.ReadOnlyJavaBeanDoublePropertyBuilder;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;
import lombok.Getter;

// Das ViewModel
public abstract class ExpenseAdapter {
    private final StringProperty position, note;
    private final ReadOnlyDoubleProperty currentMonthValue;
    private final ReadOnlyDoubleProperty nextMonthValue;
    private final ReadOnlyObjectProperty<PaymentType> paymentType;

    @Getter
    private final Expense bean;

    public ExpenseAdapter(Expense expense) throws NoSuchMethodException {
        this.position = JavaBeanStringPropertyBuilder.create().bean(expense).name("position").build();
        this.note = JavaBeanStringPropertyBuilder.create().bean(expense).name("note").build();
        // ReadOnly reicht hier, da die Expense nicht über Tabellen o.ä. bearbeitet werden sollen
        this.currentMonthValue = ReadOnlyJavaBeanDoublePropertyBuilder.create().bean(expense).name("currentMonthValue").build();
        this.nextMonthValue = ReadOnlyJavaBeanDoublePropertyBuilder.create().bean(expense).name("nextMonthValue").build();
        this.paymentType = ReadOnlyJavaBeanObjectPropertyBuilder.<PaymentType>create().bean(expense).name("paymentType").build();

        this.bean = expense;
    }

    public StringProperty positionProperty() {
        return this.position;
    }

    public StringProperty noteProperty() {
        return this.note;
    }

    public ReadOnlyDoubleProperty currentMonthValueProperty() {
        return this.currentMonthValue;
    }

    public ReadOnlyDoubleProperty nextMonthValueProperty() {
        return this.nextMonthValue;
    }

    public ReadOnlyObjectProperty<PaymentType> paymentTypeProperty() {
        return this.paymentType;
    }
}
