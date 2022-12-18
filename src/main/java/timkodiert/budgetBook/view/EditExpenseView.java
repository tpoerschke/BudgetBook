package timkodiert.budgetBook.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.PaymentInformation;
import timkodiert.budgetBook.util.EntityManager;

@RequiredArgsConstructor
public class EditExpenseView implements View, Initializable {

    private static int CURRENT_YEAR = LocalDate.now().getYear();

    private final FixedExpense expense;

    @FXML
    private ComboBox<Integer> displayYearComboBox;

    @FXML
    private TextField positionTextField;
    @FXML
    private TextArea noteTextArea;

    @FXML
    private TextField month1TextField, month2TextField, month3TextField, month4TextField, month5TextField, 
        month6TextField, month7TextField, month8TextField, month9TextField, month10TextField, month11TextField, month12TextField;

    private List<TextField> monthTextFields;

    private Map<Integer, List<Double>> newPayments = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positionTextField.setText(expense.getPosition());
        noteTextArea.setText(expense.getNote());

        monthTextFields = List.of(month1TextField, month2TextField, month3TextField, month4TextField, month5TextField, 
            month6TextField, month7TextField, month8TextField, month9TextField, month10TextField, month11TextField, month12TextField);

        displayYearComboBox.getItems().addAll(IntStream.rangeClosed(CURRENT_YEAR - 5, CURRENT_YEAR + 1).boxed().toList());
        displayYearComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldYear, Integer newYear) -> {
            // Neue Werte zwischenspeichern. Werden übernommen, wenn der Nutzer die Ausgabe speichert
            if(oldYear != null) {
                newPayments.put(oldYear, monthTextFields.stream().map(tf -> Double.valueOf(tf.getText())).toList());
            }
            // Textfields mit den werden des ausgewählten Jahres befüllen.
            IntStream.rangeClosed(1, 12).forEach(i -> {
                monthTextFields.get(i-1).setText("" + expense.getValueFor(newYear, i));
            });
        });
        displayYearComboBox.getSelectionModel().select(Integer.valueOf(CURRENT_YEAR));
    }

    @FXML 
    private void saveExpense(ActionEvent event) {

        // Eingetragene Werte übernehmen
        this.expense.setPosition(positionTextField.getText());
        this.expense.setNote(noteTextArea.getText());
        newPayments.put(displayYearComboBox.getSelectionModel().getSelectedItem(), monthTextFields.stream().map(tf -> Double.valueOf(tf.getText())).toList());

        // Speichern
        for(PaymentInformation payInfo : this.expense.getPaymentInformations()) {
            List<Double> newPaymentValues = this.newPayments.get(payInfo.getYear());
            if(newPaymentValues != null) {
                payInfo.getPayments().clear();
                IntStream.rangeClosed(1, 12).forEach(i -> {
                    if(newPaymentValues.get(i-1) != 0.0) {
                        payInfo.getPayments().put(i, newPaymentValues.get(i-1));
                    }
                });
            }
        }
        EntityManager.getInstance().persist(this.expense);
        // Fenster schließen
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void deleteExpense(ActionEvent event) {
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, "Die Ausgabe \"" + this.expense.getPosition() + "\" wirklich löschen?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if(result.filter(ButtonType.YES::equals).isPresent()) {
            EntityManager.getInstance().remove(this.expense);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
