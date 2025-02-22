package timkodiert.budgetbook.ui.control;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.Nullable;

public class MoneyTextField extends InputGroup implements Initializable {

    private static final String RESOURCE_LOCATION = "/fxml/MoneyTextField.fxml";

    private final DecimalFormat format = new DecimalFormat("0.00");
    private final MoneyTextFieldController controller = new MoneyTextFieldController();

    @FXML
    private TextField textField;


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
        controller.stringValueProperty().addListener((observable, oldVal, newVal) -> {
            textField.pseudoClassStateChanged(Styles.STATE_DANGER, !controller.isStringFormatValid());
        });
    }

    public void setValue(@Nullable Double value) {
        controller.setValue(value);
    }

    public @Nullable Double getValue() {
        return controller.getValue();
    }
}
