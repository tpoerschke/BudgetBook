package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.PaymentInformation;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.view.widget.EditPaymentInformationWidget;
import timkodiert.budgetBook.view.widget.factory.EditPaymentInformationWidgetFactory;

@RequiredArgsConstructor
public class EditExpenseView implements View, Initializable {

    private final FixedExpense expense;

    @FXML
    private TextField positionTextField;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private TreeView<Category> categoriesTreeView;
    private List<CheckBoxTreeItem<Category>> allTreeItems;

    @FXML
    private HBox widgetContainer;
    @FXML
    private VBox payInfoContainer;

    private Repository<FixedExpense> repository;
    private EditPaymentInformationWidgetFactory editPaymentInformationWidgetFactory;

    private List<EditPaymentInformationWidget> editPayInfoWidgets;

    @AssistedInject
    public EditExpenseView(Repository<FixedExpense> repository, EditPaymentInformationWidgetFactory editPaymentInformationWidgetFactory, @Assisted FixedExpense expense) {
        this.repository = repository;
        this.editPaymentInformationWidgetFactory = editPaymentInformationWidgetFactory;
        this.expense = expense;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positionTextField.setText(expense.getPosition());
        noteTextArea.setText(expense.getNote());

        List<Category> categories = EntityManager.getInstance().findAll(Category.class);
        // TODO: Refactoring, diese Code-Zeilen kommen häufiger vor
        allTreeItems = categories.stream().map(Category::asTreeItem).toList();
        List<? extends TreeItem<Category>> roots = allTreeItems.stream().filter(ti -> ti.getValue().getParent() == null).toList();
        TreeItem<Category> root = new TreeItem<>(new Category("ROOT"));
        root.getChildren().addAll(roots);
        categoriesTreeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        categoriesTreeView.setRoot(root);

        // Kategorien der Ausgabe abhacken
        allTreeItems.forEach(ti -> {
            if(expense.getCategories().contains(ti.getValue())) {
                ti.setSelected(true);
            }
        });

        // Widgets zur Bearbeitung der PaymentInformations initialisieren
        editPayInfoWidgets = expense.getPaymentInformations().stream().map(payInfo -> editPaymentInformationWidgetFactory.create(payInfoContainer, payInfo)).collect(Collectors.toList());
    }

    @FXML 
    private void saveExpense(ActionEvent event) {

        // Eingetragene Werte übernehmen
        this.expense.setPosition(positionTextField.getText());
        this.expense.setNote(noteTextArea.getText());

        // Speichern
        editPayInfoWidgets.forEach(widget -> {
            widget.persistUpdate();
            // eine neue PaymentInformation muss der Expense hinzugefügt werden
            PaymentInformation payInfo = widget.getPayInfo();
            if(!expense.getPaymentInformations().contains(payInfo)) {
                expense.getPaymentInformations().add(payInfo);
            }
        });
        List<Category> categories = allTreeItems.stream().filter(CheckBoxTreeItem::isSelected).map(TreeItem::getValue).toList();
        this.expense.getCategories().clear();
        this.expense.getCategories().addAll(categories);

        this.repository.persist(expense);
        // Fenster schließen
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void deleteExpense(ActionEvent event) {
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION, "Die Ausgabe \"" + this.expense.getPosition() + "\" wirklich löschen?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if(result.filter(ButtonType.YES::equals).isPresent()) {
            EntityManager.getInstance().remove(this.expense);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void addNewPaymentInformation() {
        PaymentInformation newPayInfo = new PaymentInformation();
        newPayInfo.setExpense(expense);
        EditPaymentInformationWidget widget = editPaymentInformationWidgetFactory.create(payInfoContainer, newPayInfo);
        editPayInfoWidgets.add(widget);
    }
}
