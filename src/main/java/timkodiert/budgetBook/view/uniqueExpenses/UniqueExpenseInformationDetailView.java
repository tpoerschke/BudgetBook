package timkodiert.budgetBook.view.uniqueExpenses;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.stage.Stage;
import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.UniqueExpenseInformation;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.view.View;

public class UniqueExpenseInformationDetailView implements View, Initializable {

    private UniqueExpenseInformation expenseInfo;
    private boolean isNew;
    private Consumer<UniqueExpenseInformation> newEntityCallback;

    @FXML
    private TextField positionTextField, valueTextField;
    @FXML
    private TreeView<Category> categoriesTreeView;
    private List<CheckBoxTreeItem<Category>> allTreeItems;

    public UniqueExpenseInformationDetailView(Optional<UniqueExpenseInformation> optionalEntity,
            Consumer<UniqueExpenseInformation> newEntityCallback) {
        this.expenseInfo = optionalEntity.orElse(new UniqueExpenseInformation());
        this.isNew = optionalEntity.isEmpty();
        this.newEntityCallback = newEntityCallback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Category> categories = EntityManager.getInstance().findAll(Category.class);
        // TODO: Refactoring, diese Code-Zeilen kommen häufiger vor
        allTreeItems = categories.stream().map(Category::asTreeItem).toList();
        List<? extends TreeItem<Category>> roots = allTreeItems.stream().filter(ti -> ti.getValue().getParent() == null)
                .toList();
        TreeItem<Category> root = new TreeItem<>(new Category("ROOT"));
        root.getChildren().addAll(roots);
        categoriesTreeView.setCellFactory(CheckBoxTreeCell.forTreeView());
        categoriesTreeView.setRoot(root);
        categoriesTreeView.setShowRoot(false);

        showEntity(expenseInfo);
    }

    public void showEntity(UniqueExpenseInformation entity) {
        positionTextField.setText(expenseInfo.getLabel());
        valueTextField.setText(String.valueOf(expenseInfo.getValue()));
        // Kategorien abhacken
        allTreeItems.forEach(ti -> ti.setSelected(expenseInfo.getCategories().contains(ti.getValue())));
    }

    @FXML
    private void onSave(ActionEvent e) {
        expenseInfo.setLabel(positionTextField.getText());
        expenseInfo.setValue(Double.valueOf(valueTextField.getText()));

        List<Category> categories = allTreeItems.stream().filter(CheckBoxTreeItem::isSelected).map(TreeItem::getValue)
                .toList();
        expenseInfo.getCategories().clear();
        expenseInfo.getCategories().addAll(categories);

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
