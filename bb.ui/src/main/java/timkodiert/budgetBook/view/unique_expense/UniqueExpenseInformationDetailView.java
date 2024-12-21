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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;

import timkodiert.budgetBook.converter.Converters;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.TurnoverDirection;
import timkodiert.budgetBook.domain.model.UniqueTurnoverInformation;
import timkodiert.budgetBook.domain.util.EntityManager;
import timkodiert.budgetBook.ui.control.AutoCompleteTextField;
import timkodiert.budgetBook.ui.helper.CategoryCheckListHelper;
import timkodiert.budgetBook.validation.ValidationWrapper;
import timkodiert.budgetBook.view.View;

public class UniqueExpenseInformationDetailView implements View, Initializable {

    private UniqueTurnoverInformation expenseInfo;
    private final boolean isNew;
    private final Consumer<UniqueTurnoverInformation> newEntityCallback;

    private final List<String> labelSuggestions;

    private final EntityManager entityManager;

    @FXML
    private AutoCompleteTextField positionTextField;
    @FXML
    private TextField valueTextField;
    @FXML
    private ComboBox<TurnoverDirection> directionComboBox;
    @FXML
    private CheckListView<Category> categoriesListView;

    private CategoryCheckListHelper categoryCheckListHelper;

    public UniqueExpenseInformationDetailView(Optional<UniqueTurnoverInformation> optionalEntity,
                                              Consumer<UniqueTurnoverInformation> newEntityCallback,
                                              List<String> labelSuggestions,
                                              EntityManager entityManager) {
        this.expenseInfo = optionalEntity.orElse(new UniqueTurnoverInformation());
        this.isNew = optionalEntity.isEmpty();
        this.newEntityCallback = newEntityCallback;
        this.labelSuggestions = labelSuggestions;
        this.entityManager = entityManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Category> categories = entityManager.findAll(Category.class);
        categoryCheckListHelper = new CategoryCheckListHelper(categoriesListView, categories);

        positionTextField.getAvailableEntries().addAll(labelSuggestions);
        directionComboBox.getItems().setAll(TurnoverDirection.values());
        directionComboBox.setConverter(Converters.get(TurnoverDirection.class));

        showEntity(expenseInfo);
    }

    public void showEntity(UniqueTurnoverInformation entity) {
        positionTextField.setText(expenseInfo.getLabel());
        valueTextField.setText(String.valueOf(expenseInfo.getValue()));
        directionComboBox.getSelectionModel().select(entity.getDirection());
        // Kategorien anzeigen
        categoryCheckListHelper.checkCategories(entity);
    }

    public boolean validate() {
        UniqueTurnoverInformation info = patchEntity(new UniqueTurnoverInformation(), false);

        Map<String, Control> validationMap = new HashMap<>();
        validationMap.put("label", positionTextField);

        ValidationWrapper<UniqueTurnoverInformation> validation = new ValidationWrapper<>(validationMap);

        return validation.validate(info);
    }

    public UniqueTurnoverInformation patchEntity(UniqueTurnoverInformation entity, boolean isSaving) {
        entity.setLabel(positionTextField.getText());
        entity.setValue(Double.valueOf(valueTextField.getText()));
        entity.setDirection(directionComboBox.getSelectionModel().getSelectedItem());
        entity.getCategories().clear();
        entity.getCategories().addAll(categoryCheckListHelper.getCheckedCategories());

        if (!isSaving) {
            return entity;
        }

        entity.getCategories().forEach(category -> {
            if (!category.getUniqueExpenseInformation().contains(entity)) {
                category.getUniqueExpenseInformation().add(entity);
            }
        });
        return entity;
    }

    @FXML
    private void onSave(ActionEvent e) {

        if (!validate()) {
            return;
        }

        expenseInfo = patchEntity(expenseInfo, true);

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
