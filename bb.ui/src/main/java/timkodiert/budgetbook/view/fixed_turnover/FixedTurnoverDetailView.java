package timkodiert.budgetbook.view.fixed_turnover;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Provider;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckListView;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.converter.DoubleCurrencyStringConverter;
import timkodiert.budgetbook.domain.model.AccountTurnover;
import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.ImportRule;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.PaymentInformation;
import timkodiert.budgetbook.domain.model.PaymentType;
import timkodiert.budgetbook.domain.model.TurnoverDirection;
import timkodiert.budgetbook.domain.repository.Repository;
import timkodiert.budgetbook.domain.util.EntityManager;
import timkodiert.budgetbook.table.cell.DateTableCell;
import timkodiert.budgetbook.table.cell.MonthYearTableCell;
import timkodiert.budgetbook.ui.helper.CategoryCheckListHelper;
import timkodiert.budgetbook.util.StageBuilder;
import timkodiert.budgetbook.view.mdv_base.EntityBaseDetailView;

import static timkodiert.budgetbook.view.FxmlResource.FIXED_TURNOVER_INFORMATION_VIEW;

public class FixedTurnoverDetailView extends EntityBaseDetailView<FixedTurnover> implements Initializable {

    @FXML
    private Pane root;

    @FXML
    private TextField positionTextField;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private ComboBox<TurnoverDirection> directionComboBox;

    @FXML
    private CheckListView<Category> categoriesListView;

    @FXML
    private Button addFixedExpenseInformationButton;
    @FXML
    private Button editFixedExpenseInformationButton;
    @FXML
    private Button deleteFixedExpenseInformationButton;
    @FXML
    private TableView<PaymentInformation> expenseInfoTable;
    @FXML
    private TableColumn<PaymentInformation, String> expenseInfoValueCol, expenseInfoTypeCol;
    @FXML
    private TableColumn<PaymentInformation, MonthYear> expenseInfoStartCol, expenseInfoEndCol;

    // Importe
    @FXML
    private CheckBox importActiveCheckbox;
    @FXML
    private TextField importReceiverTextField;
    @FXML
    private TextField importReferenceTextField;
    @FXML
    private TableView<AccountTurnover> importsTable;
    @FXML
    private TableColumn<AccountTurnover, LocalDate> importsDateCol;
    @FXML
    private TableColumn<AccountTurnover, String> importsReceiverCol, importsReferenceCol, importsAmountCol;

    private final ObservableList<PaymentInformation> paymentInfoList = FXCollections.observableArrayList();
    private final Repository<PaymentInformation> expInfoRepository;
    private final FixedTurnoverInformationDetailViewFactory fixedTurnoverInformationDetailViewFactory;
    private final Provider<StageBuilder> stageBuilderProvider;

    private CategoryCheckListHelper categoryCheckListHelper;

    @Inject
    public FixedTurnoverDetailView(Repository<FixedTurnover> repository,
                                   Repository<PaymentInformation> expInfoRepository,
                                   EntityManager entityManager,
                                   FixedTurnoverInformationDetailViewFactory fixedTurnoverInformationDetailViewFactory,
                                   Provider<StageBuilder> stageBuilderProvider) {
        super(FixedTurnover::new, repository, entityManager);
        this.expInfoRepository = expInfoRepository;
        this.fixedTurnoverInformationDetailViewFactory = fixedTurnoverInformationDetailViewFactory;
        this.stageBuilderProvider = stageBuilderProvider;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addFixedExpenseInformationButton.setGraphic(new FontIcon(BootstrapIcons.PLUS));
        editFixedExpenseInformationButton.setGraphic(new FontIcon(BootstrapIcons.PENCIL));
        deleteFixedExpenseInformationButton.setGraphic(new FontIcon(BootstrapIcons.TRASH));
        editFixedExpenseInformationButton.disableProperty()
                .bind(expenseInfoTable.getSelectionModel().selectedItemProperty().isNull());
        deleteFixedExpenseInformationButton.disableProperty()
                .bind(expenseInfoTable.getSelectionModel().selectedItemProperty().isNull());
        root.disableProperty().bind(entity.isNull());
        List<Category> categories = entityManager.findAll(Category.class);
        categoryCheckListHelper = new CategoryCheckListHelper(categoriesListView, categories);
        directionComboBox.getItems().setAll(TurnoverDirection.values());
        directionComboBox.setConverter(Converters.get(TurnoverDirection.class));

        // Tabelle der Unterelemente
        StringConverter<PaymentType> paymentTypeConverter = Converters.get(PaymentType.class);
        expenseInfoTypeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(paymentTypeConverter.toString(cellData.getValue().getType())));
        expenseInfoValueCol.setCellValueFactory(cellData -> {
            DoubleCurrencyStringConverter converter = new DoubleCurrencyStringConverter();
            return new ReadOnlyStringWrapper(converter.format(cellData.getValue().getValue()));
        });
        expenseInfoStartCol.setCellValueFactory(cellData -> new SimpleObjectProperty<MonthYear>(cellData.getValue().getStart()));
        expenseInfoEndCol.setCellValueFactory(cellData -> new SimpleObjectProperty<MonthYear>(cellData.getValue().getEnd()));
        expenseInfoStartCol.setCellFactory(col -> new MonthYearTableCell<>());
        expenseInfoEndCol.setCellFactory(col -> new MonthYearTableCell<>());

        expenseInfoTable.setItems(paymentInfoList);

        // Importe
        importReceiverTextField.disableProperty().bind(importActiveCheckbox.selectedProperty().not());
        importReferenceTextField.disableProperty().bind(importActiveCheckbox.selectedProperty().not());

        importsDateCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        importsDateCol.setCellFactory(col -> new DateTableCell<>());
        importsReceiverCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReceiver()));
        importsReferenceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReference()));
        importsAmountCol.setCellValueFactory(cellData -> {
            DoubleCurrencyStringConverter converter = new DoubleCurrencyStringConverter();
            return new ReadOnlyStringWrapper(converter.format(cellData.getValue().getAmount()));
        });

        // Validierung initialisieren
        validationMap.put("position", positionTextField);
    }

    @FXML
    private void deleteExpense(ActionEvent event) {
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, "Die Ausgabe \"" + this.entity.get().getPosition() + "\" wirklich löschen?",
                ButtonType.YES,
                ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.filter(ButtonType.YES::equals).isPresent()) {
            entityManager.remove(this.entity.get());
            setEntity(null);
            onUpdate.accept(null);
        }
    }

    @Override
    protected FixedTurnover patchEntity(FixedTurnover entity, boolean isSaving) {
        entity.setPosition(positionTextField.getText());
        entity.setNote(noteTextArea.getText());
        entity.setDirection(directionComboBox.getSelectionModel().getSelectedItem());
        entity.getCategories().clear();
        entity.getCategories().addAll(categoryCheckListHelper.getCheckedCategories());
        entity.getPaymentInformations().clear();
        entity.getPaymentInformations().addAll(paymentInfoList);
        createImportRuleIfNotExists(entity);
        entity.getImportRule().setActive(importActiveCheckbox.isSelected());
        entity.getImportRule().setReceiverContains(importReceiverTextField.getText());
        entity.getImportRule().setReferenceContains(importReferenceTextField.getText());

        if (!isSaving) {
            return entity;
        }
        // Einige Verknüpfungen sollen nicht vorgenommen werden, wenn bspw. eine (leere) Entity
        // zur Validierung befüllt wird, die gar nicht persistiert wird.
        entity.getCategories().forEach(category -> {
            if (!category.getFixedExpenses().contains(entity)) {
                category.getFixedExpenses().add(entity);
            }
        });
        return entity;
    }

    @Override
    protected void patchUi(FixedTurnover entity) {
        positionTextField.setText(entity.getPosition());
        noteTextArea.setText(entity.getNote());
        directionComboBox.getSelectionModel().select(entity.getDirection());
        // Kategorien der Ausgabe abhacken
        categoryCheckListHelper.checkCategories(entity);
        paymentInfoList.setAll(entity.getPaymentInformations());
        // Importe
        createImportRuleIfNotExists(entity);
        importActiveCheckbox.setSelected(entity.getImportRule().isActive());
        importReceiverTextField.setText(entity.getImportRule().getReceiverContains());
        importReferenceTextField.setText(entity.getImportRule().getReferenceContains());
        importsTable.getItems().setAll(entity.getAccountTurnover());
    }

    // TODO 01.11.23: Ausbauen, wenn sichergestellt ist, dass ImportRules nicht null sein können
    private void createImportRuleIfNotExists(FixedTurnover expense) {
        if (expense.getImportRule() == null) {
            expense.setImportRule(new ImportRule(expense));
        }
    }

    @FXML
    private void newExpenseInformation(ActionEvent event) {
        openUniqueExpenseInformationDetailView(Optional.empty());
    }

    @FXML
    private void editExpenseInformation(ActionEvent event) {
        Optional<PaymentInformation> optionalEntity = Optional.of(expenseInfoTable.getSelectionModel().getSelectedItem());
        openUniqueExpenseInformationDetailView(optionalEntity);
    }

    @FXML
    private void deleteExpenseInformation(ActionEvent event) {
        PaymentInformation expInfo = expenseInfoTable.getSelectionModel().getSelectedItem();
        expInfoRepository.remove(expInfo);
        paymentInfoList.remove(expInfo);
    }

    private void updateExpenseInformation(PaymentInformation expInfo) {
        if (!paymentInfoList.contains(expInfo)) {
            paymentInfoList.add(expInfo);
            expInfo.setExpense(entity.get());
        }
        expenseInfoTable.refresh();
    }

    private void openUniqueExpenseInformationDetailView(Optional<PaymentInformation> optionalEntity) {
        try {
            var subDetailView = fixedTurnoverInformationDetailViewFactory.create(PaymentInformation::new, this::updateExpenseInformation);
            stageBuilderProvider.get()
                                .withModality(Modality.APPLICATION_MODAL)
                                .withOwner(Window.getWindows().get(0))
                                .withFXMLResource(FIXED_TURNOVER_INFORMATION_VIEW.toString())
                                .withView(subDetailView)
                                .build()
                                .stage()
                                .show();
            subDetailView.setEntity(optionalEntity.orElse(new PaymentInformation()));
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
