package timkodiert.budgetBook.table.cell;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.table.monthlyOverview.TableData;

public class IconTableCell extends TableCell<TableData, TableData> {

    private final HBox box = new HBox(5);
    private final Tooltip tooltip = new Tooltip(LanguageManager.get("icon.turnoverImported"));

    public IconTableCell() {
        box.setFillHeight(true);
        box.setAlignment(Pos.CENTER);
    }

    @Override
    protected void updateItem(TableData item, boolean empty) {
        if(empty || item == null) {
            return;
        }

        box.getChildren().clear();
        if(item.hasImport()) {
            FontIcon importedIcon = new FontIcon(BootstrapIcons.FILE_EARMARK_ARROW_DOWN);
            Tooltip.install(box, tooltip); // Kann nicht direkt auf dem Icon installiert werden: https://github.com/kordamp/ikonli/issues/143
            box.getChildren().add(importedIcon);
        }

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setGraphic(box);
    }
}
