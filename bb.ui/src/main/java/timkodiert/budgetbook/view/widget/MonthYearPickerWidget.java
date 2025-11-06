package timkodiert.budgetbook.view.widget;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import atlantafx.base.theme.Styles;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetbook.dialog.StackTraceAlert;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.i18n.LanguageManager;

import static timkodiert.budgetbook.view.FxmlResource.MONTH_YEAR_PICKER_WIDGET;

public class MonthYearPickerWidget implements Initializable {

    public enum ViewMode {
        DEFAULT, INLINE
    }

    @FXML
    @Getter
    private ChoiceBox<String> monthChoiceBox;
    @FXML
    private TextField yearTextField;
    @FXML
    private VBox widgetContainer;
    @FXML
    private HBox widgetInnerContainer;
    private final Label label = new Label();
    private final Button resetBtn = new Button("", new FontIcon(BootstrapIcons.TRASH));

    private final LanguageManager languageManager;
    private final String labelStr;
    private final boolean showResetBtn;
    private final ViewMode viewMode;
    private final ObjectProperty<MonthYear> value = new SimpleObjectProperty<>();

    private boolean muteListener = false;

    @AssistedInject
    public MonthYearPickerWidget(LanguageManager languageManager,
                                 @Assisted Pane parent,
                                 @Assisted String labelStr,
                                 @Assisted MonthYear initialValue,
                                 @Assisted boolean showResetBtn,
                                 @Assisted ViewMode viewMode) {
        this.languageManager = languageManager;
        this.value.setValue(initialValue);
        this.labelStr = labelStr;
        this.showResetBtn = showResetBtn;
        this.viewMode = viewMode;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MONTH_YEAR_PICKER_WIDGET.toString()));
            loader.setController(this);
            loader.setResources(languageManager.getResourceBundle());
            parent.getChildren().add(loader.load());
        } catch (IOException e) {
            StackTraceAlert.createAndLog(languageManager.get("alert.widgetCouldNotBeOpened"), e).showAndWait();
        }
    }

    private void updateValue() {
        if (muteListener) {
            return;
        }
        if (monthChoiceBox.getSelectionModel().isEmpty() || StringUtils.isEmpty(yearTextField.getText())) {
            value.set(null);
        }
        // FIXME: Hier fliegen NFEs, besseres Handling einbauen. Dabei am MoneyTextField orientieren
        value.set(MonthYear.of(monthChoiceBox.getSelectionModel().getSelectedIndex() + 1, Integer.valueOf(yearTextField.getText())));
    }

    private void updateUi() {
        muteListener = true;
        if (this.value.get() != null) {
            monthChoiceBox.getSelectionModel().select(this.value.get().getMonth() - 1);
            yearTextField.setText("" + this.value.get().getYear());
        } else {
            monthChoiceBox.getSelectionModel().clearSelection();
            yearTextField.setText("");
        }
        muteListener = false;
    }

    public ObjectProperty<MonthYear> valueProperty() {
        return value;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resetBtn.setOnAction(event -> {
            monthChoiceBox.getSelectionModel().clearSelection();
            yearTextField.setText("");
        });

        UnaryOperator<Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([1-9]\\d*)?")) {
                return change;
            }
            return null;
        };
        yearTextField.setPromptText("(leer)");
        yearTextField.setTextFormatter(new TextFormatter<>(integerFilter));

        label.setText(labelStr);

        monthChoiceBox.getItems().setAll(Arrays.asList(languageManager.getMonths()));

        if (this.value.get() != null) {
            monthChoiceBox.getSelectionModel().select(this.value.get().getMonth() - 1);
            yearTextField.setText("" + this.value.get().getYear());
        } else {
            yearTextField.setText("");
        }

        valueProperty().addListener((observable, oldValue, newValue) -> updateUi());
        monthChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateValue());
        yearTextField.textProperty().addListener((observable, oldValue, newValue) -> updateValue());

        // Styling setzen
        if (viewMode == ViewMode.INLINE) {
            widgetInnerContainer.getChildren().addFirst(label);
            label.getStyleClass().add(Styles.LEFT_PILL);
            monthChoiceBox.getStyleClass().add(Styles.CENTER_PILL);
        } else {
            widgetContainer.getChildren().addFirst(label);
            monthChoiceBox.getStyleClass().add(Styles.LEFT_PILL);
        }

        if (showResetBtn) {
            resetBtn.getStyleClass().add(Styles.RIGHT_PILL);
            yearTextField.getStyleClass().add(Styles.CENTER_PILL);
            widgetInnerContainer.getChildren().addAll(resetBtn);
        } else {
            yearTextField.getStyleClass().add(Styles.RIGHT_PILL);
        }
    }
}
