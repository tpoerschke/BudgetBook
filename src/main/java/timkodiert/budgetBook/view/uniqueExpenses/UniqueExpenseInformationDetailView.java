package timkodiert.budgetBook.view.uniqueExpenses;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import timkodiert.budgetBook.domain.model.UniqueExpenseInformation;
import timkodiert.budgetBook.view.View;

public class UniqueExpenseInformationDetailView implements View, Initializable {

    private UniqueExpenseInformation expenseInfo;

    @FXML
    private TextField positionTextField, valueTextField;

    public UniqueExpenseInformationDetailView(Optional<UniqueExpenseInformation> optionalEntity) {
        expenseInfo = optionalEntity.orElse(new UniqueExpenseInformation());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showEntity(expenseInfo);
    }

    public void showEntity(UniqueExpenseInformation entity) {
        positionTextField.setText(expenseInfo.getLabel());
        valueTextField.setText(String.valueOf(expenseInfo.getValue()));
    }

}
