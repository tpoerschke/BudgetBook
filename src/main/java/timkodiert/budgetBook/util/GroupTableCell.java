package timkodiert.budgetBook.util;

import java.util.function.Consumer;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import timkodiert.budgetBook.view.MonthlyOverview.TableData;

public class GroupTableCell extends TableCell<TableData, TableData> {

    private final Consumer<Boolean> onHideClick;
    private boolean isCollapsed = false;
    private Button btn = new Button();

    public GroupTableCell(Consumer<Boolean> onHideClick) {
        this.onHideClick = onHideClick;
        btn.setGraphic(new FontIcon(BootstrapIcons.ARROWS_COLLAPSE));
        btn.setOnAction(event -> {
            isCollapsed = !isCollapsed;
            btn.setGraphic(isCollapsed ? new FontIcon(BootstrapIcons.ARROWS_EXPAND)
                    : new FontIcon(BootstrapIcons.ARROWS_COLLAPSE));
            onHideClick.accept(isCollapsed);
            this.setGraphic(btn);
        });
    }

    @Override
    protected void updateItem(TableData item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || !item.groupRow()) {
            this.setGraphic(null);
        } else {
            this.setGraphic(btn);
        }
    }
}
