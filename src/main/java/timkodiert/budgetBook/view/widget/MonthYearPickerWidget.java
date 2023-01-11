package timkodiert.budgetBook.view.widget;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.IntegerStringConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import timkodiert.budgetBook.domain.model.MonthYear;

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

    public MonthYearPickerWidget(Pane parent, String labelStr) {
        this(parent, labelStr, MonthYear.of(LocalDate.now().getMonthValue(), LocalDate.now().getYear()));
    }

    public MonthYearPickerWidget(Pane parent, String labelStr, MonthYear initialValue) {
        this.value = initialValue;
        this.labelStr = labelStr;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MonthYearPickerWidget.fxml"));
            loader.setController(this);
            parent.getChildren().add(loader.load());
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "Widget konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    public MonthYear getValue() {
        if(monthChoiceBox.getSelectionModel().isEmpty() || yearTextField.getText().isEmpty()) {
            return null;
        }
        return MonthYear.of(monthChoiceBox.getSelectionModel().getSelectedIndex() + 1, Integer.valueOf(yearTextField.getText()));
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
        monthChoiceBox.getStyleClass().add(Styles.LEFT_PILL);

        List<String> monthList = List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");
        monthChoiceBox.getItems().setAll(monthList);

        if(this.value != null) {
            monthChoiceBox.getSelectionModel().select(this.value.getMonth() - 1);
            yearTextField.setText("" + this.value.getYear());
            yearTextField.getStyleClass().add(Styles.RIGHT_PILL);
        }
        else {
            yearTextField.setText("");
            widgetInnerContainer.getChildren().addAll(resetBtn);
            resetBtn.getStyleClass().add(Styles.RIGHT_PILL);
            yearTextField.getStyleClass().add(Styles.CENTER_PILL);
        }
    }
}
