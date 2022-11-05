package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

        List<String> monthList = List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");
        month1ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));
        month2ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));
        month3ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));
        month4ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));

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
    private void closeWindow(ActionEvent e) {
        ((Stage)((Button)e.getSource()).getScene().getWindow()).close();
    }

    private void manageChoiceBoxes(List<Control> elementsToShow, List<Control> elementsToHide) {
        elementsToHide.forEach(el -> el.setVisible(false));
        elementsToShow.forEach(el -> el.setVisible(true));
    }
}
