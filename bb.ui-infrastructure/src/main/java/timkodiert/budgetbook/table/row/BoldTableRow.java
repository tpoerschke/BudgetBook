package timkodiert.budgetbook.table.row;

import javafx.scene.control.TableRow;

import timkodiert.budgetbook.domain.util.HasType;

public class BoldTableRow<I extends HasType<T>, T> extends TableRow<I> {

    private static final String CSS_CLASS = "text-bold";

    // Die Rows dieses Typs werden fett dargestellt
    private final T type;

    public BoldTableRow(T type) {
        super();
        this.type = type;
    }

    @Override
    protected void updateItem(I item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && this.type.equals(item.getType())) {
            getStyleClass().add(CSS_CLASS);
        } else {
            getStyleClass().remove(CSS_CLASS);
        }
    }
}
