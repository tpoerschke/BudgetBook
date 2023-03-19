package timkodiert.budgetBook.view.uniqueExpenses;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timkodiert.budgetBook.domain.model.UniqueExpenseInformation;
import timkodiert.budgetBook.view.View;

public class UniqueExpenseInformationDetailView implements View, Initializable {

    private UniqueExpenseInformation expenseInfo;
    private boolean isNew;
    private Consumer<UniqueExpenseInformation> newEntityCallback;

    @FXML
    private TextField positionTextField, valueTextField;

    public UniqueExpenseInformationDetailView(Optional<UniqueExpenseInformation> optionalEntity,
            Consumer<UniqueExpenseInformation> newEntityCallback) {
        this.expenseInfo = optionalEntity.orElse(new UniqueExpenseInformation());
        this.isNew = optionalEntity.isEmpty();
        this.newEntityCallback = newEntityCallback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showEntity(expenseInfo);
    }

    public void showEntity(UniqueExpenseInformation entity) {
        positionTextField.setText(expenseInfo.getLabel());
        valueTextField.setText(String.valueOf(expenseInfo.getValue()));
    }

    @FXML
    private void onSave(ActionEvent e) {
        expenseInfo.setLabel(positionTextField.getText());
        expenseInfo.setValue(Double.valueOf(valueTextField.getText()));

        if (isNew) {
            newEntityCallback.accept(expenseInfo);
        }

        // Das macht mich traurig ._.
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

}
