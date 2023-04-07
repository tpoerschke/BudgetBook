package timkodiert.budgetBook.view;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import timkodiert.budgetBook.domain.model.MonthYear;

public class MonthFilter implements ObservableValue<MonthYear> {

    private ComboBox<String> selectedMonthBox;
    private ComboBox<Integer> selectedYearBox;
    private Button nextBtn, prevBtn;

    private List<ChangeListener<? super MonthYear>> listeners = new ArrayList<>();
    private MonthYear value;

    public MonthFilter(ComboBox<String> selectedMonthBox, ComboBox<Integer> selectedYearBox,
            Button nextBtn, Button prevBtn) {
        this.selectedMonthBox = selectedMonthBox;
        this.selectedYearBox = selectedYearBox;

        this.nextBtn = nextBtn;
        this.prevBtn = prevBtn;

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
        throw new UnsupportedOperationException("Unimplemented method 'addListener'");
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeListener'");
    }
}
