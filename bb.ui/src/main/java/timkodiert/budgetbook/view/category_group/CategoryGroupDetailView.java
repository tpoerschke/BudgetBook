package timkodiert.budgetbook.view.category_group;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import timkodiert.budgetbook.domain.CategoryGroupCrudService;
import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.view.mdv_base.EntityBaseDetailView;

public class CategoryGroupDetailView extends EntityBaseDetailView<CategoryGroupDTO> implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea descriptionTextArea;

    private final CategoryGroupCrudService crudService;

    @Inject
    public CategoryGroupDetailView(CategoryGroupCrudService crudService) {
        this.crudService = crudService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.disableProperty().bind(beanAdapter.isEmpty());

        nameTextField.textProperty().bindBidirectional(beanAdapter.getProperty(CategoryGroupDTO::getName, CategoryGroupDTO::setName));
        descriptionTextArea.textProperty().bindBidirectional(beanAdapter.getProperty(CategoryGroupDTO::getDescription, CategoryGroupDTO::setDescription));

        validationMap.put("name", nameTextField);
    }

    @FXML
    private void delete(ActionEvent event) {
        CategoryGroupDTO category = this.getBean();

        // TODO: Hier prüfen, ob es Kategorien zu dieser Gruppe gibt

        crudService.delete(category.getId());
        beanAdapter.setBean(null);
        onUpdate.accept(null);
    }

    @Override
    protected CategoryGroupDTO createEmptyEntity() {
        return new CategoryGroupDTO();
    }

    @Override
    public boolean save() {
        CategoryGroupDTO dto = getBean();
        if (dto == null) {
            return false;
        }

        Predicate<CategoryGroupDTO> servicePersistMethod = dto.isNew() ? crudService::create : crudService::update;
        return validate() && servicePersistMethod.test(dto);
    }

    @Override
    protected CategoryGroupDTO discardChanges() {
        return crudService.readById(getBean().getId());
    }
}
