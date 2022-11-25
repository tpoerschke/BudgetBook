package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.IntStream;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.util.EntityManager;

public class NewExpenseView implements Initializable {

    @FXML
    private TextField positionTextField, valueTextField;

    @FXML
    private TextArea noteTextField;

    @FXML
    private ChoiceBox<String> typeChoiceBox, month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox;

    @FXML
    private Label month1Label, month2Label, month3Label, month4Label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // ValueTextField initialisieren
        //valueTextField.setTextFormatter(new TextFormatter<>(new CurrencyStringConverter(NumberFormat.getInstance(Locale.GERMAN))));

        // Monatsauswahlen initialisieren
        List<String> monthList = List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");
        month1ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));
        month2ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));
        month3ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));
        month4ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));

        month1ChoiceBox.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> obs, Number oldIndex, Number newIndex) -> {
            int month = newIndex.intValue();
            if(typeChoiceBox.getSelectionModel().getSelectedItem().equals("halbjährlich")) {
                month2ChoiceBox.getSelectionModel().select(((month + 6) % 12));
            }
            else if(typeChoiceBox.getSelectionModel().getSelectedItem().equals("vierteljährlich")) {
                month2ChoiceBox.getSelectionModel().select(((month + 3) % 12));
                month3ChoiceBox.getSelectionModel().select(((month + 6) % 12));
                month4ChoiceBox.getSelectionModel().select(((month + 9) % 12));
            }
        });

        List<String> typeList = List.of("monatlich", "jährlich", "halbjährlich", "vierteljährlich");
        typeChoiceBox.getItems().addAll(typeList);
        // Monatsboxen mit ChangeListener ausblenden
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> obsValue, String oldValue, String newValue) -> {
            switch(newValue) {
                case "jährlich":
                    manageChoiceBoxes(List.of(month1Label, month1ChoiceBox), List.of(month2Label, month2ChoiceBox, month3Label, month3ChoiceBox, month4Label, month4ChoiceBox));
                    break;
                case "halbjährlich":
                    manageChoiceBoxes(List.of(month1Label, month1ChoiceBox, month2Label, month2ChoiceBox), List.of(month3Label, month3ChoiceBox, month4Label, month4ChoiceBox));
                    break;
                case "monatlich":
                    manageChoiceBoxes(List.of(), List.of(month1Label, month1ChoiceBox, month2Label, month2ChoiceBox, month3Label, month3ChoiceBox, month4Label, month4ChoiceBox));
                    break;
                default:
                    // Alles anzeigen
                    manageChoiceBoxes(List.of(month1Label, month1ChoiceBox, month2Label, month2ChoiceBox, month3Label, month3ChoiceBox, month4Label, month4ChoiceBox), List.of());
            }
        });
        typeChoiceBox.getSelectionModel().select(0);
    }

    @FXML
    private void createNewExpense(ActionEvent e) {
        positionTextField.getStyleClass().remove("validation-error");
        valueTextField.getStyleClass().remove("validation-error");
        typeChoiceBox.getStyleClass().remove("validation-error");
        month1ChoiceBox.getStyleClass().remove("validation-error"); 
        month2ChoiceBox.getStyleClass().remove("validation-error");
        month3ChoiceBox.getStyleClass().remove("validation-error");
        month4ChoiceBox.getStyleClass().remove("validation-error");

        String position = positionTextField.getText().trim();
        double value = Double.parseDouble(valueTextField.getText());
        List<Integer> datesOfPayment = IntStream.rangeClosed(1, 12).boxed().toList();
        if(!typeChoiceBox.getSelectionModel().getSelectedItem().equals("monatlich")) {
            datesOfPayment = List.of(month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox)
                .stream()
                .map(box -> box.getSelectionModel().getSelectedIndex())
                .filter(selectedIndex -> selectedIndex > -1)
                .map(selectedIndex -> selectedIndex + 1)
                .toList();
        }

        PaymentType type = PaymentType.fromString(typeChoiceBox.getSelectionModel().getSelectedItem());

        FixedExpense newExpense = new FixedExpense(position, value, type, datesOfPayment);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<FixedExpense>> violations = validator.validate(newExpense);
        if(!violations.isEmpty()) {
            violations.stream().forEach(violation -> {
                switch(violation.getPropertyPath().toString()) {
                    case "position": positionTextField.getStyleClass().add("validation-error"); break;
                    case "value": valueTextField.getStyleClass().add("validation-error"); break;
                    case "type": typeChoiceBox.getStyleClass().add("validation-error"); break;
                    case "datesOfPayment": 
                        month1ChoiceBox.getStyleClass().add("validation-error"); 
                        month2ChoiceBox.getStyleClass().add("validation-error");
                        month3ChoiceBox.getStyleClass().add("validation-error");
                        month4ChoiceBox.getStyleClass().add("validation-error");
                        break;
                }
            });
        }
        else {
            EntityManager em = EntityManager.getInstance();
            em.persist(newExpense);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Ausgabe \"" + newExpense.getPosition() + "\" hinzugefügt.");
            alert.showAndWait();
        }
    }

    @FXML
    private void closeWindow(ActionEvent e) {
        ((Stage)((Button)e.getSource()).getScene().getWindow()).close();
    }

    private void manageChoiceBoxes(List<Control> elementsToShow, List<Control> elementsToHide) {
        elementsToHide.forEach(el -> el.setVisible(false));
        elementsToShow.forEach(el -> el.setVisible(true));
    }
}
