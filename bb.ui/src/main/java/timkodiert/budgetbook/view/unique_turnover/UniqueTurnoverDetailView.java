package timkodiert.budgetbook.view.unique_turnover;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.inject.Provider;

import atlantafx.base.controls.Message;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetbook.converter.DoubleCurrencyStringConverter;
import timkodiert.budgetbook.converter.ReferenceStringConverter;
import timkodiert.budgetbook.dialog.StackTraceAlert;
import timkodiert.budgetbook.domain.AccountTurnoverDTO;
import timkodiert.budgetbook.domain.FixedTurnoverCrudService;
import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.UniqueTurnoverCrudService;
import timkodiert.budgetbook.domain.UniqueTurnoverDTO;
import timkodiert.budgetbook.domain.UniqueTurnoverInformationDTO;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.ui.control.AutoCompleteTextField;
import timkodiert.budgetbook.ui.helper.Bind;
import timkodiert.budgetbook.util.StageBuilder;
import timkodiert.budgetbook.view.mdv_base.EntityBaseDetailView;

import static timkodiert.budgetbook.util.ObjectUtils.nvl;
import static timkodiert.budgetbook.view.FxmlResource.IMAGE_VIEW;
import static timkodiert.budgetbook.view.FxmlResource.UNIQUE_TURNOVER_INFORMATION_VIEW;

public class UniqueTurnoverDetailView extends EntityBaseDetailView<UniqueTurnoverDTO> implements Initializable {

    @FXML
    private Pane root;
    @FXML
    private AutoCompleteTextField billerTextField;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField receiptTextField;
    @FXML
    private ImageView receiptImageView;

    @FXML
    private Button addUniqueExpenseInformationButton;
    @FXML
    private Button editUniqueExpenseInformationButton;
    @FXML
    private Button deleteUniqueExpenseInformationButton;

    @FXML
    private TableView<UniqueTurnoverInformationDTO> expenseInfoTable;
    @FXML
    private TableColumn<UniqueTurnoverInformationDTO, String> expenseInfoPositionCol;
    @FXML
    private TableColumn<UniqueTurnoverInformationDTO, String> expenseInfoValueCol;
    @FXML
    private TableColumn<UniqueTurnoverInformationDTO, String> expenseInfoCategoriesCol;

    @FXML
    private ComboBox<Reference<FixedTurnoverDTO>> fixedTurnoverComboBox;

    @FXML
    private TextField importReceiverTextField;
    @FXML
    private TextField importReferenceTextField;
    @FXML
    private TextField importPostingTextTextField;
    @FXML
    private TextField importAmountTextField;
    @FXML
    private Message amountWarningMessage;

    @FXML
    private ColumnConstraints rightColumn;

    private final DoubleCurrencyStringConverter doubleCurrencyStringConverter;
    private final UniqueTurnoverCrudService crudService;
    private final FixedTurnoverCrudService fixedTurnoverCrudService;
    private final LanguageManager languageManager;
    private final Provider<StageBuilder> stageBuilderProvider;
    private final UniqueTurnoverInformationDetailViewFactory uniqueTurnoverInformationDetailViewFactory;

    @Inject
    public UniqueTurnoverDetailView(DoubleCurrencyStringConverter doubleCurrencyStringConverter,
                                    UniqueTurnoverCrudService crudService,
                                    FixedTurnoverCrudService fixedTurnoverCrudService,
                                    LanguageManager languageManager,
                                    Provider<StageBuilder> stageBuilderProvider,
                                    UniqueTurnoverInformationDetailViewFactory uniqueTurnoverInformationDetailViewFactory) {
        super();
        this.doubleCurrencyStringConverter = doubleCurrencyStringConverter;
        this.crudService = crudService;
        this.fixedTurnoverCrudService = fixedTurnoverCrudService;
        this.languageManager = languageManager;
        this.stageBuilderProvider = stageBuilderProvider;
        this.uniqueTurnoverInformationDetailViewFactory = uniqueTurnoverInformationDetailViewFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addUniqueExpenseInformationButton.setGraphic(new FontIcon(BootstrapIcons.PLUS));
        editUniqueExpenseInformationButton.setGraphic(new FontIcon(BootstrapIcons.PENCIL));
        deleteUniqueExpenseInformationButton.setGraphic(new FontIcon(BootstrapIcons.TRASH));
        editUniqueExpenseInformationButton.disableProperty()
                .bind(expenseInfoTable.getSelectionModel().selectedItemProperty().isNull());
        deleteUniqueExpenseInformationButton.disableProperty()
                .bind(expenseInfoTable.getSelectionModel().selectedItemProperty().isNull());

        root.disableProperty().bind(beanAdapter.isEmpty());

        receiptTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isBlank()) {
                receiptImageView.setImage(new Image(new File(newValue).toURI().toString()));
            } else {
                receiptImageView.setImage(null);
            }
        });
        receiptImageView.setOnMouseClicked(event -> {
            String path = receiptTextField.getText();
            if (path == null || path.isBlank()) {
                return;
            }
            try {
                stageBuilderProvider.get()
                                    .withFXMLResource(IMAGE_VIEW.toString())
                                    .withModality(Modality.APPLICATION_MODAL)
                                    .withTitle("Beleg / Kassenbon")
                                    .withView(new ImageModalView(languageManager, path))
                                    .build()
                                    .stage()
                                    .showAndWait();
            } catch (IOException e) {
                StackTraceAlert.of("ImageModal konnte nicht geöffnet werden", e).showAndWait();
            }
        });

        expenseInfoPositionCol
                .setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLabel()));
        expenseInfoValueCol.setCellValueFactory(cellData -> {
            DoubleCurrencyStringConverter converter = new DoubleCurrencyStringConverter();
            return new ReadOnlyStringWrapper(converter.toString(cellData.getValue().getValueSigned()));
        });
        expenseInfoCategoriesCol.setCellValueFactory(cellData ->
                                                             new ReadOnlyStringWrapper(String.join(", ",
                                                                                                   cellData.getValue()
                                                                                                           .getCategories()
                                                                                                           .stream()
                                                                                                           .map(Reference::name)
                                                                                                           .toList())));

        billerTextField.getAvailableEntries().addAll(crudService.getUniqueTurnoverLabels());

        fixedTurnoverComboBox.setConverter(new ReferenceStringConverter<>());
        fixedTurnoverComboBox.getItems().add(null);
        fixedTurnoverComboBox.getItems().addAll(fixedTurnoverCrudService.findAllAsReference());

        // Bindings
        billerTextField.textProperty().bindBidirectional(beanAdapter.getProperty(UniqueTurnoverDTO::getBiller, UniqueTurnoverDTO::setBiller));
        datePicker.valueProperty().bindBidirectional(beanAdapter.getProperty(UniqueTurnoverDTO::getDate, UniqueTurnoverDTO::setDate));
        noteTextArea.textProperty().bindBidirectional(beanAdapter.getProperty(UniqueTurnoverDTO::getNote, UniqueTurnoverDTO::setNote));
        receiptTextField.textProperty().bindBidirectional(beanAdapter.getProperty(UniqueTurnoverDTO::getReceiptImagePath, UniqueTurnoverDTO::setReceiptImagePath));

        Bindings.bindContentBidirectional(expenseInfoTable.getItems(), beanAdapter.getListProperty(UniqueTurnoverDTO::getPaymentInformations,
                                                                                                   UniqueTurnoverDTO::setPaymentInformations));

        Bind.comboBox(fixedTurnoverComboBox, beanAdapter.getProperty(UniqueTurnoverDTO::getFixedTurnover, UniqueTurnoverDTO::setFixedTurnover));

        // Validierungen
        validationMap.put("biller", billerTextField);
        validationMap.put("date", datePicker);
        validationMap.put("paymentInformations", expenseInfoTable);
    }

    @Override
    protected void beanSet() {
        importReceiverTextField.setText(nvl(beanAdapter.getBean().getAccountTurnover(), AccountTurnoverDTO::getReceiver));
        importReferenceTextField.setText(nvl(beanAdapter.getBean().getAccountTurnover(), AccountTurnoverDTO::getReference));
        importPostingTextTextField.setText(nvl(beanAdapter.getBean().getAccountTurnover(), AccountTurnoverDTO::getPostingText));
        importAmountTextField.setText(doubleCurrencyStringConverter.toString(nvl(beanAdapter.getBean().getAccountTurnover(), AccountTurnoverDTO::getAmount)));
        updateImportAmountWarning();
    }

    private void updateImportAmountWarning() {
        if (beanAdapter.isEmpty().get() || beanAdapter.getBean().getAccountTurnover() == null) {
            importAmountTextField.setStyle("");
            amountWarningMessage.setVisible(false);
            return;
        }
        double turnoverValue = beanAdapter.getBean().getTotalValue();
        double importedValue = beanAdapter.getBean().getAccountTurnover().getAmount();
        if (turnoverValue == importedValue) {
            importAmountTextField.setStyle("");
            amountWarningMessage.setVisible(false);
        } else {
            importAmountTextField.setStyle("-fx-border-color: -color-warning-2;");
            amountWarningMessage.setVisible(true);
        }
    }

    @Override
    public boolean save() {
        UniqueTurnoverDTO bean = getBean();
        if (bean == null) {
            return false;
        }
        Predicate<UniqueTurnoverDTO> crudMethod = bean.getId() <= 0 ? crudService::create : crudService::update;
        boolean success = validate() && crudMethod.test(getBean());
        if (success) {
            beanAdapter.setDirty(false);
            onUpdate.accept(bean);
            return true;
        }
        return false;
    }

    @Override
    protected UniqueTurnoverDTO discardChanges() {
        return crudService.readById(Objects.requireNonNull(getBean()).getId());
    }

    @FXML
    private void delete(ActionEvent event) {
        UniqueTurnoverDTO bean = Objects.requireNonNull(getBean());
        String confirmationText = "Den Umsatz \"%s\" wirklich löschen?".formatted(bean.getBiller());
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, confirmationText, ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.filter(ButtonType.YES::equals).isPresent()) {
            crudService.delete(bean.getId());
            setBean(null);
            onUpdate.accept(null);
        }
    }

    @FXML
    private void newUniqueExpenseInformation(ActionEvent event) {
        openUniqueExpenseInformationDetailView(new UniqueTurnoverInformationDTO());
    }

    @FXML
    private void editUniqueExpenseInformation(ActionEvent event) {
        openUniqueExpenseInformationDetailView(expenseInfoTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void deleteUniqueExpenseInformation(ActionEvent event) {
        UniqueTurnoverInformationDTO expInfo = expenseInfoTable.getSelectionModel().getSelectedItem();
        expenseInfoTable.getItems().remove(expInfo);
        updateImportAmountWarning();
    }

    private void updateTurnoverInformation(@Nullable UniqueTurnoverInformationDTO oldVal, UniqueTurnoverInformationDTO newVal) {
        // oldVal nicht null, wenn die Entity in der SubView verworfen wird, da diese per Service neu geladen wird.
        if (oldVal != null) {
            int replaceIndex = expenseInfoTable.getItems().indexOf(oldVal);
            expenseInfoTable.getItems().set(replaceIndex, newVal);
        }
        int index = expenseInfoTable.getItems().indexOf(newVal);
        if (index < 0) {
            expenseInfoTable.getItems().add(newVal);
        }
        expenseInfoTable.refresh();
        updateImportAmountWarning();
    }

    private void openUniqueExpenseInformationDetailView(UniqueTurnoverInformationDTO uniqueTurnover) {
        try {
            var subEntityDetailView = uniqueTurnoverInformationDetailViewFactory.create(this::updateTurnoverInformation);
            stageBuilderProvider.get()
                                .withModality(Modality.APPLICATION_MODAL)
                                .withOwner(Window.getWindows().getFirst())
                                .withFXMLResource(UNIQUE_TURNOVER_INFORMATION_VIEW.toString())
                                .withView(subEntityDetailView)
                                .build()
                                .stage()
                                .show();
            subEntityDetailView.setBean(uniqueTurnover);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, languageManager.get("alert.viewCouldNotBeOpened"));
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @Override
    protected UniqueTurnoverDTO createEmptyEntity() {
        return new UniqueTurnoverDTO();
    }
}
