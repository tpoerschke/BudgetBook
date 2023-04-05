package timkodiert.budgetBook.table.cell;

import java.util.Map;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import timkodiert.budgetBook.view.MonthlyOverview.RowType;
import timkodiert.budgetBook.view.MonthlyOverview.TableData;

public class GroupTableCell extends TableCell<TableData, TableData> {

    private final Map<RowType, BooleanProperty> dataGroupProperties;
    private final Button btn = new Button();

    public GroupTableCell(Map<RowType, BooleanProperty> dataGroupProperties) {
        this.dataGroupProperties = dataGroupProperties;
        btn.setOnAction(this::handleClick);
    }

    private void handleClick(ActionEvent event) {
        TableData item = getItem();

        BooleanProperty collapsedProperty = dataGroupProperties.get(item.type());
        collapsedProperty.set(!collapsedProperty.get());
        displayIcon(collapsedProperty.get());
    }

    private void displayIcon(boolean isCollapsed) {
        btn.setGraphic(isCollapsed
                ? new FontIcon(BootstrapIcons.ARROWS_EXPAND)
                : new FontIcon(BootstrapIcons.ARROWS_COLLAPSE));
    }

    @Override
    protected void updateItem(TableData item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && RowType.getGroupTypes().contains(item.type())) {
            this.setGraphic(btn);

            BooleanProperty isCollapsedProperty = dataGroupProperties.get(item.type());
            displayIcon(isCollapsedProperty.get());
        } else {
            this.setGraphic(null);
        }
    }
}
