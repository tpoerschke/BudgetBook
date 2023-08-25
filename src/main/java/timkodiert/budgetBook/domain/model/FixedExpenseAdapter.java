package timkodiert.budgetBook.domain.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;
import lombok.Getter;

public class FixedExpenseAdapter implements Adapter<FixedExpense> {
    private final StringProperty position, note;
    private final ReadOnlyObjectProperty<PaymentType> paymentType;

    @Getter
    private final FixedExpense bean;

    public FixedExpenseAdapter(FixedExpense expense) throws NoSuchMethodException {
        this.position = JavaBeanStringPropertyBuilder.create().bean(expense).name("position").build();
        this.note = JavaBeanStringPropertyBuilder.create().bean(expense).name("note").build();

        this.paymentType = ReadOnlyJavaBeanObjectPropertyBuilder.<PaymentType>create().bean(expense).name("type")
                .build();

        this.bean = expense;
    }

    public StringProperty positionProperty() {
        return this.position;
    }

    public StringProperty noteProperty() {
        return this.note;
    }

    public ReadOnlyObjectProperty<PaymentType> paymentTypeProperty() {
        return this.paymentType;
    }
}
