package timkodiert.budgetBook.table.row;

import javax.inject.Inject;

import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetBook.table.BaseTableData;
import timkodiert.budgetBook.table.RowType;
import timkodiert.budgetBook.util.HasType;
import timkodiert.budgetBook.view.FxmlResource;
import timkodiert.budgetBook.view.ShortcutFunction;

public class ShortcutTableRow<T extends BaseTableData> extends TableRow<T> {

    private final ShortcutFunction shortcutFunction;

    @Inject
    public ShortcutTableRow(ShortcutFunction shortcutFunction) {
        this.shortcutFunction = shortcutFunction;
        setOnMouseClicked(this::handleClick);
    }

    private void handleClick(MouseEvent event) {
        BaseTableData data = getItem();
        if (data == null) {
            return;
        }
        FxmlResource targetFxmlResource = mapRowDataToFxmlResource(data);
        if (event.getClickCount() == 2 && !isEmpty() && targetFxmlResource != null) {
            shortcutFunction.openDetailView(targetFxmlResource, data.getId());
        }
    }

    private @Nullable FxmlResource mapRowDataToFxmlResource(HasType<RowType> tableData) {
        return switch (tableData.getType()) {
            case FIXED_EXPENSE -> FxmlResource.MANAGE_REGULAR_TURNOVER_VIEW;
            case UNIQUE_EXPENSE -> FxmlResource.MANAGE_UNIQUE_TURNOVER_VIEW;
            default -> null;
        };
    }
}
