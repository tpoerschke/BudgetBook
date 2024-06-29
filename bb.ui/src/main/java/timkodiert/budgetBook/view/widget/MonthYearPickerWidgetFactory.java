package timkodiert.budgetBook.view.widget;

import dagger.assisted.AssistedFactory;
import javafx.scene.layout.Pane;

import timkodiert.budgetBook.domain.model.MonthYear;

@AssistedFactory
public interface MonthYearPickerWidgetFactory {

    MonthYearPickerWidget create(Pane parent, String labelStr, MonthYear initialValue, boolean showResetBtn);
}
