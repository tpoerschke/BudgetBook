package timkodiert.budgetBook.view.fixedExpenses;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import timkodiert.budgetBook.domain.model.PaymentInformation;
import timkodiert.budgetBook.view.View;
import timkodiert.budgetBook.view.baseViews.BaseDetailView;
import timkodiert.budgetBook.view.widget.MonthYearPickerWidget;

public class FixedExpenseInformationDetailView extends BaseDetailView<PaymentInformation> implements Initializable {

    @FXML
    private TextField valueTextField;
    @FXML
    private ChoiceBox<String> month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox;
    @FXML
    private ComboBox<String> typeChoiceBox;
    @FXML
    private HBox dateContainer;

    private MonthYearPickerWidget startMonthWidget, endMonthWidget;

    public FixedExpenseInformationDetailView(Supplier<PaymentInformation> emptyEntityProducer) {
        super(emptyEntityProducer);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<String> typeList = List.of("monatlich", "jährlich", "halbjährlich", "vierteljährlich");
        typeChoiceBox.getItems().addAll(typeList);

        startMonthWidget = MonthYearPickerWidget.builder()
                .labelStr("Erster Monat")
                .parent(dateContainer)
                .initialValue(null)
                .build();
        endMonthWidget = MonthYearPickerWidget.builder()
                .labelStr("Letzter Monat (optional)")
                .parent(dateContainer)
                .initialValue(null)
                .showResetBtn(true)
                .build();
    }

    @FXML
    private void onSave(ActionEvent e) {

    }

    @FXML
    private void onRevert(ActionEvent e) {

    }

    @Override
    protected PaymentInformation patchEntity(PaymentInformation entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'patchEntity'");
    }

    @Override
    protected void patchUi(PaymentInformation entity) {
        valueTextField.setText(entity.getValue() + "");
        startMonthWidget.setValue(entity.getStart());
        endMonthWidget.setValue(entity.getEnd());
    }

}
