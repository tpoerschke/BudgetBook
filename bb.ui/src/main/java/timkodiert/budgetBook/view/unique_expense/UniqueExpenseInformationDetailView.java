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
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import timkodiert.budgetBook.converter.Converters;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.TurnoverDirection;
import timkodiert.budgetBook.domain.model.UniqueTurnoverInformation;
import timkodiert.budgetBook.domain.util.EntityManager;
import timkodiert.budgetBook.ui.control.AutoCompleteTextField;
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
    private TreeView<Category> categoriesTreeView;

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
        //categoryTreeHelper = from(categoriesTreeView, categories);

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
        //categoryTreeHelper.selectCategories(entity);
    }

    public boolean validate() {
        UniqueTurnoverInformation info = patchEntity(new UniqueTurnoverInformation());

        Map<String, Control> validationMap = new HashMap<>();
        validationMap.put("label", positionTextField);

        ValidationWrapper<UniqueTurnoverInformation> validation = new ValidationWrapper<>(validationMap);

        return validation.validate(info);
    }

    public UniqueTurnoverInformation patchEntity(UniqueTurnoverInformation entity) {
        entity.setLabel(positionTextField.getText());
        entity.setValue(Double.valueOf(valueTextField.getText()));
        entity.setDirection(directionComboBox.getSelectionModel().getSelectedItem());
        entity.getCategories().clear();
        //entity.getCategories().addAll(categoryTreeHelper.getSelectedCategories());
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
