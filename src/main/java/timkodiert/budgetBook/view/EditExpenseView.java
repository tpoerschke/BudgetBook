package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import timkodiert.budgetBook.domain.model.FixedExpense;

@RequiredArgsConstructor
public class EditExpenseView implements View, Initializable {

    private static int CURRENT_YEAR = 2022;

    private final FixedExpense expense;

    @FXML
    private TextField positionTextField;

    @FXML
    private TextField month1TextField, month2TextField, month3TextField, month4TextField, month5TextField, 
        month6TextField, month7TextField, month8TextField, month9TextField, month10TextField, month11TextField, month12TextField;

    private List<TextField> monthTextFields;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positionTextField.setText(expense.getPosition());

        monthTextFields = List.of(month1TextField, month2TextField, month3TextField, month4TextField, month5TextField, 
            month6TextField, month7TextField, month8TextField, month9TextField, month10TextField, month11TextField, month12TextField);

        IntStream.rangeClosed(1, 12).forEach(i -> {
            monthTextFields.get(i-1).setText("" + expense.getValueFor(CURRENT_YEAR, i));
        });
    }
}
