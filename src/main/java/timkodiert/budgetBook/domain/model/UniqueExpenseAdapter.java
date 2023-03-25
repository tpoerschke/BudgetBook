package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.ReadOnlyJavaBeanStringPropertyBuilder;
import lombok.Getter;

public class UniqueExpenseAdapter {
    private final ReadOnlyStringProperty biller, note;
    private final ReadOnlyObjectProperty<LocalDate> date;

    @Getter
    private final UniqueExpense bean;

    public UniqueExpenseAdapter(UniqueExpense expense) throws NoSuchMethodException {
        this.biller = ReadOnlyJavaBeanStringPropertyBuilder.create().bean(expense).name("biller").build();
        this.note = ReadOnlyJavaBeanStringPropertyBuilder.create().bean(expense).name("note").build();
        this.date = ReadOnlyJavaBeanObjectPropertyBuilder.<LocalDate>create().bean(expense).name("date").build();

        this.bean = expense;
    }

    public ReadOnlyStringProperty billerProperty() {
        return this.biller;
    }

    public ReadOnlyStringProperty noteProperty() {
        return this.note;
    }

    public ReadOnlyObjectProperty<LocalDate> dateProperty() {
        return this.date;
    }
}
