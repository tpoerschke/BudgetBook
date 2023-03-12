package timkodiert.budgetBook.view.uniqueExpenses;

import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;
import timkodiert.budgetBook.view.View;

public class UniqueExpenseDetailView implements View, Initializable {

    @Setter
    private Runnable onDelete;

    @FXML
    private Pane root;
    @FXML
    private TextField billerTextField;
    @FXML
    private TextArea noteTextArea;

    @Inject
    public UniqueExpenseDetailView() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setDisable(true);
    }

    @FXML
    private void save(ActionEvent event) {

    }

    @FXML
    private void discardChanges(ActionEvent event) {
        // Durch erneutes Setzen der Expense werden
        // die Änderungen verworfen.
        // setExpense(expense);
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
}
