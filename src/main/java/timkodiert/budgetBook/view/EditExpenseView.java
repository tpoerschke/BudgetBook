package timkodiert.budgetBook.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import timkodiert.budgetBook.domain.model.FixedExpense;

@RequiredArgsConstructor
public class EditExpenseView implements View, Initializable {

    private static int CURRENT_YEAR = LocalDate.now().getYear();

    private final FixedExpense expense;

    @FXML
    private ComboBox<Integer> displayYearComboBox;

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

        displayYearComboBox.getItems().addAll(IntStream.rangeClosed(CURRENT_YEAR - 5, CURRENT_YEAR + 1).boxed().toList());
        displayYearComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldYear, Integer newYear) -> {
            IntStream.rangeClosed(1, 12).forEach(i -> {
                monthTextFields.get(i-1).setText("" + expense.getValueFor(newYear, i));
            });
        });
        displayYearComboBox.getSelectionModel().select(Integer.valueOf(CURRENT_YEAR));
    }
}
