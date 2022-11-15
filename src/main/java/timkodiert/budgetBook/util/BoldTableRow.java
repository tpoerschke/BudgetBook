package timkodiert.budgetBook.util;

import javafx.scene.control.TableRow;
import timkodiert.budgetBook.domain.model.ExpenseType;
import timkodiert.budgetBook.domain.model.FixedExpense;

public class BoldTableRow extends TableRow<FixedExpense> {

    // Alle Ausgaben dieses Typs werden fett dargestellt
    private ExpenseType expenseType;

    public BoldTableRow(ExpenseType expenseType) {
        super();
        this.expenseType = expenseType;
    }

    @Override
    protected void updateItem(FixedExpense item, boolean empty) {
        super.updateItem(item, empty);
        if(!empty && item.getType().equals(this.expenseType)) {
            getStyleClass().add("text-bold");
        }
    }
}
