package timkodiert.budgetBook.view.fixed_expense;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import timkodiert.budgetBook.domain.model.PaymentInformation;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.view.mdv_base.BaseDetailView;
import timkodiert.budgetBook.view.widget.MonthYearPickerWidget;

public class FixedExpenseInformationDetailView extends BaseDetailView<PaymentInformation> implements Initializable {

    @FXML
    private TextField valueTextField;
    @FXML
    private Label month1Label, month2Label, month3Label, month4Label;
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

        List<String> typeList = List.of(PaymentType.MONTHLY.getType(),
                                        PaymentType.ANNUAL.getType(),
                                        PaymentType.SEMIANNUAL.getType(),
                                        PaymentType.QUARTERLY.getType());
        typeChoiceBox.getItems().addAll(typeList);
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::typeChoiceBoxListener);

        List.of(month1ChoiceBox,month2ChoiceBox,month3ChoiceBox,month4ChoiceBox).forEach(e->e.getItems().addAll(FXCollections.observableArrayList(
                LanguageManager.getInstance().getMonths())));
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
        entity.setValue(Double.parseDouble(valueTextField.getText()));
        entity.setStart(startMonthWidget.getValue());
        entity.setEnd(endMonthWidget.getValue());
        entity.setType(PaymentType.fromString(typeChoiceBox.getSelectionModel().getSelectedItem()));
        List<Integer> datesOfPayment = IntStream.rangeClosed(1, 12).boxed().toList();
        if (!typeChoiceBox.getSelectionModel().getSelectedItem().equals("monatlich")) {
            datesOfPayment = List.of(month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox)
                                 .stream()
                                 .map(box -> box.getSelectionModel().getSelectedIndex())
                                 .filter(selectedIndex -> selectedIndex > -1)
                                 .map(selectedIndex -> selectedIndex + 1)
                                 .toList();
        }
        entity.setMonthsOfPayment(datesOfPayment);
        return entity;
    }

    @Override
    protected void patchUi(PaymentInformation entity) {
        valueTextField.setText(entity.getValue() + "");
        startMonthWidget.setValue(entity.getStart());
        endMonthWidget.setValue(entity.getEnd());
        PaymentType type = entity.getType() != null ? entity.getType() : PaymentType.MONTHLY;
        typeChoiceBox.getSelectionModel().select(type.getType());
        if (!PaymentType.MONTHLY.equals(type)) {
            List<ChoiceBox<String>> choiceBoxes = List.of(month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox);
            for (int i = 0; i < entity.getMonthsOfPayment().size(); i++) {
                choiceBoxes.get(i).getSelectionModel().select(entity.getMonthsOfPayment().get(i) - 1);
            }
        }
    }

    private void typeChoiceBoxListener(ObservableValue<? extends String> obsValue, String oldValue, String newValue) {
        switch (newValue) {
            case "jährlich":
                manageChoiceBoxes(List.of(month1Label, month1ChoiceBox),
                                  List.of(month2Label, month2ChoiceBox,
                                          month3Label, month3ChoiceBox,
                                          month4Label, month4ChoiceBox));
                break;
            case "halbjährlich":
                manageChoiceBoxes(List.of(month1Label, month1ChoiceBox,
                                          month2Label, month2ChoiceBox),
                                  List.of(month3Label, month3ChoiceBox,
                                          month4Label, month4ChoiceBox));
                break;
            case "monatlich":
                manageChoiceBoxes(List.of(),
                                  List.of(month1Label, month1ChoiceBox,
                                          month2Label, month2ChoiceBox,
                                          month3Label, month3ChoiceBox,
                                          month4Label, month4ChoiceBox));
                break;
            default:
                // Alles anzeigen
                manageChoiceBoxes(List.of(month1Label, month1ChoiceBox,
                                          month2Label, month2ChoiceBox,
                                          month3Label, month3ChoiceBox,
                                          month4Label, month4ChoiceBox),
                                  List.of());
        }
    }

    private void manageChoiceBoxes(List<Control> elementsToShow, List<Control> elementsToHide) {
        elementsToHide.forEach(el -> el.setVisible(false));
        elementsToShow.forEach(el -> el.setVisible(true));
    }
}
