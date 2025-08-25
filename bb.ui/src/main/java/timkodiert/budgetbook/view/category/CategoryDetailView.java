package timkodiert.budgetbook.view.category;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
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

import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.converter.DoubleCurrencyStringConverter;
import timkodiert.budgetbook.converter.ReferenceStringConverter;
import timkodiert.budgetbook.domain.CategoryCrudService;
import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.CategoryGroupCrudService;
import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.model.BudgetType;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.ui.helper.Bind;
import timkodiert.budgetbook.view.mdv_base.EntityBaseDetailView;

public class CategoryDetailView extends EntityBaseDetailView<CategoryDTO> implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private ComboBox<Reference<CategoryGroupDTO>> groupComboBox;
    @FXML
    private TextField budgetValueTextField;
    @FXML
    private CheckBox budgetActiveCheckBox;
    @FXML
    private ComboBox<BudgetType> budgetTypeComboBox;

    private final LanguageManager languageManager;
    private final CategoryCrudService categoryCrudService;
    private final CategoryGroupCrudService categoryGroupCrudService;

    @Inject
    protected CategoryDetailView(LanguageManager languageManager,
                                 CategoryCrudService categoryCrudService,
                                 CategoryGroupCrudService categoryGroupCrudService) {
        this.languageManager = languageManager;
        this.categoryCrudService = categoryCrudService;
        this.categoryGroupCrudService = categoryGroupCrudService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.disableProperty().bind(beanAdapter.isEmpty());
        groupComboBox.setConverter(new ReferenceStringConverter<>());
        groupComboBox.getItems().add(null);
        groupComboBox.getItems().addAll(categoryGroupCrudService.readAll()
                                                                .stream()
                                                                .map(cg -> new Reference<>(CategoryGroupDTO.class, cg.getId(), cg.getName()))
                                                                .toList());
        budgetTypeComboBox.setConverter(Converters.get(BudgetType.class));
        budgetTypeComboBox.getItems().add(null);
        budgetTypeComboBox.getItems().addAll(BudgetType.values());

        // Name und Beschreibung
        nameTextField.textProperty().bindBidirectional(beanAdapter.getProperty(CategoryDTO::getName, CategoryDTO::setName));
        descriptionTextArea.textProperty().bindBidirectional(beanAdapter.getProperty(CategoryDTO::getDescription, CategoryDTO::setDescription));
        // Kategoriegruppe
        Bind.comboBox(groupComboBox, beanAdapter.getProperty(CategoryDTO::getGroup, CategoryDTO::setGroup));
        // Budget
        budgetActiveCheckBox.selectedProperty().bindBidirectional(beanAdapter.getProperty(CategoryDTO::isBudgetActive, CategoryDTO::setBudgetActive));
        budgetValueTextField.textProperty().bindBidirectional(beanAdapter.getProperty(CategoryDTO::getBudgetValue, CategoryDTO::setBudgetValue),
                                                              new DoubleCurrencyStringConverter());
        Bind.comboBox(budgetTypeComboBox, beanAdapter.getProperty(CategoryDTO::getBudgetType, CategoryDTO::setBudgetType));
    }

    @Override
    protected CategoryDTO createEmptyEntity() {
        return new CategoryDTO();
    }

    @Override
    public boolean save() {
        CategoryDTO bean = getBean();
        if (bean == null) {
            return false;
        }

        Predicate<CategoryDTO> servicePersistMethod = bean.isNew() ? categoryCrudService::create : categoryCrudService::update;
        boolean success = validate() && servicePersistMethod.test(bean);
        //        boolean saved = super.save();
        //        CategoryGroup catGroup = entity.get().getGroup();
        //        if (saved && catGroup != null) {
        //            // TODO: Dafür eine Schnittstelle schaffen bzw. ins Repository schieben
        //            entityManager.refresh(catGroup);
        //            return true;
        //        }
        if (success) {
            beanAdapter.setDirty(false);
            onUpdate.accept(bean);
            return true;
        }
        return false;
    }

    @Override
    protected CategoryDTO discardChanges() {
        return categoryCrudService.readById(getBean().getId());
    }

    @FXML
    private void delete(ActionEvent event) {
        CategoryDTO category = this.getBean();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Die Kategorie \"" + category.getName() + "\" wirklich löschen?",
                                            ButtonType.YES,
                                            ButtonType.NO);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.filter(ButtonType.NO::equals).isPresent()) {
            return;
        }

        if (category.isHasLinkedTurnover()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(languageManager.get("manageCategories.alert.expensesAreAssignedToThisCategory"));

            if (alert.showAndWait().filter(ButtonType.CANCEL::equals).isPresent()) {
                return;
            }
        }

        categoryCrudService.delete(category.getId());
        setBean(null);
        onUpdate.accept(null);
    }
}
