package timkodiert.budgetbook.ui.control;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

import atlantafx.base.layout.InputGroup;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class MoneyTextField extends InputGroup implements Initializable {

    private static final String RESOURCE_LOCATION = "/fxml/MoneyTextField.fxml";

    private final DecimalFormat format = new DecimalFormat("0.00");
    private final MoneyTextFieldController controller = new MoneyTextFieldController();

    @Getter
    @FXML
    private TextField textField;

    @Getter
    private boolean nullable = false;

    public MoneyTextField() {
        format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.GERMAN));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(RESOURCE_LOCATION));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textField.textProperty().bindBidirectional(controller.stringValueProperty());
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (Boolean.FALSE.equals(isNowFocused) && StringUtils.isEmpty(textField.getText()) && !nullable) {
                controller.stringValueProperty().setValue("0,00");
            }
        });
    }

    public void setValue(@Nullable Double value) {
        controller.setValue(value);
    }

    public @Nullable Double getValue() {
        return controller.getValue();
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
        this.controller.setNullable(nullable);
    }

    public ObjectProperty<Integer> integerValueProperty() {
        return controller.integerValueProperty();
    }

    public boolean isStringFormatValid() {
        return controller.isStringFormatValid();
    }
}
