package timkodiert.budgetbook.table.cell;

import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetbook.domain.table.RowType;
import timkodiert.budgetbook.table.BaseTableData;

public class GroupTableCell<S extends BaseTableData, T extends BaseTableData> extends TableCell<S, T> {

    private final Map<RowType, BooleanProperty> dataGroupProperties;
    private final Button btn = new Button();

    public GroupTableCell(Map<RowType, BooleanProperty> dataGroupProperties) {
        this.dataGroupProperties = dataGroupProperties;
        btn.setOnAction(this::handleClick);
    }

    private void handleClick(ActionEvent event) {
        T item = getItem();

        BooleanProperty collapsedProperty = dataGroupProperties.get(item.getType());
        collapsedProperty.set(!collapsedProperty.get());
        displayIcon(collapsedProperty.get());
    }

    private void displayIcon(boolean isCollapsed) {
        btn.setGraphic(isCollapsed
                ? new FontIcon(BootstrapIcons.ARROWS_EXPAND)
                : new FontIcon(BootstrapIcons.ARROWS_COLLAPSE));
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty && RowType.getGroupTypes().contains(item.getType())) {
            this.setGraphic(btn);

            BooleanProperty isCollapsedProperty = dataGroupProperties.get(item.getType());
            displayIcon(isCollapsedProperty.get());
        } else {
            this.setGraphic(null);
        }
    }
}
