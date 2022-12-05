package timkodiert.budgetBook.util;

import javafx.scene.control.TableRow;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.model.Expense;

public class BoldTableRow extends TableRow<Expense> {

    // Alle Ausgaben dieses Typs werden fett dargestellt
    private PaymentType expenseType;

    public BoldTableRow(PaymentType expenseType) {
        super();
        this.expenseType = expenseType;
    }

    @Override
    protected void updateItem(Expense item, boolean empty) {
        super.updateItem(item, empty);
        if(!empty && this.expenseType.equals(item.getPaymentType())) {
            getStyleClass().add("text-bold");
        }
    }
}
