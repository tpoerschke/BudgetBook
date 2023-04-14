package timkodiert.budgetBook.table.column;

import javafx.scene.control.TableColumn;

public class BoldTableColumn<S, T> extends TableColumn<S, T> {

    public BoldTableColumn() {
        super();
        initStyle();
    }

    public BoldTableColumn(String text) {
        super(text);
        initStyle();
    }

    private void initStyle() {
        getStyleClass().add("text-bold");
    }
}
