package timkodiert.budgetbook.ui.helper;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;

import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.table.cell.EditableCheckBoxCell;
import timkodiert.budgetbook.table.cell.TextFieldTableCell;
import timkodiert.budgetbook.view.mdv_base.BeanAdapter;

public class Bind {

    private final BeanAdapter<?> beanAdapter;

    public Bind(BeanAdapter<?> beanAdapter) {
        this.beanAdapter = beanAdapter;
    }

    public static <T> void comboBox(ComboBox<T> comboBox, ObjectProperty<T> property) {
        //        ReadOnlyObjectProperty<T> selectedItemProperty = comboBox.getSelectionModel().selectedItemProperty();
        //        property.bind(selectedItemProperty);
        //        property.addListener((obs, oldVal, newVal) -> comboBox.getSelectionModel().select(newVal));
        comboBox.valueProperty().bindBidirectional(property);
    }

    public static <T> void comboBox(ComboBox<T> comboBox, ObjectProperty<T> property, Collection<T> items, Class<T> type) {
        comboBox.setConverter(Converters.get(type));
        comboBox.getItems().setAll(items);
        comboBox.valueProperty().bindBidirectional(property);
    }

    public <T> void editableTableColumn(TableColumn<T, String> col, Function<T, String> valueGetter, BiConsumer<T, String> valueSetter) {
        col.setCellValueFactory(cellData -> new SimpleStringProperty(valueGetter.apply(cellData.getValue())));
        col.setCellFactory(c -> new TextFieldTableCell<>(editHandler(valueSetter)));
    }

    private <T> BiConsumer<String, T> editHandler(BiConsumer<T, String> valueSetter) {
        return (value, rowItem) -> {
            valueSetter.accept(rowItem, value);
            beanAdapter.setDirty(true);
        };
    }

    public <T> void editableTableColumn(TableColumn<T, Boolean> col, Predicate<T> valueGetter, BiConsumer<T, Boolean> valueSetter) {
        col.setCellValueFactory(cellData -> new SimpleBooleanProperty(valueGetter.test(cellData.getValue())));
        col.setCellFactory(c -> new EditableCheckBoxCell<>(checkHandler(valueSetter)));
    }

    private <T> BiConsumer<Boolean, T> checkHandler(BiConsumer<T, Boolean> valueSetter) {
        return (value, rowItem) -> {
            valueSetter.accept(rowItem, value);
            beanAdapter.setDirty(true);
        };
    }
}
