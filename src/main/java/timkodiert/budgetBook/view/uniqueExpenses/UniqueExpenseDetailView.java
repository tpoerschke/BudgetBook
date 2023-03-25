package timkodiert.budgetBook.view.uniqueExpenses;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.inject.Inject;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.model.UniqueExpenseInformation;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.util.DoubleCurrencyStringConverter;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.util.StageBuilder;
import timkodiert.budgetBook.view.View;

public class UniqueExpenseDetailView implements View, Initializable {

    @Setter
    private Runnable onUpdate;

    @FXML
    private Pane root;
    @FXML
    private TextField billerTextField;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private DatePicker datePicker;

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

    private Repository<UniqueExpense> repository;
    private ObjectProperty<UniqueExpense> expense = new SimpleObjectProperty<>();
    private ObservableList<UniqueExpenseInformation> paymentInfoList = FXCollections.observableArrayList();

    @Inject
    public UniqueExpenseDetailView(Repository<UniqueExpense> repository) {
        this.repository = repository;
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

        root.disableProperty().bind(expense.isNull());

        expenseInfoPositionCol
                .setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getLabel()));
        expenseInfoValueCol.setCellValueFactory(cellData -> {
            DoubleCurrencyStringConverter converter = new DoubleCurrencyStringConverter();
            return new ReadOnlyStringWrapper(converter.format(cellData.getValue().getValue()));
        });
        expenseInfoCategoriesCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                String.join(", ", cellData.getValue().getCategories().stream().map(Category::getName).toList())));
        expenseInfoTable.setItems(paymentInfoList);
    }

    public void setBean(UniqueExpense uniqueExpense) {
        this.expense.set(uniqueExpense);

        if (uniqueExpense == null) {
            return;
        }

        billerTextField.setText(uniqueExpense.getBiller());
        noteTextArea.setText(uniqueExpense.getNote());
        datePicker.setValue(uniqueExpense.getDate());
        paymentInfoList.setAll(uniqueExpense.getPaymentInformations());
    }

    private void addNewExpenseInformation(UniqueExpenseInformation newInfo) {
        newInfo.setExpense(expense.get());
        paymentInfoList.add(newInfo);
    }

    @FXML
    private void save(ActionEvent event) {
        UniqueExpense exp = this.expense.get();
        exp.setBiller(billerTextField.getText());
        exp.setNote(noteTextArea.getText());
        exp.setPaymentInformations(paymentInfoList);
        exp.setDate(datePicker.getValue());
        repository.persist(exp);
        EntityManager.getInstance().refresh(exp);
        onUpdate.run();
    }

    @FXML
    private void discardChanges(ActionEvent event) {
        // Durch erneutes Setzen der Expense werden
        // die Änderungen verworfen.
        setBean(expense.get());
    }

    @FXML
    private void delete(ActionEvent event) {
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION,
                String.format("Die Ausgabe \"%s\" vom %s wirklich löschen?", this.expense.get().getBiller(),
                        this.expense.get().getDate()),
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.filter(ButtonType.YES::equals).isPresent()) {
            repository.remove(this.expense.get());
            setBean(null);
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
        paymentInfoList.remove(expenseInfoTable.getSelectionModel().getSelectedItem());
    }

    private void openUniqueExpenseInformationDetailView(Optional<UniqueExpenseInformation> optionalEntity) {
        try {
            Stage stage = StageBuilder.create()
                    .withModality(Modality.APPLICATION_MODAL)
                    .withOwner(Window.getWindows().get(0))
                    .withFXMLResource("/fxml/UniqueExpenses/Information.fxml")
                    .withView(new UniqueExpenseInformationDetailView(optionalEntity, this::addNewExpenseInformation))
                    .build();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
