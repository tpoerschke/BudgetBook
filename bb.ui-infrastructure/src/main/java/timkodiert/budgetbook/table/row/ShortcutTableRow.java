package timkodiert.budgetbook.table.row;

import javax.inject.Inject;

import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;
import org.jspecify.annotations.Nullable;

import timkodiert.budgetbook.monthly_overview.TableRowData;
import timkodiert.budgetbook.representation.HasRowType;
import timkodiert.budgetbook.view.FxmlResource;
import timkodiert.budgetbook.view.ShortcutFunction;

public class ShortcutTableRow extends TableRow<TableRowData> {

    private final ShortcutFunction shortcutFunction;

    @Inject
    public ShortcutTableRow(ShortcutFunction shortcutFunction) {
        this.shortcutFunction = shortcutFunction;
        setOnMouseClicked(this::handleClick);
    }

    private void handleClick(MouseEvent event) {
        TableRowData data = getItem();
        if (data == null) {
            return;
        }
        FxmlResource targetFxmlResource = mapRowDataToFxmlResource(data);
        if (event.getClickCount() == 2 && !isEmpty() && targetFxmlResource != null) {
            shortcutFunction.openDetailView(targetFxmlResource, data.id());
        }
    }

    private @Nullable FxmlResource mapRowDataToFxmlResource(HasRowType tableData) {
        return switch (tableData.getRowType()) {
            case FIXED_EXPENSE -> FxmlResource.MANAGE_REGULAR_TURNOVER_VIEW;
            case UNIQUE_EXPENSE -> FxmlResource.MANAGE_UNIQUE_TURNOVER_VIEW;
            default -> null;
        };
    }
}
