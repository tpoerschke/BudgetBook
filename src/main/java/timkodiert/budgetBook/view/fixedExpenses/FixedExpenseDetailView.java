package timkodiert.budgetBook.view.fixedExpenses;

import static timkodiert.budgetBook.util.CategoryTreeHelper.from;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.inject.Inject;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.MonthYear;
import timkodiert.budgetBook.domain.model.PaymentInformation;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.table.cell.MonthYearTableCell;
import timkodiert.budgetBook.util.CategoryTreeHelper;
import timkodiert.budgetBook.util.DoubleCurrencyStringConverter;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.view.BaseDetailView;

public class FixedExpenseDetailView extends BaseDetailView<FixedExpense> implements Initializable {

    @FXML
    private Pane root;

    @FXML
    private TextField positionTextField;

    @FXML
    private TextArea noteTextArea;

    @FXML
    private TreeView<Category> categoriesTreeView;
    private CategoryTreeHelper categoryTreeHelper;

    // @FXML
    // private Button addUniqueExpenseInformationButton;
    // @FXML
    // private Button editUniqueExpenseInformationButton;
    // @FXML
    // private Button deleteUniqueExpenseInformationButton;
    @FXML
    private TableView<PaymentInformation> expenseInfoTable;
    @FXML
    private TableColumn<PaymentInformation, String> expenseInfoValueCol, expenseInfoTypeCol;
    @FXML
    private TableColumn<PaymentInformation, MonthYear> expenseInfoStartCol, expenseInfoEndCol;

    private ObservableList<PaymentInformation> paymentInfoList = FXCollections.observableArrayList();
    private Repository<PaymentInformation> expInfoRepository;

    @Inject
    public FixedExpenseDetailView(Repository<FixedExpense> repository, Repository<PaymentInformation> expInfoRepository) {
        super(() -> new FixedExpense(), repository);
        this.expInfoRepository = expInfoRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.disableProperty().bind(entity.isNull());
        List<Category> categories = EntityManager.getInstance().findAll(Category.class);
        categoryTreeHelper = from(categoriesTreeView, categories);

        // Tabelle der Unterelemente
        expenseInfoTypeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getType().getType()));
        expenseInfoValueCol.setCellValueFactory(cellData -> {
            DoubleCurrencyStringConverter converter = new DoubleCurrencyStringConverter();
            return new ReadOnlyStringWrapper(converter.format(cellData.getValue().getValue()));
        });
        expenseInfoStartCol.setCellValueFactory(cellData -> new SimpleObjectProperty<MonthYear>(cellData.getValue().getStart()));
        expenseInfoEndCol.setCellValueFactory(cellData -> new SimpleObjectProperty<MonthYear>(cellData.getValue().getEnd()));
        expenseInfoStartCol.setCellFactory(col -> new MonthYearTableCell<>());
        expenseInfoEndCol.setCellFactory(col -> new MonthYearTableCell<>());

        expenseInfoTable.setItems(paymentInfoList);
    }

    @Override
    public boolean save() {
        // Speichern
        // editPayInfoWidgets.forEach(widget -> {
        //     widget.persistUpdate();
        //     // eine neue PaymentInformation muss der Expense hinzugefügt werden
        //     PaymentInformation payInfo = widget.getPayInfo();
        //     if (!entity.get().getPaymentInformations().contains(payInfo)) {
        //         entity.get().getPaymentInformations().add(payInfo);
        //     }
        //     if (widget.isDeleted()) {
        //         entity.get().getPaymentInformations().remove(payInfo);
        //     }
        // });
        this.repository.persist(entity.get());
        EntityManager.getInstance().refresh(entity.get());
        return true;
    }

    @FXML
    private void deleteExpense(ActionEvent event) {
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, "Die Ausgabe \"" + this.entity.get().getPosition() + "\" wirklich löschen?",
                ButtonType.YES,
                ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.filter(ButtonType.YES::equals).isPresent()) {
            EntityManager.getInstance().remove(this.entity.get());
            setEntity(null);
            onUpdate.run();
        }
    }

    // @FXML
    // private void addNewPaymentInformation() {
    //     PaymentInformation newPayInfo = new PaymentInformation();
    //     newPayInfo.setExpense(entity.get());
    //     EditPaymentInformationWidget widget = editPaymentInformationWidgetFactory.create(payInfoContainer, newPayInfo);
    //     editPayInfoWidgets.add(widget);
    // }

    @Override
    public String getFxmlLocation() {
        return "/fxml/EditExpense.fxml";
    }

    @Override
    protected FixedExpense patchEntity(FixedExpense entity) {
        entity.setPosition(positionTextField.getText());
        entity.setNote(noteTextArea.getText());
        entity.getCategories().clear();
        entity.getCategories().addAll(categoryTreeHelper.getSelectedCategories());
        entity.getPaymentInformations().clear();
        entity.getPaymentInformations().addAll(paymentInfoList);
        return entity;
    }

    @Override
    protected void patchUi(FixedExpense entity) {
        positionTextField.setText(entity.getPosition());
        noteTextArea.setText(entity.getNote());
        // Kategorien der Ausgabe abhacken
        categoryTreeHelper.selectCategories(entity);
        paymentInfoList.setAll(entity.getPaymentInformations());
        // Widgets zur Bearbeitung der PaymentInformations initialisieren
        // payInfoContainer.getChildren().clear();
        // editPayInfoWidgets = entity.getPaymentInformations().stream().map(payInfo -> editPaymentInformationWidgetFactory.create(payInfoContainer, payInfo))
        //         .collect(Collectors.toList());
    }
}
