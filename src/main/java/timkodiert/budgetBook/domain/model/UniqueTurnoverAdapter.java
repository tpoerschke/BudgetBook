package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.ReadOnlyJavaBeanStringPropertyBuilder;
import lombok.Getter;

import timkodiert.budgetBook.util.HasType;
import timkodiert.budgetBook.view.MonthlyOverview;

public class UniqueTurnoverAdapter implements Adapter<UniqueTurnover>, HasType<MonthlyOverview.RowType> {

    private final ReadOnlyStringProperty biller, note;
    private final ReadOnlyObjectProperty<LocalDate> date;

    @Getter
    private final UniqueTurnover bean;

    public UniqueTurnoverAdapter(UniqueTurnover expense) throws NoSuchMethodException {
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

    @Override
    public MonthlyOverview.RowType getType() {
        // TODO: Refactoring => Konzept entwickeln. RowTypes werden für die Stylings der TableCells verwendet.
        return MonthlyOverview.RowType.UNIQUE_EXPENSE;
    }
}
