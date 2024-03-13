package timkodiert.budgetBook.util.dialog;

import javax.inject.Inject;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

public class DialogFactory {

    public static ButtonType CANCEL = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
    public static ButtonType SAVE_CHANGES = new ButtonType("Änderungen speichern", ButtonData.YES);
    public static ButtonType DISCARD_CHANGES = new ButtonType("Änderungen verwerfen", ButtonData.NO);

    @Inject
    public DialogFactory() {
    }

    public Alert buildConfirmationDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Wie soll mit den Änderungen verfahren werden?",
                SAVE_CHANGES, DISCARD_CHANGES, CANCEL);
        alert.setHeaderText("Es liegen ungespeicherte Änderungen vor.");
        return alert;
    }

    public Alert buildInformationDialog(String information) {
        Alert alert = new Alert(AlertType.INFORMATION, information);
        return alert;
    }

    public Alert buildErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR, message);
        return alert;
    }
}
