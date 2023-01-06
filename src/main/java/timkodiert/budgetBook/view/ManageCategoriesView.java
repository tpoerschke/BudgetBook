package timkodiert.budgetBook.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.Alert.AlertType;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.repository.CategoriesRepository;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.util.ButtonTreeTableCell;

public class ManageCategoriesView implements Initializable, View {

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

    private Repository<Category> repository;

    private Category selectedCategory;

    @Inject
    public ManageCategoriesView(Repository<Category> repository) {
        this.repository = repository;
    }

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
        
        List<Category> categories = repository.findAll();
        fillTable(categories);
    }

    private void deleteCategory(Category category) {
        if(!category.getExpenses().isEmpty()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("Dieser Kategorie sind Ausgaben zugeordnet. " + 
                "Wenn Sie die Kategorie löschen, wird die Kategorie auch bei den betroffenen Ausgaben entfernt. Trotzdem löschen?");

            if(alert.showAndWait().filter(ButtonType.CANCEL::equals).isPresent()) {
                return;
            }
        }

        repository.remove(category);
        fillTable(repository.findAll());
    }

    private void fillTable(List<Category> categories) {
        List<? extends TreeItem<Category>> treeItems = categories.stream().map(Category::asTreeItem).toList();
        List<? extends TreeItem<Category>> roots = treeItems.stream().filter(ti -> ti.getValue().getParent() == null).toList();

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

            repository.persist(selectedCategory);
            categoriesTable.refresh();
        }
    }

    @FXML
    private void resetCategory(ActionEvent event) {
        nameTextField.setText(selectedCategory.getName());
        descriptionTextArea.setText(selectedCategory.getDescription());
    }
}
