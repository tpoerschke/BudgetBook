package timkodiert.budgetBook.view.category;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import timkodiert.budgetBook.converter.Converters;
import timkodiert.budgetBook.domain.model.BudgetType;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.CategoryGroup;
import timkodiert.budgetBook.domain.repository.CategoryGroupsRepository;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.domain.util.EntityManager;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.view.mdv_base.EntityBaseDetailView;

import static timkodiert.budgetBook.util.ObjectUtils.nvl;

public class CategoryDetailView extends EntityBaseDetailView<Category> implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private ComboBox<CategoryGroup> groupComboBox;
    @FXML
    private TextField budgetValueTextField;
    @FXML
    private CheckBox budgetActiveCheckBox;
    @FXML
    private ComboBox<BudgetType> budgetTypeComboBox;

    private final LanguageManager languageManager;
    private final CategoryGroupsRepository groupsRepository;

    @Inject
    protected CategoryDetailView(Repository<Category> repository,
                                 EntityManager entityManager,
                                 LanguageManager languageManager,
                                 CategoryGroupsRepository groupsRepository) {
        super(Category::new, repository, entityManager);
        this.languageManager = languageManager;
        this.groupsRepository = groupsRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.disableProperty().bind(entity.isNull());
        groupComboBox.setConverter(Converters.get(CategoryGroup.class));
        groupComboBox.getItems().add(null);
        groupComboBox.getItems().addAll(groupsRepository.findAll());
        budgetTypeComboBox.setConverter(Converters.get(BudgetType.class));
        budgetTypeComboBox.getItems().add(null);
        budgetTypeComboBox.getItems().addAll(BudgetType.values());
    }

    @Override
    protected Category patchEntity(Category entity, boolean isSaving) {
        entity.setName(nameTextField.getText());
        entity.setDescription(descriptionTextArea.getText());
        entity.setGroup(groupComboBox.getSelectionModel().getSelectedItem());
        String budgetValueStr = budgetValueTextField.getText().trim();
        entity.setBudgetValue(budgetValueStr.isEmpty() ? null : Double.parseDouble(budgetValueStr));
        entity.setBudgetActive(budgetActiveCheckBox.isSelected());
        entity.setBudgetType(budgetTypeComboBox.getSelectionModel().getSelectedItem());
        return entity;
    }

    @Override
    protected void patchUi(Category entity) {
        nameTextField.setText(entity.getName());
        descriptionTextArea.setText(entity.getDescription());
        groupComboBox.getSelectionModel().select(entity.getGroup());
        budgetValueTextField.setText(nvl(entity.getBudgetValue(), String::valueOf, ""));
        budgetActiveCheckBox.setSelected(entity.isBudgetActive());
        budgetTypeComboBox.getSelectionModel().select(entity.getBudgetType());
    }

    @Override
    public boolean save() {
        boolean saved = super.save();
        CategoryGroup catGroup = entity.get().getGroup();
        if (saved && catGroup != null) {
            // TODO: Dafür eine Schnittstelle schaffen bzw. ins Repository schieben
            entityManager.refresh(catGroup);
            return true;
        }
        return false;
    }

    @FXML
    private void delete(ActionEvent event) {
        Category category = this.entity.get();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Die Kategorie \"" + category.getName() + "\" wirklich löschen?",
                                            ButtonType.YES,
                                            ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.filter(ButtonType.NO::equals).isPresent()) {
            return;
        }

        if (!category.getFixedExpenses().isEmpty() || !category.getUniqueExpenseInformation().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(languageManager.get("manageCategories.alert.expensesAreAssignedToThisCategory"));

            if (alert.showAndWait().filter(ButtonType.CANCEL::equals).isPresent()) {
                return;
            }
        }

        repository.remove(category);
        setEntity(null);
        onUpdate.accept(null);
    }
}
