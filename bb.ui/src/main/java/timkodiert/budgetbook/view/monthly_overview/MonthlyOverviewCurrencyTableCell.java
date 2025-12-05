package timkodiert.budgetbook.view.monthly_overview;

import java.util.Map;

import javafx.beans.property.BooleanProperty;

import timkodiert.budgetbook.domain.table.RowType;
import timkodiert.budgetbook.monthly_overview.TableRowData;
import timkodiert.budgetbook.table.cell.CurrencyTableCell;

public class MonthlyOverviewCurrencyTableCell extends CurrencyTableCell<TableRowData, Number> {

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
