package timkodiert.budgetbook.ui.control;

import java.time.YearMonth;

import dagger.assisted.AssistedFactory;
import javafx.scene.layout.Pane;

import static timkodiert.budgetbook.ui.control.MonthYearPickerWidget.ViewMode;

@AssistedFactory
public interface MonthYearPickerWidgetFactory {

    MonthYearPickerWidget create(Pane parent, String labelStr, YearMonth initialValue, boolean showResetBtn, ViewMode viewMode);

    default MonthYearPickerWidget create(Pane parent, String labelStr, YearMonth initialValue, boolean showResetBtn) {
        return create(parent, labelStr, initialValue, showResetBtn, ViewMode.DEFAULT);
    }
}
