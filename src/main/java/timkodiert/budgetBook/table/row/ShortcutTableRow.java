package timkodiert.budgetBook.table.row;

import javax.inject.Inject;

import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetBook.table.monthlyOverview.TableData;
import timkodiert.budgetBook.view.FxmlResource;
import timkodiert.budgetBook.view.ShortcutFunction;

public class ShortcutTableRow extends TableRow<TableData> {

    private final ShortcutFunction shortcutFunction;

    @Inject
    public ShortcutTableRow(ShortcutFunction shortcutFunction) {
        this.shortcutFunction = shortcutFunction;
        setOnMouseClicked(this::handleClick);
    }

    private void handleClick(MouseEvent event) {
        TableData data = getItem();
        if (data == null) {
            return;
        }
        FxmlResource targetFxmlResource = mapRowDataToFxmlResource(data);
        if (event.getClickCount() == 2 && !isEmpty() && targetFxmlResource != null) {
            shortcutFunction.openDetailView(targetFxmlResource, data.id());
        }
    }

    private @Nullable FxmlResource mapRowDataToFxmlResource(TableData tableData) {
        return switch (tableData.type()) {
            case FIXED_EXPENSE -> FxmlResource.MANAGE_REGULAR_TURNOVER_VIEW;
            case UNIQUE_EXPENSE -> FxmlResource.MANAGE_UNIQUE_TURNOVER_VIEW;
            default -> null;
        };
    }
}
