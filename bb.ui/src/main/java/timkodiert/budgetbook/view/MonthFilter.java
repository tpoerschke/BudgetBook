package timkodiert.budgetbook.view;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.i18n.LanguageManager;

public class MonthFilter implements ObservableValue<MonthYear> {

    private final LanguageManager languageManager;

    private final ComboBox<String> selectedMonthBox;
    private final ComboBox<Integer> selectedYearBox;
    private Button nextBtn, prevBtn;

    private final Set<ChangeListener<? super MonthYear>> listeners = new HashSet<>();
    private final Set<InvalidationListener> invalidationListeners = new HashSet<>();
    private MonthYear value;

    @AssistedInject
    public MonthFilter(LanguageManager languageManager,
                       @Assisted("selectedMonthBox") ComboBox<String> selectedMonthBox,
                       @Assisted("selectedYearBox") ComboBox<Integer> selectedYearBox,
                       @Assisted("nextBtn") Button nextBtn,
                       @Assisted("prevBtn") Button prevBtn) {
        this.languageManager = languageManager;
        this.selectedMonthBox = selectedMonthBox;
        this.selectedYearBox = selectedYearBox;

        this.nextBtn = nextBtn;
        this.prevBtn = prevBtn;

        nextBtn.setGraphic(new FontIcon(BootstrapIcons.CHEVRON_RIGHT));
        nextBtn.setText("");
        prevBtn.setGraphic(new FontIcon(BootstrapIcons.CHEVRON_LEFT));
        prevBtn.setText("");

        this.value = MonthYear.now();
        selectedMonthBox.getItems().setAll(languageManager.getMonths());
        selectedYearBox.getItems().setAll(IntStream.rangeClosed(value.getYear() - 5, value.getYear() + 5).boxed().toList());
        selectedMonthBox.getSelectionModel().selectedIndexProperty().addListener(this::monthBoxListener);
        selectedYearBox.getSelectionModel().selectedItemProperty().addListener(this::yearBoxListener);
        setSelection(value);

        nextBtn.setOnAction(event -> {
            setSelection(value.plusMonths(1));
        });
        prevBtn.setOnAction(event -> {
            setSelection(value.plusMonths(-1));
        });
    }

    private void monthBoxListener(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        MonthYear oldMonthYear = value;
        value = MonthYear.of(newValue.intValue() + 1, value.getYear());
        this.listeners.forEach(l -> l.changed(this, oldMonthYear, value));
        this.invalidationListeners.forEach(l -> l.invalidated(this));
    }

    private void yearBoxListener(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        MonthYear oldMonthYear = value;
        value = MonthYear.of(value.getMonth(), newValue.intValue());
        this.listeners.forEach(l -> l.changed(this, oldMonthYear, value));
        this.invalidationListeners.forEach(l -> l.invalidated(this));
    }

    private void setSelection(MonthYear monthYear) {
        selectedMonthBox.getSelectionModel().select(monthYear.getMonth() - 1);
        selectedYearBox.getSelectionModel().select(Integer.valueOf(monthYear.getYear()));
    }

    @Override
    public void addListener(ChangeListener<? super MonthYear> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChangeListener<? super MonthYear> listener) {
        listeners.remove(listener);
    }

    @Override
    public MonthYear getValue() {
        return value;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListeners.remove(listener);
    }
}
