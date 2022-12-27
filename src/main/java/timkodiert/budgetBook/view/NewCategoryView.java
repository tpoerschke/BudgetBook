package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.util.EntityManager;

public class NewCategoryView implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TreeView<Category> categoriesTreeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EntityManager em = EntityManager.getInstance();
        List<Category> categories = em.findAll(Category.class);

        List<? extends TreeItem<Category>> treeItems = categories.stream().map(Category::asTreeItem).toList();
        List<? extends TreeItem<Category>> roots = treeItems.stream().filter(ti -> ti.getValue().getParent() == null).toList();

        // Dummy-Wurzel
        TreeItem<Category> root = new TreeItem<>(new Category("ROOT"));
        root.getChildren().addAll(roots);

        categoriesTreeView.setRoot(root);
    }

    @FXML
    private void saveCategory(ActionEvent event) {
        nameTextField.getStyleClass().remove("validation-error");

        Category category = new Category();
        category.setName(nameTextField.getText().trim());
        category.setDescription(descriptionTextArea.getText().trim());

        if(category.getName().equals("")) {
            nameTextField.getStyleClass().add("validation-error");
        }
        else {
            TreeItem<Category> selectedItem = categoriesTreeView.getSelectionModel().getSelectedItem();
            Category parent = selectedItem != null ? selectedItem.getValue() : null;
            category.setParent(parent);

            EntityManager.getInstance().persist(category);
            if(parent != null) {
                EntityManager.getInstance().refresh(parent);
            }

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("Ausgabe \"" + category.getName() + "\" hinzugefügt.");
            alert.showAndWait();
            closeStage(event);
        }
    }

    @FXML
    private void resetParentSelection(ActionEvent event) {
        categoriesTreeView.getSelectionModel().clearSelection();
    }

    @FXML
    private void closeStage(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
