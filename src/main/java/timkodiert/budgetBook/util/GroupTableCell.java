package timkodiert.budgetBook.util;

import java.util.function.Consumer;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import timkodiert.budgetBook.view.MonthlyOverview.RowType;
import timkodiert.budgetBook.view.MonthlyOverview.TableData;

public class GroupTableCell extends TableCell<TableData, TableData> {

    private final SimpleBooleanProperty isUniqueCollapsedProperty, isFixedCollapsedProperty;
    private final Button btn = new Button();

    public GroupTableCell(SimpleBooleanProperty isUniqueCollapsedProperty,
            SimpleBooleanProperty isFixedCollapsedProperty) {
        this.isUniqueCollapsedProperty = isUniqueCollapsedProperty;
        this.isFixedCollapsedProperty = isFixedCollapsedProperty;
        btn.setOnAction(this::handleClick);
    }

    private void handleClick(ActionEvent event) {
        TableData item = getItem();

        if (item.type() == RowType.UNIQUE_EXPENSE_GROUP) {
            isUniqueCollapsedProperty.set(!isUniqueCollapsedProperty.get());
            btn.setGraphic(isUniqueCollapsedProperty.get()
                    ? new FontIcon(BootstrapIcons.ARROWS_COLLAPSE)
                    : new FontIcon(BootstrapIcons.ARROWS_EXPAND));
        } else if (item.type() == RowType.FIXED_EXPENSE_GROUP) {
            isFixedCollapsedProperty.set(!isFixedCollapsedProperty.get());
            btn.setGraphic(isFixedCollapsedProperty.get()
                    ? new FontIcon(BootstrapIcons.ARROWS_COLLAPSE)
                    : new FontIcon(BootstrapIcons.ARROWS_EXPAND));
        }
    }

    @Override
    protected void updateItem(TableData item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && RowType.getGroupTypes().contains(item.type())) {
            this.setGraphic(btn);

            if (item.type() == RowType.UNIQUE_EXPENSE_GROUP) {
                btn.setGraphic(isUniqueCollapsedProperty.get()
                        ? new FontIcon(BootstrapIcons.ARROWS_EXPAND)
                        : new FontIcon(BootstrapIcons.ARROWS_COLLAPSE));
            } else if (item.type() == RowType.FIXED_EXPENSE_GROUP) {
                btn.setGraphic(isFixedCollapsedProperty.get()
                        ? new FontIcon(BootstrapIcons.ARROWS_EXPAND)
                        : new FontIcon(BootstrapIcons.ARROWS_COLLAPSE));
            }
        } else {
            this.setGraphic(null);
        }
    }
}
