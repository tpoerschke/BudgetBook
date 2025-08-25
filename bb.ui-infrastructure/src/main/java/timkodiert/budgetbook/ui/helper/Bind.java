package timkodiert.budgetbook.ui.helper;

import java.util.Collection;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ComboBox;

import timkodiert.budgetbook.converter.Converters;

public class Bind {

    private Bind() {
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
}
