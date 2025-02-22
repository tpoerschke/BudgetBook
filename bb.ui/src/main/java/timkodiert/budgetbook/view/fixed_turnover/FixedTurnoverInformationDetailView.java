package timkodiert.budgetbook.view.fixed_turnover;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
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

import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.domain.model.PaymentInformation;
import timkodiert.budgetbook.domain.model.PaymentType;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.view.mdv_base.BaseDetailView;
import timkodiert.budgetbook.view.widget.MonthYearPickerWidget;
import timkodiert.budgetbook.view.widget.MonthYearPickerWidgetFactory;

public class FixedTurnoverInformationDetailView extends BaseDetailView<PaymentInformation> implements Initializable {

    @FXML
    private TextField valueTextField;
    @FXML
    private Label month1Label, month2Label, month3Label, month4Label;
    @FXML
    private ChoiceBox<String> month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox;
    @FXML
    private ComboBox<PaymentType> typeChoiceBox;
    @FXML
    private HBox dateContainer;

    private MonthYearPickerWidget startMonthWidget, endMonthWidget;

    private final LanguageManager languageManager;
    private final MonthYearPickerWidgetFactory monthYearPickerWidgetFactory;
    private final Consumer<PaymentInformation> onSaveCallback;

    @AssistedInject
    public FixedTurnoverInformationDetailView(LanguageManager languageManager,
                                              MonthYearPickerWidgetFactory monthYearPickerWidgetFactory,
                                              @Assisted Supplier<PaymentInformation> emptyEntityProducer,
                                              @Assisted Consumer<PaymentInformation> onSaveCallback) {
        super(emptyEntityProducer);
        this.languageManager = languageManager;
        this.monthYearPickerWidgetFactory = monthYearPickerWidgetFactory;
        this.onSaveCallback = onSaveCallback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        startMonthWidget = monthYearPickerWidgetFactory.create(dateContainer, "Erster Monat", null, false);
        endMonthWidget = monthYearPickerWidgetFactory.create(dateContainer, "Letzter Monat (optional)", null, true);

        List<PaymentType> typeList = List.of(PaymentType.MONTHLY,
                                             PaymentType.ANNUAL,
                                             PaymentType.SEMIANNUAL,
                                             PaymentType.QUARTERLY);
        typeChoiceBox.getItems().addAll(typeList);
        typeChoiceBox.setConverter(Converters.get(PaymentType.class));
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener(this::typeChoiceBoxListener);

        List.of(month1ChoiceBox,month2ChoiceBox,month3ChoiceBox,month4ChoiceBox).forEach(e->e.getItems().addAll(FXCollections.observableArrayList(
                languageManager.getMonths())));
    }

    @FXML
    private void onSave(ActionEvent e) {
        if (!validate()) {
            return;
        }

        var expenseInfo = patchEntity(entity.get(), true);
        onSaveCallback.accept(expenseInfo);
        // Das macht mich traurig ._.
        ((Stage) ((Node) e.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void onRevert(ActionEvent e) {
        patchUi(entity.get());
    }

    @Override
    protected PaymentInformation patchEntity(PaymentInformation entity, boolean isSaving) {
        entity.setValue(Double.parseDouble(valueTextField.getText()));
        entity.setStart(startMonthWidget.getValue());
        entity.setEnd(endMonthWidget.getValue());
        entity.setType(typeChoiceBox.getSelectionModel().getSelectedItem());
        List<Integer> datesOfPayment = IntStream.rangeClosed(1, 12).boxed().toList();
        if (typeChoiceBox.getSelectionModel().getSelectedItem() != PaymentType.MONTHLY) {
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
        typeChoiceBox.getSelectionModel().select(type);
        if (!PaymentType.MONTHLY.equals(type)) {
            List<ChoiceBox<String>> choiceBoxes = List.of(month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox);
            for (int i = 0; i < entity.getMonthsOfPayment().size(); i++) {
                choiceBoxes.get(i).getSelectionModel().select(entity.getMonthsOfPayment().get(i) - 1);
            }
        }
    }

    private void typeChoiceBoxListener(ObservableValue<? extends PaymentType> obsValue, PaymentType oldValue, PaymentType newValue) {
        switch (newValue) {
            case ANNUAL:
                manageChoiceBoxes(List.of(month1Label, month1ChoiceBox),
                                  List.of(month2Label, month2ChoiceBox,
                                          month3Label, month3ChoiceBox,
                                          month4Label, month4ChoiceBox));
                break;
            case SEMIANNUAL:
                manageChoiceBoxes(List.of(month1Label, month1ChoiceBox,
                                          month2Label, month2ChoiceBox),
                                  List.of(month3Label, month3ChoiceBox,
                                          month4Label, month4ChoiceBox));
                break;
            case MONTHLY:
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
