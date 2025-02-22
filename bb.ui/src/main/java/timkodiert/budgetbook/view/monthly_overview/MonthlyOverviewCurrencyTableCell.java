package timkodiert.budgetbook.view.monthly_overview;

import java.util.Map;

import javafx.beans.property.BooleanProperty;

import timkodiert.budgetbook.table.RowType;
import timkodiert.budgetbook.table.cell.CurrencyTableCell;

public class MonthlyOverviewCurrencyTableCell extends CurrencyTableCell<TableData, Number, RowType> {

    private final Map<RowType, BooleanProperty> isCollapsedProperties;

    public MonthlyOverviewCurrencyTableCell(Map<RowType, BooleanProperty> isCollapsedProperties) {
        super();
        this.isCollapsedProperties = isCollapsedProperties;
    }

    @Override
    protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        RowType rowType = this.getTableRow().getItem() != null ? this.getTableRow().getItem().getType() : null;
        if (isCollapsedProperties.containsKey(rowType) && !isCollapsedProperties.get(rowType).get()) {
            setText("");
        }
    }
}
