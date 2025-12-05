package timkodiert.budgetbook.table.cell;

import java.net.URL;
import java.util.Optional;
import java.util.Set;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.text.Font;
import org.jspecify.annotations.Nullable;

import timkodiert.budgetbook.converter.BbCurrencyStringConverter;
import timkodiert.budgetbook.domain.model.PaymentType;
import timkodiert.budgetbook.domain.table.RowType;
import timkodiert.budgetbook.domain.util.HasType;
import timkodiert.budgetbook.table.cell.style.CellStyle;

public class CurrencyTableCell<S extends HasType<RowType>, T extends Number> extends TableCell<S, T> {

    private static final String STYLE_CLASS = "value-col";

    private final BbCurrencyStringConverter converter = new BbCurrencyStringConverter();

    private final boolean forceBold;
    private final @Nullable CellStyle cellStyle;

    public CurrencyTableCell() {
        this(false);
    }

    public CurrencyTableCell(boolean forceBold) {
        this.forceBold = forceBold;
        this.cellStyle = null;
    }

    public CurrencyTableCell(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
        this.forceBold = false;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        RowType rowType = Optional.ofNullable(getTableRow()).map(TableRow::getItem).map(HasType::getType).orElse(RowType.EMPTY);

        getStyleClass().remove(STYLE_CLASS);
        getStyleClass().add(STYLE_CLASS);
        if (cellStyle != null) {
            cellStyle.reset(this);
            cellStyle.apply(this, rowType);
        }

        if (!empty && rowType != RowType.EMPTY) {
            if (shouldBeBold(rowType)) {
                URL fontUrl = getClass().getResource("/css/RobotoMono-Bold.ttf");
                setFont(Font.loadFont(fontUrl.toExternalForm(), 14));
            } else {
                URL fontUrl = getClass().getResource("/css/RobotoMono-Regular.ttf");
                setFont(Font.loadFont(fontUrl.toExternalForm(), 14));
            }
        }

        if (empty || rowType == RowType.CATEGORY_GROUP) {
            setText("");
        } else {
            setText(item.intValue() == 0 ? "-" : converter.toString(item.intValue()));
        }
    }

    private boolean shouldBeBold(RowType rowType) {
        return forceBold || Set.of(RowType.TOTAL_SUM, PaymentType.CUMULATIVE).contains(rowType);
    }

}
