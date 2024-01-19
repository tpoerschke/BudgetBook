package timkodiert.budgetBook.domain.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;
import lombok.Getter;

public class FixedTurnoverAdapter implements Adapter<FixedTurnover> {
    private final StringProperty position, note;
    private final ReadOnlyObjectProperty<PaymentType> paymentType;
    private final ReadOnlyObjectProperty<TurnoverDirection> direction;

    @Getter
    private final FixedTurnover bean;

    public FixedTurnoverAdapter(FixedTurnover expense) throws NoSuchMethodException {
        this.position = JavaBeanStringPropertyBuilder.create().bean(expense).name("position").build();
        this.note = JavaBeanStringPropertyBuilder.create().bean(expense).name("note").build();

        this.paymentType = ReadOnlyJavaBeanObjectPropertyBuilder.<PaymentType>create().bean(expense).name("type").build();
        this.direction = ReadOnlyJavaBeanObjectPropertyBuilder.<TurnoverDirection>create().bean(expense).name("direction").build();

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

    public ReadOnlyObjectProperty<TurnoverDirection> directionProperty() {
        return this.direction;
    }
}
