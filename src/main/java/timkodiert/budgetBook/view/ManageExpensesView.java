package timkodiert.budgetBook.view;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class ManageExpensesView implements View {

    @FXML
    private Pane detailViewContainer;

    @Inject
    public ManageExpensesView() {

    }
}
