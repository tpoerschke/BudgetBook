package timkodiert.budgetbook.view.fixed_turnover;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.inject.Provider;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckListView;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.converter.DoubleCurrencyStringConverter;
import timkodiert.budgetbook.converter.ReferenceStringConverter;
import timkodiert.budgetbook.dialog.StackTraceAlert;
import timkodiert.budgetbook.domain.AccountTurnoverDTO;
import timkodiert.budgetbook.domain.CategoryCrudService;
import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.FixedTurnoverCrudService;
import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.PaymentInformationDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.PaymentType;
import timkodiert.budgetbook.domain.model.TurnoverDirection;
import timkodiert.budgetbook.table.cell.DateTableCell;
import timkodiert.budgetbook.table.cell.MonthYearTableCell;
import timkodiert.budgetbook.ui.helper.Bind;
import timkodiert.budgetbook.util.StageBuilder;
import timkodiert.budgetbook.view.mdv_base.EntityBaseDetailView;

import static timkodiert.budgetbook.view.FxmlResource.FIXED_TURNOVER_INFORMATION_VIEW;

public class FixedTurnoverDetailView extends EntityBaseDetailView<FixedTurnoverDTO> implements Initializable {

    @FXML
    private Pane root;

    @FXML
    private TextField positionTextField;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private ComboBox<TurnoverDirection> directionComboBox;
    @FXML
    private CheckBox payInfoFutureOnlyCheckBox;

    @FXML
    private CheckListView<Reference<CategoryDTO>> categoriesListView;

    @FXML
    private Button addFixedExpenseInformationButton;
    @FXML
    private Button editFixedExpenseInformationButton;
    @FXML
    private Button deleteFixedExpenseInformationButton;
    @FXML
    private TableView<PaymentInformationDTO> paymentInformationTableView;
    @FXML
    private TableColumn<PaymentInformationDTO, String> expenseInfoValueCol;
    @FXML
    private TableColumn<PaymentInformationDTO, String> expenseInfoTypeCol;
    @FXML
    private TableColumn<PaymentInformationDTO, MonthYear> expenseInfoStartCol;
    @FXML
    private TableColumn<PaymentInformationDTO, MonthYear> expenseInfoEndCol;

    // Importe
    @FXML
    private TableView<AccountTurnoverDTO> importsTable;
    @FXML
    private TableColumn<AccountTurnoverDTO, LocalDate> importsDateCol;
    @FXML
    private TableColumn<AccountTurnoverDTO, String> importsReceiverCol;
    @FXML
    private TableColumn<AccountTurnoverDTO, String> importsReferenceCol;
    @FXML
    private TableColumn<AccountTurnoverDTO, String> importsAmountCol;

    private final FixedTurnoverInformationDetailViewFactory fixedTurnoverInformationDetailViewFactory;
    private final Provider<StageBuilder> stageBuilderProvider;
    private final CategoryCrudService categoryCrudService;
    private final FixedTurnoverCrudService crudService;

    @Inject
    public FixedTurnoverDetailView(FixedTurnoverInformationDetailViewFactory fixedTurnoverInformationDetailViewFactory,
                                   Provider<StageBuilder> stageBuilderProvider,
                                   CategoryCrudService categoryCrudService,
                                   FixedTurnoverCrudService crudService) {
        this.fixedTurnoverInformationDetailViewFactory = fixedTurnoverInformationDetailViewFactory;
        this.stageBuilderProvider = stageBuilderProvider;
        this.categoryCrudService = categoryCrudService;
        this.crudService = crudService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addFixedExpenseInformationButton.setGraphic(new FontIcon(BootstrapIcons.PLUS));
        editFixedExpenseInformationButton.setGraphic(new FontIcon(BootstrapIcons.PENCIL));
        deleteFixedExpenseInformationButton.setGraphic(new FontIcon(BootstrapIcons.TRASH));
        editFixedExpenseInformationButton.disableProperty()
                                         .bind(paymentInformationTableView.getSelectionModel().selectedItemProperty().isNull());
        deleteFixedExpenseInformationButton.disableProperty()
                                           .bind(paymentInformationTableView.getSelectionModel().selectedItemProperty().isNull());

        root.disableProperty().bind(beanAdapter.isEmpty());

        // Tabelle der Unterelemente
        StringConverter<PaymentType> paymentTypeConverter = Converters.get(PaymentType.class);
        expenseInfoTypeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(paymentTypeConverter.toString(cellData.getValue().getType())));
        expenseInfoValueCol.setCellValueFactory(cellData -> {
            DoubleCurrencyStringConverter converter = new DoubleCurrencyStringConverter();
            return new ReadOnlyStringWrapper(converter.format(cellData.getValue().getValue()));
        });
        expenseInfoStartCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStart()));
        expenseInfoEndCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getEnd()));
        expenseInfoStartCol.setCellFactory(col -> new MonthYearTableCell<>());
        expenseInfoEndCol.setCellFactory(col -> new MonthYearTableCell<>());

        // Importe
        importsDateCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        importsDateCol.setCellFactory(col -> new DateTableCell<>());
        importsReceiverCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReceiver()));
        importsReferenceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReference()));
        importsAmountCol.setCellValueFactory(cellData -> {
            DoubleCurrencyStringConverter converter = new DoubleCurrencyStringConverter();
            return new ReadOnlyStringWrapper(converter.format(cellData.getValue().getAmount()));
        });
        importsDateCol.setSortType(TableColumn.SortType.DESCENDING);
        importsTable.getSortOrder().add(importsDateCol);

        // Kategorien
        categoriesListView.setCellFactory(lv -> new CheckBoxListCell<>(item -> categoriesListView.getItemBooleanProperty(item), new ReferenceStringConverter<>()));
        List<Reference<CategoryDTO>> categories = categoryCrudService.readAll().stream().map(c -> new Reference<>(CategoryDTO.class, c.getId(), c.getName())).toList();
        categoriesListView.getItems().setAll(categories);

        // Bindings
        positionTextField.textProperty().bindBidirectional(beanAdapter.getProperty(FixedTurnoverDTO::getPosition, FixedTurnoverDTO::setPosition));
        noteTextArea.textProperty().bindBidirectional(beanAdapter.getProperty(FixedTurnoverDTO::getNote, FixedTurnoverDTO::setNote));
        Bind.comboBox(directionComboBox,
                      beanAdapter.getProperty(FixedTurnoverDTO::getDirection, FixedTurnoverDTO::setDirection),
                      Arrays.asList(TurnoverDirection.values()),
                      TurnoverDirection.class);
        Bindings.bindContentBidirectional(paymentInformationTableView.getItems(),
                                          beanAdapter.getListProperty(FixedTurnoverDTO::getPaymentInformations, FixedTurnoverDTO::setPaymentInformations));
        Bindings.bindContent(importsTable.getItems(),
                             new SortedList<>(beanAdapter.getListProperty(FixedTurnoverDTO::getAccountTurnover, FixedTurnoverDTO::setAccountTurnover),
                                              Comparator.comparing(AccountTurnoverDTO::getDate).reversed()));
        payInfoFutureOnlyCheckBox.selectedProperty().bindBidirectional(beanAdapter.getProperty(FixedTurnoverDTO::isUsePaymentInfoForFutureOnly,
                                                                                               FixedTurnoverDTO::setUsePaymentInfoForFutureOnly));

        // Validierung initialisieren
        validationMap.put("position", positionTextField);
    }

    @Override
    protected void beanSet() {
        categoriesListView.getCheckModel().clearChecks();
        FixedTurnoverDTO bean = beanAdapter.getBean();
        bean.getCategories().forEach(cat -> categoriesListView.getCheckModel().check(cat));
    }

    @Override
    public boolean save() {
        FixedTurnoverDTO bean = getBean();
        if (bean == null) {
            return false;
        }
        beanAdapter.getBean().setCategories(categoriesListView.getCheckModel().getCheckedItems());
        Predicate<FixedTurnoverDTO> crudMethod = bean.getId() <= 0 ? crudService::create : crudService::update;
        boolean success = validate() && crudMethod.test(getBean());
        if (success) {
            beanAdapter.setDirty(false);
            onUpdate.accept(bean);
            return true;
        }
        return false;
    }

    @Override
    protected FixedTurnoverDTO discardChanges() {
        return crudService.readById(getBean().getId());
    }

    @FXML
    private void deleteExpense(ActionEvent event) {
        FixedTurnoverDTO bean = Objects.requireNonNull(getBean());
        String confirmationText = "Den Umsatz \"%s\" wirklich löschen?".formatted(bean.getPosition());
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, confirmationText, ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.filter(ButtonType.YES::equals).isPresent()) {
            crudService.delete(bean.getId());
            setBean(null);
            onUpdate.accept(null);
        }
    }

    @FXML
    private void newExpenseInformation(ActionEvent event) {
        openTurnoverInformationDetailView(new PaymentInformationDTO());
    }

    @FXML
    private void editExpenseInformation(ActionEvent event) {
        openTurnoverInformationDetailView(paymentInformationTableView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void deleteExpenseInformation(ActionEvent event) {
        PaymentInformationDTO payInfoToRemove = paymentInformationTableView.getSelectionModel().getSelectedItem();
        paymentInformationTableView.getItems().remove(payInfoToRemove);
    }

    private void updateExpenseInformation(@Nullable PaymentInformationDTO oldVal, PaymentInformationDTO newVal) {
        // oldVal nicht null, wenn die Entity in der SubView verworfen wird, da diese per Service neu geladen wird.
        if (oldVal != null) {
            int replaceIndex = paymentInformationTableView.getItems().indexOf(oldVal);
            paymentInformationTableView.getItems().set(replaceIndex, newVal);
        }
        int index = paymentInformationTableView.getItems().indexOf(newVal);
        if (index < 0) {
            paymentInformationTableView.getItems().add(newVal);
        }
        paymentInformationTableView.refresh();
    }

    private void openTurnoverInformationDetailView(PaymentInformationDTO payInfo) {
        try {
            var subDetailView = fixedTurnoverInformationDetailViewFactory.create(this::updateExpenseInformation);
            stageBuilderProvider.get()
                                .withModality(Modality.APPLICATION_MODAL)
                                .withOwner(Window.getWindows().getFirst())
                                .withFXMLResource(FIXED_TURNOVER_INFORMATION_VIEW.toString())
                                .withView(subDetailView)
                                .build()
                                .stage()
                                .show();
            subDetailView.setBean(payInfo);
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceAlert.of("Ansicht konnte nicht geöffnet werden!", e).showAndWait();
        }
    }

    @Override
    protected FixedTurnoverDTO createEmptyEntity() {
        return new FixedTurnoverDTO();
    }
}
