package timkodiert.budgetbook.view.widget;

import dagger.assisted.AssistedFactory;
import javafx.scene.layout.Pane;

import timkodiert.budgetbook.domain.model.MonthYear;

import static timkodiert.budgetbook.view.widget.MonthYearPickerWidget.ViewMode;

@AssistedFactory
public interface MonthYearPickerWidgetFactory {

    MonthYearPickerWidget create(Pane parent, String labelStr, MonthYear initialValue, boolean showResetBtn, ViewMode viewMode);

    default MonthYearPickerWidget create(Pane parent, String labelStr, MonthYear initialValue, boolean showResetBtn) {
        return create(parent, labelStr, initialValue, showResetBtn, ViewMode.DEFAULT);
    }
}
