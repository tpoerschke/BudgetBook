package timkodiert.budgetBook.view.uniqueExpenses;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.inject.Inject;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Setter;
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.domain.repository.UniqueExpensesRepository;
import timkodiert.budgetBook.util.StageBuilder;
import timkodiert.budgetBook.view.View;
import timkodiert.budgetBook.view.ViewComponent;

public class UniqueExpenseDetailView implements View, Initializable {

    @Setter
    private Runnable onUpdate;

    @FXML
    private Pane root;
    @FXML
    private TextField billerTextField;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private Button addUniqueExpenseInformationButton;

    private Repository<UniqueExpense> repository;
    private ObjectProperty<UniqueExpense> expense = new SimpleObjectProperty<>();

    @Inject
    public UniqueExpenseDetailView(Repository<UniqueExpense> repository) {
        this.repository = repository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addUniqueExpenseInformationButton.setText("");
        addUniqueExpenseInformationButton.setGraphic(new FontIcon(BootstrapIcons.PLUS));

        root.disableProperty().bind(Bindings.createBooleanBinding(() -> expense.get() == null, expense));
    }

    public void setBean(UniqueExpense uniqueExpense) {
        this.expense.set(uniqueExpense);
        billerTextField.setText(expense.get().getBiller());
    }

    @FXML
    private void save(ActionEvent event) {
        UniqueExpense exp = this.expense.get();
        exp.setBiller(billerTextField.getText());
        repository.persist(exp);
        onUpdate.run();
    }

    @FXML
    private void discardChanges(ActionEvent event) {
        // Durch erneutes Setzen der Expense werden
        // die Änderungen verworfen.
        setBean(expense.get());
    }

    @FXML
    private void delete(ActionEvent event) {
        // Alert confirmationAlert = new Alert(AlertType.CONFIRMATION,
        //         "Die Ausgabe \"" + this.expense.getPosition() + "\" wirklich löschen?", ButtonType.YES, ButtonType.NO);
        // Optional<ButtonType> result = confirmationAlert.showAndWait();
        // if (result.filter(ButtonType.YES::equals).isPresent()) {
        //     EntityManager.getInstance().remove(this.expense);
        //     setExpense(null);
        //     onDelete.run();
        // }
    }

    @FXML
    private void newUniqueExpenseInformation(ActionEvent event) {
        try {
            Stage stage = StageBuilder.create()
                    .withModality(Modality.APPLICATION_MODAL)
                    .withOwner(Window.getWindows().get(0))
                    .withFXMLResource("/fxml/UniqueExpenses/Information.fxml")
                    .withView(new UniqueExpenseInformationDetailView(Optional.empty()))
                    .build();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
