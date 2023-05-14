package timkodiert.budgetBook.table.row;

import javafx.scene.control.TableRow;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.model.FixedExpense;

public class BoldTableRow extends TableRow<FixedExpense> {

    // Alle Ausgaben dieses Typs werden fett dargestellt
    private PaymentType expenseType;

    public BoldTableRow(PaymentType expenseType) {
        super();
        this.expenseType = expenseType;
    }

    @Override
    protected void updateItem(FixedExpense item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && this.expenseType.equals(item.getPaymentType())) {
            getStyleClass().add("text-bold");
        } else {
            getStyleClass().remove("text-bold");
        }
    }
}
