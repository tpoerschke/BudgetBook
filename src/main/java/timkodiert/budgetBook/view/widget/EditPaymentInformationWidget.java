package timkodiert.budgetBook.view.widget;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import lombok.Getter;
import timkodiert.budgetBook.domain.model.PaymentInformation;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.repository.Repository;

public class EditPaymentInformationWidget implements Initializable {

    @FXML
    private TitledPane root;
    @FXML
    private Label month1Label, month2Label, month3Label, month4Label;
    @FXML
    private ChoiceBox<String> typeChoiceBox, month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox;
    @FXML
    private TextField valueTextField;
    @FXML
    private Pane startWidgetContainer, endWidgetContainer;

    private PaymentInformation payInfo;

    @Getter
    private boolean isDeleted = false;

    private MonthYearPickerWidget startMonthWidget, endMonthWidget;

    private Repository<PaymentInformation> repository;

    @AssistedInject
    public EditPaymentInformationWidget(@Assisted Pane parent, @Assisted PaymentInformation payInfo, Repository<PaymentInformation> repository) {
        this.payInfo = payInfo;
        this.repository = repository;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditPaymentInformationWidget.fxml"));
            loader.setController(this);
            parent.getChildren().add(loader.load());
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "Widget konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    public void persistUpdate() {
        if(isDeleted) {
            this.repository.remove(payInfo);
        }
        else {
            // Entity patchen
            List<Integer> datesOfPayment = IntStream.rangeClosed(1, 12).boxed().toList();
            if(!typeChoiceBox.getSelectionModel().getSelectedItem().equals("monatlich")) {
                datesOfPayment = List.of(month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox)
                    .stream()
                    .map(box -> box.getSelectionModel().getSelectedIndex())
                    .filter(selectedIndex -> selectedIndex > -1)
                    .map(selectedIndex -> selectedIndex + 1)
                    .toList();
            }
            this.payInfo.setMonthsOfPayment(datesOfPayment);
            this.payInfo.setType(PaymentType.fromString(typeChoiceBox.getSelectionModel().getSelectedItem()));
            this.payInfo.setValue(Double.parseDouble(valueTextField.getText()));
            this.payInfo.setStart(startMonthWidget.getValue());
            this.payInfo.setEnd(endMonthWidget.getValue());
        }
    }

    @FXML
    private void deletePaymentInformation(ActionEvent e) {
        // Merkt vor, dass diese Information gelöscht werden soll.
        // Wird tatsächlich gelöscht, wenn persistUpdate() 
        // aufgerufen wird (damit "Abbrechen" möglich ist).
        isDeleted = true;
        root.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisieren
        List<String> monthList = List.of("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember");
        month1ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));
        month2ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));
        month3ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));
        month4ChoiceBox.getItems().addAll(FXCollections.observableArrayList(monthList));

        List<String> typeList = List.of("monatlich", "jährlich", "halbjährlich", "vierteljährlich");
        typeChoiceBox.getItems().addAll(typeList);
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

        // Werte setzen
        if(payInfo.getEnd() == null) {
            root.setText("Ab " + payInfo.getStart().toString());
        }
        else {
            root.setText(payInfo.getStart().toString() +  " - " + payInfo.getEnd().toString());
        }

        valueTextField.setText(payInfo.getValue() + "");

        typeChoiceBox.getSelectionModel().select(payInfo.getType().getType());

        if(!payInfo.getType().equals(PaymentType.MONTHLY)) {
            List<ChoiceBox<String>> choiceBoxes = List.of(month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox);
            for(int i = 0; i < payInfo.getMonthsOfPayment().size(); i++) {
                choiceBoxes.get(i).getSelectionModel().select(payInfo.getMonthsOfPayment().get(i)-1);
            }
        }

        startMonthWidget = MonthYearPickerWidget.builder()
            .labelStr("Erster Monat")
            .parent(startWidgetContainer)
            .initialValue(payInfo.getStart())
            .build();
        endMonthWidget = MonthYearPickerWidget.builder()
            .labelStr("Letzter Monat (optional)")
            .parent(endWidgetContainer)
            .initialValue(payInfo.getEnd())
            .showResetBtn(true)
            .build();
    }

    private void manageChoiceBoxes(List<Control> elementsToShow, List<Control> elementsToHide) {
        elementsToHide.forEach(el -> el.setVisible(false));
        elementsToShow.forEach(el -> el.setVisible(true));
    }
}
