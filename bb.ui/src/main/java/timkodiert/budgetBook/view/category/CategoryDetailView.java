package timkodiert.budgetBook.view.category;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.domain.util.EntityManager;
import timkodiert.budgetBook.view.mdv_base.EntityBaseDetailView;

public class CategoryDetailView extends EntityBaseDetailView<Category> {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea descriptionTextArea;

    @Inject
    protected CategoryDetailView(Repository<Category> repository, EntityManager entityManager) {
        super(Category::new, repository, entityManager);
    }

    @Override
    protected Category patchEntity(Category entity) {
        entity.setName(nameTextField.getText());
        entity.setDescription(descriptionTextArea.getText());
        return entity;
    }

    @Override
    protected void patchUi(Category entity) {
        nameTextField.setText(entity.getName());
        descriptionTextArea.setText(entity.getDescription());
    }

//    @FXML
//    private void saveCategory(ActionEvent event) {
//        nameTextField.getStyleClass().remove("validation-error");
//
//        if (nameTextField.getText().trim().equals("")) {
//            nameTextField.getStyleClass().add("validation-error");
//        } else {
//            selectedCategory.setName(nameTextField.getText().trim());
//            selectedCategory.setDescription(descriptionTextArea.getText().trim());
//
//            repository.persist(selectedCategory);
//            categoriesTable.refresh();
//        }
//    }
//
//    @FXML
//    private void resetCategory(ActionEvent event) {
//        nameTextField.setText(selectedCategory.getName());
//        descriptionTextArea.setText(selectedCategory.getDescription());
//    }
}
