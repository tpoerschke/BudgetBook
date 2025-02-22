package timkodiert.budgetbook.view.widget;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import atlantafx.base.theme.Styles;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.i18n.LanguageManager;

import static timkodiert.budgetbook.view.FxmlResource.MONTH_YEAR_PICKER_WIDGET;

public class MonthYearPickerWidget implements Initializable {

    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private TextField yearTextField;
    @FXML
    private Label label;
    @FXML
    private HBox widgetInnerContainer;
    private final Button resetBtn = new Button("", new FontIcon(BootstrapIcons.TRASH));

    private final LanguageManager languageManager;
    private final String labelStr;
    private final boolean showResetBtn;
    private MonthYear value;

    @AssistedInject
    public MonthYearPickerWidget(LanguageManager languageManager,
                                 @Assisted Pane parent,
                                 @Assisted String labelStr,
                                 @Assisted MonthYear initialValue,
                                 @Assisted boolean showResetBtn) {
        this.languageManager = languageManager;
        this.value = initialValue;
        this.labelStr = labelStr;
        this.showResetBtn = showResetBtn;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MONTH_YEAR_PICKER_WIDGET.toString()));
            loader.setController(this);
            loader.setResources(languageManager.getResourceBundle());
            parent.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, languageManager.get("alert.widgetCouldNotBeOpened"));
            alert.showAndWait();
        }
    }

    public MonthYear getValue() {
        if (monthChoiceBox.getSelectionModel().isEmpty() || yearTextField.getText().isEmpty()) {
            return null;
        }
        return MonthYear.of(monthChoiceBox.getSelectionModel().getSelectedIndex() + 1, Integer.valueOf(yearTextField.getText()));
    }

    public void setValue(MonthYear value) {
        this.value = value;
        if (this.value != null) {
            monthChoiceBox.getSelectionModel().select(this.value.getMonth() - 1);
            yearTextField.setText("" + this.value.getYear());
        } else {
            yearTextField.setText("");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetBtn.setOnAction(event -> {
            monthChoiceBox.getSelectionModel().clearSelection();
            yearTextField.setText("");
        });

        UnaryOperator<Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };
        yearTextField.setPromptText("(leer)");
        yearTextField.setTextFormatter(new TextFormatter<>(integerFilter));

        label.setText(labelStr);

        monthChoiceBox.getItems().setAll(Arrays.asList(languageManager.getMonths()));

        if (this.value != null) {
            monthChoiceBox.getSelectionModel().select(this.value.getMonth() - 1);
            yearTextField.setText("" + this.value.getYear());
        } else {
            yearTextField.setText("");
        }

        // Styling setzen
        monthChoiceBox.getStyleClass().add(Styles.LEFT_PILL);
        if (showResetBtn) {
            resetBtn.getStyleClass().add(Styles.RIGHT_PILL);
            yearTextField.getStyleClass().add(Styles.CENTER_PILL);
            widgetInnerContainer.getChildren().addAll(resetBtn);
        } else {
            yearTextField.getStyleClass().add(Styles.RIGHT_PILL);
        }
    }
}
