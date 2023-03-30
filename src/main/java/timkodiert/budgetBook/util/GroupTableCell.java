package timkodiert.budgetBook.util;

import java.util.function.Consumer;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import timkodiert.budgetBook.view.MonthlyOverview.TableData;

public class GroupTableCell extends TableCell<TableData, TableData> {

    private final SimpleBooleanProperty isCollapsedProperty;
    private final Button btn = new Button();

    public GroupTableCell(SimpleBooleanProperty isCollapsedProperty) {
        this.isCollapsedProperty = isCollapsedProperty;
        btn.setGraphic(new FontIcon(BootstrapIcons.ARROWS_COLLAPSE));
        btn.setOnAction(this::handleClick);
    }

    private void handleClick(ActionEvent event) {
        isCollapsedProperty.set(!isCollapsedProperty.get());
        btn.setGraphic(isCollapsedProperty.get()
                ? new FontIcon(BootstrapIcons.ARROWS_COLLAPSE)
                : new FontIcon(BootstrapIcons.ARROWS_EXPAND));
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
