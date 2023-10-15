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
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.model.UniqueExpenseInformation;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.ui.control.AutoCompleteTextField;
import timkodiert.budgetBook.util.DoubleCurrencyStringConverter;
import timkodiert.budgetBook.util.StageBuilder;
import timkodiert.budgetBook.view.mdv_base.EntityBaseDetailView;

public class UniqueExpenseDetailView extends EntityBaseDetailView<UniqueExpense> implements Initializable {

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
    private TableView<UniqueExpenseInformation> expenseInfoTable;
    @FXML
    private TableColumn<UniqueExpenseInformation, String> expenseInfoPositionCol, expenseInfoValueCol,
            expenseInfoCategoriesCol;

    @FXML
    private ColumnConstraints rightColumn;

    private ObservableList<UniqueExpenseInformation> paymentInfoList = FXCollections.observableArrayList();

    private Repository<UniqueExpenseInformation> expInfoRepository;

    @Inject
    public UniqueExpenseDetailView(Repository<UniqueExpense> repository,
            Repository<UniqueExpenseInformation> expInfoRepository) {
        super(() -> new UniqueExpense(), repository);
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
            return new ReadOnlyStringWrapper(converter.format(cellData.getValue().getValue()));
        });
        expenseInfoCategoriesCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                String.join(", ", cellData.getValue().getCategories().stream().map(Category::getName).toList())));
        expenseInfoTable.setItems(paymentInfoList);

        billerTextField.getAvailableEntries()
                .addAll(repository.findAll().stream().map(UniqueExpense::getBiller).toList());

        validationMap.put("biller", billerTextField);
        validationMap.put("date", datePicker);
        validationMap.put("paymentInformations", expenseInfoTable);
    }

    private void addNewExpenseInformation(UniqueExpenseInformation newInfo) {
        newInfo.setExpense(entity.get());
        paymentInfoList.add(newInfo);
    }

    @Override
    protected UniqueExpense patchEntity(UniqueExpense entity) {
        entity.setBiller(billerTextField.getText());
        entity.setNote(noteTextArea.getText());
        entity.setPaymentInformations(paymentInfoList);
        entity.setDate(datePicker.getValue());
        entity.setReceiptImagePath(receiptTextField.getText());
        return entity;
    }

    @Override
    protected void patchUi(UniqueExpense entity) {
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
        Optional<UniqueExpenseInformation> optionalEntity = Optional
                .of(expenseInfoTable.getSelectionModel().getSelectedItem());
        openUniqueExpenseInformationDetailView(optionalEntity);
    }

    @FXML
    private void deleteUniqueExpenseInformation(ActionEvent event) {
        UniqueExpenseInformation expInfo = expenseInfoTable.getSelectionModel().getSelectedItem();
        expInfoRepository.remove(expInfo);
        paymentInfoList.remove(expInfo);
    }

    private List<String> getUniqueExpenseInformationSuggestions() {
        return repository.findAll().stream().flatMap(exp -> exp.getPaymentInformations().stream())
                .map(UniqueExpenseInformation::getLabel).distinct().toList();
    }

    private void openUniqueExpenseInformationDetailView(Optional<UniqueExpenseInformation> optionalEntity) {
        try {
            Stage stage = StageBuilder.create()
                    .withModality(Modality.APPLICATION_MODAL)
                    .withOwner(Window.getWindows().get(0))
                    .withFXMLResource("/fxml/UniqueExpenses/Information.fxml")
                    .withView(new UniqueExpenseInformationDetailView(optionalEntity, this::addNewExpenseInformation,
                            this.getUniqueExpenseInformationSuggestions()))
                    .build();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
