package timkodiert.budgetBook.view.uniqueExpenses;

import static timkodiert.budgetBook.util.CategoryTreeHelper.from;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.UniqueExpenseInformation;
import timkodiert.budgetBook.util.CategoryTreeHelper;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.view.View;

public class UniqueExpenseInformationDetailView implements View, Initializable {

    private UniqueExpenseInformation expenseInfo;
    private boolean isNew;
    private Consumer<UniqueExpenseInformation> newEntityCallback;

    @FXML
    private TextField positionTextField, valueTextField;
    @FXML
    private TreeView<Category> categoriesTreeView;
    private CategoryTreeHelper categoryTreeHelper;

    public UniqueExpenseInformationDetailView(Optional<UniqueExpenseInformation> optionalEntity,
            Consumer<UniqueExpenseInformation> newEntityCallback) {
        this.expenseInfo = optionalEntity.orElse(new UniqueExpenseInformation());
        this.isNew = optionalEntity.isEmpty();
        this.newEntityCallback = newEntityCallback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Category> categories = EntityManager.getInstance().findAll(Category.class);
        categoryTreeHelper = from(categoriesTreeView, categories);

        showEntity(expenseInfo);
    }

    public void showEntity(UniqueExpenseInformation entity) {
        positionTextField.setText(expenseInfo.getLabel());
        valueTextField.setText(String.valueOf(expenseInfo.getValue()));
        // Kategorien anzeigen
        categoryTreeHelper.selectCategories(entity);
    }

    @FXML
    private void onSave(ActionEvent e) {
        expenseInfo.setLabel(positionTextField.getText());
        expenseInfo.setValue(Double.valueOf(valueTextField.getText()));
        expenseInfo.getCategories().clear();
        expenseInfo.getCategories().addAll(categoryTreeHelper.getSelectedCategories());

        if (isNew) {
            newEntityCallback.accept(expenseInfo);
        }

        // Das macht mich traurig ._.
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void onRevert() {
        showEntity(expenseInfo);
    }

}
