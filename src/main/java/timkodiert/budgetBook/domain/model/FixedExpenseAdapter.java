package timkodiert.budgetBook.domain.model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

public class FixedExpenseAdapter extends ExpenseAdapter {

    private final StringProperty type;

    public FixedExpenseAdapter(FixedExpense expense) throws NoSuchMethodException {
        super(expense);
        this.type = JavaBeanStringPropertyBuilder.create().bean(expense).name("type").build();
    }
    
    public StringProperty typeProperty() {
        return this.type;
    }
}
