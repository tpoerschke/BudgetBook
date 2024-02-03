package timkodiert.budgetBook.table.monthlyOverview;

import java.util.Map;

import javafx.beans.property.BooleanProperty;

import timkodiert.budgetBook.table.cell.CurrencyTableCell;
import timkodiert.budgetBook.view.MonthlyOverview.RowType;

public class MonthlyOverviewCurrencyTableCell extends CurrencyTableCell<TableData, Number, RowType> {

    private final Map<RowType, BooleanProperty> isCollapsedProperties;

    public MonthlyOverviewCurrencyTableCell(Map<RowType, BooleanProperty> isCollapsedProperties) {
        super();
        this.isCollapsedProperties = isCollapsedProperties;
    }

    @Override
    protected void updateItem(Number item, boolean empty) {
        super.updateItem(item, empty);
        RowType rowType = this.getTableRow().getItem() != null ? this.getTableRow().getItem().type() : null;
        if (isCollapsedProperties.containsKey(rowType) && !isCollapsedProperties.get(rowType).get()) {
            setText("");
        }
    }
}
