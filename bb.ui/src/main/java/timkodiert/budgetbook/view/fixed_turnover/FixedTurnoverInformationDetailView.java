package timkodiert.budgetbook.view.fixed_turnover;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;

import timkodiert.budgetbook.domain.PaymentInformationDTO;
import timkodiert.budgetbook.domain.PaymentType;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.ui.control.MoneyTextField;
import timkodiert.budgetbook.ui.control.MonthYearPickerWidget;
import timkodiert.budgetbook.ui.control.MonthYearPickerWidgetFactory;
import timkodiert.budgetbook.ui.helper.Bind;
import timkodiert.budgetbook.validation.ValidationResult;
import timkodiert.budgetbook.validation.ValidationWrapperFactory;
import timkodiert.budgetbook.view.mdv_base.BaseDetailView;

public class FixedTurnoverInformationDetailView extends BaseDetailView<PaymentInformationDTO> implements Initializable {

    private static final String MONTH_SELECTED_VALIDATION_KEY = "{fixedTurnover.month.selected}";

    @FXML
    private MoneyTextField valueTextField;
    @FXML
    private Label month1Label;
    @FXML
    private Label month2Label;
    @FXML
    private Label month3Label;
    @FXML
    private Label month4Label;
    @FXML
    private ChoiceBox<String> month1ChoiceBox;
    @FXML
    private ChoiceBox<String> month2ChoiceBox;
    @FXML
    private ChoiceBox<String> month3ChoiceBox;
    @FXML
    private ChoiceBox<String> month4ChoiceBox;
    @FXML
    private ComboBox<PaymentType> typeChoiceBox;
    @FXML
    private HBox dateContainer;

    private MonthYearPickerWidget startMonthWidget;
    private MonthYearPickerWidget endMonthWidget;

    private Stage stage;
    private PaymentInformationDTO backupBean;

    private final LanguageManager languageManager;
    private final MonthYearPickerWidgetFactory monthYearPickerWidgetFactory;
    private final BiConsumer<PaymentInformationDTO, PaymentInformationDTO> onSaveCallback;

    @AssistedInject
    public FixedTurnoverInformationDetailView(ValidationWrapperFactory<PaymentInformationDTO> validationWrapperFactory,
                                              LanguageManager languageManager,
                                              MonthYearPickerWidgetFactory monthYearPickerWidgetFactory,
                                              @Assisted BiConsumer<PaymentInformationDTO, PaymentInformationDTO> updateCallback) {
        super(validationWrapperFactory);
        this.languageManager = languageManager;
        this.monthYearPickerWidgetFactory = monthYearPickerWidgetFactory;
        this.onSaveCallback = updateCallback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startMonthWidget = monthYearPickerWidgetFactory.create(dateContainer, "Erster Monat", null, false);
        endMonthWidget = monthYearPickerWidgetFactory.create(dateContainer, "Letzter Monat (optional)", null, true);

        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> typeChoiceBoxListener(newValue));

        List.of(month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox)
            .forEach(e -> e.getItems().addAll(FXCollections.observableArrayList(languageManager.getMonths())));

        // Bindings
        valueTextField.integerValueProperty().bindBidirectional(beanAdapter.getProperty(PaymentInformationDTO::getValue, PaymentInformationDTO::setValue));

        List<PaymentType> typeList = List.of(PaymentType.MONTHLY, PaymentType.ANNUAL, PaymentType.SEMIANNUAL, PaymentType.QUARTERLY);
        Bind.comboBox(typeChoiceBox,
                      beanAdapter.getProperty(PaymentInformationDTO::getType, PaymentInformationDTO::setType),
                      typeList,
                      PaymentType.class);

        startMonthWidget.valueProperty().bindBidirectional(beanAdapter.getProperty(PaymentInformationDTO::getStart, PaymentInformationDTO::setStart));
        endMonthWidget.valueProperty().bindBidirectional(beanAdapter.getProperty(PaymentInformationDTO::getEnd, PaymentInformationDTO::setEnd));

        // Validierungen
        validationMap.put("start", startMonthWidget.getMonthChoiceBox());
        validationMap.put("type", typeChoiceBox);
        validationWrapper.register(typeChoiceBox.getSelectionModel().selectedItemProperty(),
                                   startMonthWidget.valueProperty(),
                                   endMonthWidget.valueProperty());
        validationWrapper.registerCustomValidation("valueValid",
                                                   valueTextField.getTextField(),
                                                   () -> valueTextField.isStringFormatValid()
                                                           ? ValidationResult.valid()
                                                           : ValidationResult.error("{amount.format.valid}"),
                                                   valueTextField.getTextField().textProperty());
        validationWrapper.registerCustomValidation("month1Valid",
                                                   month1ChoiceBox,
                                                   () -> !month1ChoiceBox.isVisible() || month1ChoiceBox.getSelectionModel().getSelectedItem() != null
                                                           ? ValidationResult.valid()
                                                           : ValidationResult.error(MONTH_SELECTED_VALIDATION_KEY),
                                                   month1ChoiceBox.getSelectionModel().selectedItemProperty(),
                                                   typeChoiceBox.getSelectionModel().selectedItemProperty());
        validationWrapper.registerCustomValidation("month2Valid",
                                                   month2ChoiceBox,
                                                   () -> !month2ChoiceBox.isVisible() || month2ChoiceBox.getSelectionModel().getSelectedItem() != null
                                                           ? ValidationResult.valid()
                                                           : ValidationResult.error(MONTH_SELECTED_VALIDATION_KEY),
                                                   month2ChoiceBox.getSelectionModel().selectedItemProperty(),
                                                   typeChoiceBox.getSelectionModel().selectedItemProperty());
        validationWrapper.registerCustomValidation("month3Valid",
                                                   month3ChoiceBox,
                                                   () -> !month3ChoiceBox.isVisible() || month3ChoiceBox.getSelectionModel().getSelectedItem() != null
                                                           ? ValidationResult.valid()
                                                           : ValidationResult.error(MONTH_SELECTED_VALIDATION_KEY),
                                                   month3ChoiceBox.getSelectionModel().selectedItemProperty(),
                                                   typeChoiceBox.getSelectionModel().selectedItemProperty());
        validationWrapper.registerCustomValidation("month4Valid",
                                                   month4ChoiceBox,
                                                   () -> !month4ChoiceBox.isVisible() || month4ChoiceBox.getSelectionModel().getSelectedItem() != null
                                                           ? ValidationResult.valid()
                                                           : ValidationResult.error(MONTH_SELECTED_VALIDATION_KEY),
                                                   month4ChoiceBox.getSelectionModel().selectedItemProperty(),
                                                   typeChoiceBox.getSelectionModel().selectedItemProperty());
        validationWrapper.registerCustomValidation("endBeforeStart",
                                                   endMonthWidget.getMonthChoiceBox(),
                                                   () -> endMonthWidget.valueProperty().get() == null ||
                                                           endMonthWidget.valueProperty().get().isAfter(startMonthWidget.valueProperty().get())
                                                           ? ValidationResult.valid()
                                                           : ValidationResult.error("{fixedTurnover.end.afterStart}"),
                                                   month4ChoiceBox.getSelectionModel().selectedItemProperty(),
                                                   typeChoiceBox.getSelectionModel().selectedItemProperty());
    }

    @Override
    protected void beanSet() {
        backupBean = SerializationUtils.clone(beanAdapter.getBean());
        PaymentInformationDTO payInfo = beanAdapter.getBean();
        if (PaymentType.MONTHLY != payInfo.getType()) {
            List<ChoiceBox<String>> choiceBoxes = List.of(month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox);
            for (int i = 0; i < payInfo.getMonthsOfPayment().size(); i++) {
                choiceBoxes.get(i).getSelectionModel().select(payInfo.getMonthsOfPayment().get(i) - 1);
            }
        }
    }

    @FXML
    private void onSave(ActionEvent e) {
        if (!validate()) {
            return;
        }
        setMonthsOfPayment(beanAdapter.getBean());
        onSaveCallback.accept(null, beanAdapter.getBean());
        stage.close();
    }

    @FXML
    private void onRevert() {
        PaymentInformationDTO modified = beanAdapter.getBean();
        setBean(SerializationUtils.clone(backupBean));
        onSaveCallback.accept(modified, beanAdapter.getBean());
    }

    private void setMonthsOfPayment(PaymentInformationDTO paymentInformationDTO) {
        List<Integer> datesOfPayment = IntStream.rangeClosed(1, 12).boxed().toList();
        if (typeChoiceBox.getSelectionModel().getSelectedItem() != PaymentType.MONTHLY) {
            datesOfPayment = Stream.of(month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox)
                                   .map(box -> box.getSelectionModel().getSelectedIndex())
                                   .filter(selectedIndex -> selectedIndex > -1)
                                   .map(selectedIndex -> selectedIndex + 1)
                                   .toList();
        }
        paymentInformationDTO.setMonthsOfPayment(datesOfPayment);
    }

    private void typeChoiceBoxListener(PaymentType newValue) {
        switch (newValue) {
            case ANNUAL -> manageChoiceBoxes(List.of(month1Label, month1ChoiceBox),
                                             List.of(month2Label, month2ChoiceBox,
                                                     month3Label, month3ChoiceBox,
                                                     month4Label, month4ChoiceBox));
            case SEMIANNUAL -> manageChoiceBoxes(List.of(month1Label, month1ChoiceBox,
                                                         month2Label, month2ChoiceBox),
                                                 List.of(month3Label, month3ChoiceBox,
                                                         month4Label, month4ChoiceBox));
            case MONTHLY -> manageChoiceBoxes(List.of(),
                                              List.of(month1Label, month1ChoiceBox,
                                                      month2Label, month2ChoiceBox,
                                                      month3Label, month3ChoiceBox,
                                                      month4Label, month4ChoiceBox));
            // Alles anzeigen
            case null, default -> manageChoiceBoxes(List.of(month1Label, month1ChoiceBox,
                                                            month2Label, month2ChoiceBox,
                                                            month3Label, month3ChoiceBox,
                                                            month4Label, month4ChoiceBox),
                                                    List.of());
        }
    }

    private void manageChoiceBoxes(List<Control> elementsToShow, List<Control> elementsToHide) {
        elementsToHide.forEach(el -> el.setVisible(false));
        elementsToShow.forEach(el -> {
            el.setVisible(true);
            if (el instanceof ChoiceBox<?> cb) {
                cb.getSelectionModel().clearSelection();
            }
        });
    }

    @Override
    protected PaymentInformationDTO createEmptyEntity() {
        return new PaymentInformationDTO();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> onRevert());
    }
}
