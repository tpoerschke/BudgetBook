package timkodiert.budgetBook.table.row;

import javafx.scene.control.TableRow;
import timkodiert.budgetBook.domain.model.FixedTurnover;
import timkodiert.budgetBook.domain.model.PaymentType;

public class BoldTableRow extends TableRow<FixedTurnover> {

    // Alle Ausgaben dieses Typs werden fett dargestellt
    private PaymentType expenseType;

    public BoldTableRow(PaymentType expenseType) {
        super();
        this.expenseType = expenseType;
    }

    @Override
    protected void updateItem(FixedTurnover item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && this.expenseType.equals(item.getType())) {
            getStyleClass().add("text-bold");
        } else {
            getStyleClass().remove("text-bold");
        }
    }
}
