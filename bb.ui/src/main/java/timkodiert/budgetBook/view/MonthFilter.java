package timkodiert.budgetBook.view;

import java.util.ArrayList;
import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetBook.domain.model.MonthYear;
import timkodiert.budgetBook.i18n.LanguageManager;

public class MonthFilter implements ObservableValue<MonthYear> {

    private final LanguageManager languageManager;

    private final ComboBox<String> selectedMonthBox;
    private final ComboBox<Integer> selectedYearBox;
    private Button nextBtn, prevBtn;

    private List<ChangeListener<? super MonthYear>> listeners = new ArrayList<>();
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

        selectedMonthBox.getItems().setAll(languageManager.getMonths());
        selectedYearBox.getItems().setAll(List.of(2020, 2021, 2022, 2023, 2024));

        this.value = MonthYear.now();
        setSelection(value);

        selectedMonthBox.getSelectionModel().selectedIndexProperty().addListener(this::monthBoxListener);
        selectedYearBox.getSelectionModel().selectedItemProperty().addListener(this::yearBoxListener);

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
    }

    private void yearBoxListener(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        MonthYear oldMonthYear = value;
        value = MonthYear.of(value.getMonth(), newValue.intValue());
        this.listeners.forEach(l -> l.changed(this, oldMonthYear, value));
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

    // Hm? Was soll ich damit? :D
    @Override
    public void addListener(InvalidationListener listener) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(languageManager.get("alert.unimplementedMethod").formatted("addListener"));
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(languageManager.get("alert.unimplementedMethod").formatted("removeListener"));
    }
}
