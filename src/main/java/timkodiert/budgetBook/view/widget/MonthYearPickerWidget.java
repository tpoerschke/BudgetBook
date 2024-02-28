package timkodiert.budgetBook.view.widget;

import static timkodiert.budgetBook.view.FxmlResource.MONTH_YEAR_PICKER_WIDGET;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import atlantafx.base.theme.Styles;
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
import lombok.Builder;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetBook.domain.model.MonthYear;
import timkodiert.budgetBook.i18n.LanguageManager;

public class MonthYearPickerWidget implements Initializable {

    @FXML
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private TextField yearTextField;
    @FXML
    private Label label;
    @FXML
    private HBox widgetInnerContainer;

    private Button resetBtn = new Button("", new FontIcon(BootstrapIcons.TRASH));

    private MonthYear value;
    private String labelStr;
    private boolean showResetBtn = false;

    public MonthYearPickerWidget(Pane parent, String labelStr, MonthYear initialValue, boolean showResetBtn) {
        this.value = initialValue;
        this.labelStr = labelStr;
        this.showResetBtn = showResetBtn;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MONTH_YEAR_PICKER_WIDGET.toString()));
            loader.setController(this);
            loader.setResources(LanguageManager.getInstance().getResourceBundle());
            parent.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, LanguageManager.get("alert.widgetCouldNotBeOpened"));
            alert.showAndWait();
        }
    }

    @Builder(builderMethodName = "builder")
    public static MonthYearPickerWidget newWidget(Pane parent, String labelStr, MonthYear initialValue, boolean showResetBtn) {
        return new MonthYearPickerWidget(parent, labelStr, initialValue, showResetBtn);
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

        monthChoiceBox.getItems().setAll(Arrays.stream(LanguageManager.getInstance().getMonths()).toList());

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
