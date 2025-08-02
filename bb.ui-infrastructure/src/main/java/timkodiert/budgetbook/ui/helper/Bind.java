package timkodiert.budgetbook.ui.helper;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ComboBox;

public class Bind {

    private Bind() {
    }

    public static <T> void comboBox(ComboBox<T> comboBox, ObjectProperty<T> property) {
        //        ReadOnlyObjectProperty<T> selectedItemProperty = comboBox.getSelectionModel().selectedItemProperty();
        //        property.bind(selectedItemProperty);
        //        property.addListener((obs, oldVal, newVal) -> comboBox.getSelectionModel().select(newVal));
        comboBox.valueProperty().bindBidirectional(property);
    }
}
