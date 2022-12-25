package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.util.ButtonTreeTableCell;
import timkodiert.budgetBook.util.EntityManager;

public class ManageCategoriesView implements Initializable {

    @FXML
    private TreeTableView<Category> categoriesTable;

    @FXML
    private TreeTableColumn<Category, String> nameColumn;
    @FXML
    private TreeTableColumn<Category, Category> actionColumn;

    @FXML
    private Node editContainer;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea descriptionTextArea;

    private EntityManager em = EntityManager.getInstance();

    private Category selectedCategory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue().getName()));
        actionColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Category>(cellData.getValue().getValue()));
        actionColumn.setCellFactory(col -> new ButtonTreeTableCell<>("Löschen", this::deleteCategory));

        categoriesTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<Category>> observable, TreeItem<Category> oldValue, TreeItem<Category> newValue) -> {
            if(newValue != null) {
                selectedCategory = newValue.getValue();
                nameTextField.setText(selectedCategory.getName());
                descriptionTextArea.setText(selectedCategory.getDescription());
                editContainer.setDisable(false);
            }
            else {
                editContainer.setDisable(true);
                nameTextField.setText("");
                descriptionTextArea.setText("");
            }
        });
        
        List<Category> categories = em.findAll(Category.class);
        fillTable(categories);
    }

    private void deleteCategory(Category category) {
        // TODO: Checken, ob es Ausgaben mit dieser Kategorie gibt.
        // Dann um Bestätigung bitten und die Ausgaben der Elternkategorie hinzufügen.
        if(category.getParent() != null) {
            category.getParent().getChildren().remove(category);
        }
        em.remove(category);
        fillTable(em.findAll(Category.class));
    }

    private void fillTable(List<Category> categories) {
        List<TreeItem<Category>> treeItems = categories.stream().map(Category::asTreeItem).toList();
        List<TreeItem<Category>> roots = treeItems.stream().filter(ti -> ti.getValue().getParent() == null).toList();

        // Dummy-Wurzel
        TreeItem<Category> root = new TreeItem<>(new Category("ROOT"));
        root.getChildren().addAll(roots);

        categoriesTable.setRoot(root);
    }

    @FXML
    private void saveCategory(ActionEvent event) {
        nameTextField.getStyleClass().remove("validation-error");

        if(nameTextField.getText().trim().equals("")) {
            nameTextField.getStyleClass().add("validation-error");
        }
        else {
            selectedCategory.setName(nameTextField.getText().trim());
            selectedCategory.setDescription(descriptionTextArea.getText().trim());

            em.persist(selectedCategory);
            categoriesTable.refresh();
        }
    }

    @FXML
    private void resetCategory(ActionEvent event) {
        nameTextField.setText(selectedCategory.getName());
        descriptionTextArea.setText(selectedCategory.getDescription());
    }
}
