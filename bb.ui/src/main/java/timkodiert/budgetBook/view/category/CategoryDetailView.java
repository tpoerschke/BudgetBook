package timkodiert.budgetBook.view.category;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javax.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.domain.util.EntityManager;
import timkodiert.budgetBook.ui.helper.CategoryTreeHelper;
import timkodiert.budgetBook.view.mdv_base.EntityBaseDetailView;

public class CategoryDetailView extends EntityBaseDetailView<Category> implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TreeView<Category> categoriesTreeView;
    @FXML
    private VBox categoriesTreeViewContainer;

    private CategoryTreeHelper categoryTreeHelper;

    @Inject
    protected CategoryDetailView(Repository<Category> repository, EntityManager entityManager) {
        super(Category::new, repository, entityManager);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.disableProperty().bind(entity.isNull());

        List<Category> categories = repository.findAll();
        categoryTreeHelper = CategoryTreeHelper.from(categoriesTreeView, categories, false);
    }

    @Override
    protected Category patchEntity(Category entity) {
        entity.setName(nameTextField.getText());
        entity.setDescription(descriptionTextArea.getText());
        entity.setParent(categoryTreeHelper.getSelectedCategories().stream().findAny().orElse(null));
        return entity;
    }

    @Override
    protected void patchUi(Category entity) {
        nameTextField.setText(entity.getName());
        descriptionTextArea.setText(entity.getDescription());
        categoriesTreeViewContainer.setVisible(entity.isNew());
    }

    @FXML
    private void resetParentSelection(ActionEvent event) {
        categoriesTreeView.getSelectionModel().clearSelection();
    }

    @Override
    public boolean save() {
        boolean saved = super.save();
        if (saved) {
            if (entity.get().getParent() != null) {
                // TODO: Dafür eine Schnittstelle schaffen bzw. ins Repository schieben
                entityManager.refresh(entity.get().getParent());
            }
            return true;
        }
        return false;
    }
}
