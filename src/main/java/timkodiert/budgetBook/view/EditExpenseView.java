package timkodiert.budgetBook.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.PaymentInformation;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.util.EntityManager;

@RequiredArgsConstructor
public class EditExpenseView implements View, Initializable {

    private static int CURRENT_YEAR = LocalDate.now().getYear();

    private final FixedExpense expense;

    @FXML
    private ComboBox<Integer> displayYearComboBox;

    @FXML
    private TextField positionTextField;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private TreeView<Category> categoriesTreeView;
    private List<CheckBoxTreeItem<Category>> allTreeItems;

    @FXML
    private TextField month1TextField, month2TextField, month3TextField, month4TextField, month5TextField, 
        month6TextField, month7TextField, month8TextField, month9TextField, month10TextField, month11TextField, month12TextField;

    private List<TextField> monthTextFields;

    private Map<Integer, List<Double>> newPayments = new HashMap<>();

    private Repository<FixedExpense> repository;

    @AssistedInject
    public EditExpenseView(Repository<FixedExpense> repository, @Assisted FixedExpense expense) {
        this.repository = repository;
        this.expense = expense;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        positionTextField.setText(expense.getPosition());
        noteTextArea.setText(expense.getNote());

        monthTextFields = List.of(month1TextField, month2TextField, month3TextField, month4TextField, month5TextField, 
            month6TextField, month7TextField, month8TextField, month9TextField, month10TextField, month11TextField, month12TextField);

        displayYearComboBox.getItems().addAll(IntStream.rangeClosed(CURRENT_YEAR - 5, CURRENT_YEAR + 1).boxed().toList());
        displayYearComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldYear, Integer newYear) -> {
            // Neue Werte zwischenspeichern. Werden übernommen, wenn der Nutzer die Ausgabe speichert
            if(oldYear != null) {
                newPayments.put(oldYear, monthTextFields.stream().map(tf -> Double.valueOf(tf.getText())).toList());
            }
            // Textfields mit den werden des ausgewählten Jahres befüllen.
            IntStream.rangeClosed(1, 12).forEach(i -> {
                monthTextFields.get(i-1).setText("" + expense.getValueFor(newYear, i));
            });
        });
        displayYearComboBox.getSelectionModel().select(Integer.valueOf(CURRENT_YEAR));

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
    }

    @FXML 
    private void saveExpense(ActionEvent event) {

        // Eingetragene Werte übernehmen
        this.expense.setPosition(positionTextField.getText());
        this.expense.setNote(noteTextArea.getText());
        newPayments.put(displayYearComboBox.getSelectionModel().getSelectedItem(), monthTextFields.stream().map(tf -> Double.valueOf(tf.getText())).toList());

        // Speichern
        for(PaymentInformation payInfo : this.expense.getPaymentInformations()) {
            List<Double> newPaymentValues = this.newPayments.get(payInfo.getYear());
            if(newPaymentValues != null) {
                payInfo.getPayments().clear();
                IntStream.rangeClosed(1, 12).forEach(i -> {
                    if(newPaymentValues.get(i-1) != 0.0) {
                        payInfo.getPayments().put(i, newPaymentValues.get(i-1));
                    }
                });
            }
        }
        List<Category> categories = allTreeItems.stream().filter(CheckBoxTreeItem::isSelected).map(TreeItem::getValue).toList();
        this.expense.getCategories().clear();
        this.expense.getCategories().addAll(categories);

        EntityManager.getInstance().persist(this.expense);
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
}
