package timkodiert.budgetBook.table.cell;

import java.net.URL;
import java.util.Objects;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.text.Font;

import timkodiert.budgetBook.util.DoubleCurrencyStringConverter;
import timkodiert.budgetBook.util.HasType;
import timkodiert.budgetBook.view.MonthlyOverview;

import static timkodiert.budgetBook.util.ObjectUtils.nvl;

public class CurrencyTableCell<S extends HasType<U>, T extends Number, U> extends TableCell<S, T> {

    private DoubleCurrencyStringConverter converter = new DoubleCurrencyStringConverter();

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        HasType<U> hasRowType = nvl(getTableRow(), TableRow::getItem);
        if (!empty && hasRowType != null) {
            if (Objects.equals(hasRowType.getType(), MonthlyOverview.RowType.TOTAL_SUM)) {
                URL fontUrl = getClass().getResource("/css/RobotoMono-Bold.ttf");
                setFont(Font.loadFont(fontUrl.toExternalForm(), 14));
            } else {
                URL fontUrl = getClass().getResource("/css/RobotoMono-Regular.ttf");
                setFont(Font.loadFont(fontUrl.toExternalForm(), 14));
            }
        }

        if (empty) {
            setText("");
        } else {
            setText(item.doubleValue() == 0 ? "-" : converter.format(item.doubleValue()));
        }
    }

}
