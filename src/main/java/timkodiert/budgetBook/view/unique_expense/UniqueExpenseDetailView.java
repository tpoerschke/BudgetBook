package timkodiert.budgetBook.view.unique_expense;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.inject.Inject;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import javafx.stage.Stage;
import javafx.stage.Window;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.UniqueTurnover;
import timkodiert.budgetBook.domain.model.UniqueTurnoverInformation;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.ui.control.AutoCompleteTextField;
import timkodiert.budgetBook.util.DoubleCurrencyStringConverter;
import timkodiert.budgetBook.util.StageBuilder;
import timkodiert.budgetBook.view.mdv_base.EntityBaseDetailView;

public class UniqueExpenseDetailView extends EntityBaseDetailView<UniqueTurnover> implements Initializable {

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
    private TableView<UniqueTurnoverInformation> expenseInfoTable;
    @FXML
    private TableColumn<UniqueTurnoverInformation, String> expenseInfoPositionCol, expenseInfoValueCol,
            expenseInfoCategoriesCol;

    @FXML
    private ColumnConstraints rightColumn;

    private ObservableList<UniqueTurnoverInformation> paymentInfoList = FXCollections.observableArrayList();

    private Repository<UniqueTurnoverInformation> expInfoRepository;

    @Inject
    public UniqueExpenseDetailView(Repository<UniqueTurnover> repository,
            Repository<UniqueTurnoverInformation> expInfoRepository) {
        super(() -> new UniqueTurnover(), repository);
        this.expInfoRepository = expInfoRepository;
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

        root.disableProperty().bind(entity.isNull());

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
                StageBuilder.create()
                        .withFXMLResource("/fxml/ImageView.fxml")
                        .withModality(Modality.APPLICATION_MODAL)
                        .withTitle("Beleg / Kassenbon")
                        .withView(new ImageModalView(path))
                        .build()
                        .showAndWait();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        expenseInfoPositionCol
                .setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLabel()));
        expenseInfoValueCol.setCellValueFactory(cellData -> {
            DoubleCurrencyStringConverter converter = new DoubleCurrencyStringConverter();
            return new ReadOnlyStringWrapper(converter.format(cellData.getValue().getValueSigned()));
        });
        expenseInfoCategoriesCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                String.join(", ", cellData.getValue().getCategories().stream().map(Category::getName).toList())));
        expenseInfoTable.setItems(paymentInfoList);

        billerTextField.getAvailableEntries()
                .addAll(repository.findAll().stream().map(UniqueTurnover::getBiller).toList());

        validationMap.put("biller", billerTextField);
        validationMap.put("date", datePicker);
        validationMap.put("paymentInformations", expenseInfoTable);
    }

    private void addNewExpenseInformation(UniqueTurnoverInformation newInfo) {
        newInfo.setExpense(entity.get());
        paymentInfoList.add(newInfo);
    }

    @Override
    protected UniqueTurnover patchEntity(UniqueTurnover entity) {
        entity.setBiller(billerTextField.getText());
        entity.setNote(noteTextArea.getText());
        entity.setPaymentInformations(paymentInfoList);
        entity.setDate(datePicker.getValue());
        entity.setReceiptImagePath(receiptTextField.getText());
        return entity;
    }

    @Override
    protected void patchUi(UniqueTurnover entity) {
        billerTextField.setText(entity.getBiller());
        noteTextArea.setText(entity.getNote());
        datePicker.setValue(entity.getDate());
        paymentInfoList.setAll(entity.getPaymentInformations());
        receiptTextField.setText(entity.getReceiptImagePath());
    }

    @FXML
    private void delete(ActionEvent event) {
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION,
                String.format("Die Ausgabe \"%s\" vom %s wirklich löschen?", this.entity.get().getBiller(),
                        this.entity.get().getDate()),
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.filter(ButtonType.YES::equals).isPresent()) {
            repository.remove(this.entity.get());
            setEntity(null);
            onUpdate.run();
        }
    }

    @FXML
    private void newUniqueExpenseInformation(ActionEvent event) {
        openUniqueExpenseInformationDetailView(Optional.empty());
    }

    @FXML
    private void editUniqueExpenseInformation(ActionEvent event) {
        Optional<UniqueTurnoverInformation> optionalEntity = Optional
                .of(expenseInfoTable.getSelectionModel().getSelectedItem());
        openUniqueExpenseInformationDetailView(optionalEntity);
    }

    @FXML
    private void deleteUniqueExpenseInformation(ActionEvent event) {
        UniqueTurnoverInformation expInfo = expenseInfoTable.getSelectionModel().getSelectedItem();
        expInfoRepository.remove(expInfo);
        paymentInfoList.remove(expInfo);
    }

    private List<String> getUniqueExpenseInformationSuggestions() {
        return repository.findAll().stream().flatMap(exp -> exp.getPaymentInformations().stream())
                         .map(UniqueTurnoverInformation::getLabel).distinct().toList();
    }

    private void openUniqueExpenseInformationDetailView(Optional<UniqueTurnoverInformation> optionalEntity) {
        try {
            Stage stage = StageBuilder.create()
                    .withModality(Modality.APPLICATION_MODAL)
                    .withOwner(Window.getWindows().get(0))
                    .withFXMLResource("/fxml/unique_turnover/Information.fxml")
                    .withView(new UniqueExpenseInformationDetailView(optionalEntity, this::addNewExpenseInformation,
                            this.getUniqueExpenseInformationSuggestions()))
                    .build();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, LanguageManager.get("alert.viewCouldNotBeOpened"));
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
