package timkodiert.budgetBook.view.category_group;

import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import timkodiert.budgetBook.domain.model.CategoryGroup;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.domain.util.EntityManager;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.view.mdv_base.EntityBaseDetailView;

public class CategoryGroupDetailView extends EntityBaseDetailView<CategoryGroup> implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea descriptionTextArea;

    private final LanguageManager languageManager;

    @Inject
    protected CategoryGroupDetailView(Repository<CategoryGroup> repository, EntityManager entityManager, LanguageManager languageManager) {
        super(CategoryGroup::new, repository, entityManager);
        this.languageManager = languageManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.disableProperty().bind(entity.isNull());

        validationMap.put("name", nameTextField);
    }

    @Override
    protected CategoryGroup patchEntity(CategoryGroup entity, boolean isSaving) {
        entity.setName(nameTextField.getText());
        entity.setDescription(descriptionTextArea.getText());
        return entity;
    }

    @Override
    protected void patchUi(CategoryGroup entity) {
        nameTextField.setText(entity.getName());
        descriptionTextArea.setText(entity.getDescription());
    }

    @FXML
    private void delete(ActionEvent event) {
        CategoryGroup category = this.entity.get();

        // TODO: Hier prüfen, ob es Kategorien zu dieser Gruppe gibt

        repository.remove(category);
        setEntity(null);
        onUpdate.accept(null);
    }
}
