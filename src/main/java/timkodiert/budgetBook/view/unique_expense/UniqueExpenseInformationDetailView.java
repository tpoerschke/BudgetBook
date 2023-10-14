package timkodiert.budgetBook.view.unique_expense;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.UniqueExpenseInformation;
import timkodiert.budgetBook.ui.control.AutoCompleteTextField;
import timkodiert.budgetBook.util.CategoryTreeHelper;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.validation.ValidationWrapper;
import timkodiert.budgetBook.view.View;

import static timkodiert.budgetBook.util.CategoryTreeHelper.from;

public class UniqueExpenseInformationDetailView implements View, Initializable {

    private UniqueExpenseInformation expenseInfo;
    private boolean isNew;
    private Consumer<UniqueExpenseInformation> newEntityCallback;

    private List<String> labelSuggestions;

    @FXML
    private AutoCompleteTextField positionTextField;
    @FXML
    private TextField valueTextField;
    @FXML
    private TreeView<Category> categoriesTreeView;
    private CategoryTreeHelper categoryTreeHelper;

    public UniqueExpenseInformationDetailView(Optional<UniqueExpenseInformation> optionalEntity,
            Consumer<UniqueExpenseInformation> newEntityCallback, List<String> labelSuggestions) {
        this.expenseInfo = optionalEntity.orElse(new UniqueExpenseInformation());
        this.isNew = optionalEntity.isEmpty();
        this.newEntityCallback = newEntityCallback;
        this.labelSuggestions = labelSuggestions;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Category> categories = EntityManager.getInstance().findAll(Category.class);
        categoryTreeHelper = from(categoriesTreeView, categories);

        positionTextField.getAvailableEntries().addAll(labelSuggestions);

        showEntity(expenseInfo);
    }

    public void showEntity(UniqueExpenseInformation entity) {
        positionTextField.setText(expenseInfo.getLabel());
        valueTextField.setText(String.valueOf(expenseInfo.getValue()));
        // Kategorien anzeigen
        categoryTreeHelper.selectCategories(entity);
    }

    public boolean validate() {
        UniqueExpenseInformation info = patchEntity(new UniqueExpenseInformation());

        Map<String, Control> validationMap = new HashMap<>();
        validationMap.put("label", positionTextField);

        ValidationWrapper<UniqueExpenseInformation> validation = new ValidationWrapper<>(validationMap);

        if (!validation.validate(info)) {
            return false;
        }
        return true;
    }

    public UniqueExpenseInformation patchEntity(UniqueExpenseInformation entity) {
        entity.setLabel(positionTextField.getText());
        entity.setValue(Double.valueOf(valueTextField.getText()));
        entity.getCategories().clear();
        entity.getCategories().addAll(categoryTreeHelper.getSelectedCategories());
        return entity;
    }

    @FXML
    private void onSave(ActionEvent e) {

        if (!validate()) {
            return;
        }

        expenseInfo = patchEntity(expenseInfo);

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
