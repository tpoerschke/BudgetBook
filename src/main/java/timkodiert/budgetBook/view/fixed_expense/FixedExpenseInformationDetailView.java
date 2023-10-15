package timkodiert.budgetBook.view.fixed_expense;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import timkodiert.budgetBook.domain.model.PaymentInformation;
import timkodiert.budgetBook.view.mdv_base.BaseDetailView;
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

    private Consumer<PaymentInformation> onSaveCallback;

    public FixedExpenseInformationDetailView(Supplier<PaymentInformation> emptyEntityProducer, Consumer<PaymentInformation> onSaveCallback) {
        super(emptyEntityProducer);
        this.onSaveCallback = onSaveCallback;
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
        if (!validate()) {
            return;
        }

        var expenseInfo = patchEntity(entity.get());
        onSaveCallback.accept(expenseInfo);
        // Das macht mich traurig ._.
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void onRevert(ActionEvent e) {
        patchUi(entity.get());
    }

    @Override
    protected PaymentInformation patchEntity(PaymentInformation entity) {
        entity.setValue(Double.valueOf(valueTextField.getText()));
        entity.setStart(startMonthWidget.getValue());
        entity.setEnd(endMonthWidget.getValue());
        return entity;
    }

    @Override
    protected void patchUi(PaymentInformation entity) {
        valueTextField.setText(entity.getValue() + "");
        startMonthWidget.setValue(entity.getStart());
        endMonthWidget.setValue(entity.getEnd());
    }

}
