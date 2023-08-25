package timkodiert.budgetBook.view.fixedExpenses;

import javax.inject.Inject;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.FixedExpenseAdapter;
import timkodiert.budgetBook.domain.repository.FixedExpensesRepository;
import timkodiert.budgetBook.util.DialogFactory;
import timkodiert.budgetBook.view.ViewComponent;
import timkodiert.budgetBook.view.baseViews.BaseManageView;

public class FixedExpensesManageView extends BaseManageView<FixedExpense, FixedExpenseAdapter> {

    @FXML
    private TableColumn<FixedExpenseAdapter, String> positionCol, typeCol;

    @Inject
    public FixedExpensesManageView(FixedExpensesRepository repository, DialogFactory dialogFactory,
            ViewComponent viewComponent) {
        super(() -> new FixedExpense(), repository, viewComponent.getEditExpenseView(), dialogFactory);
    }

    @Override
    protected void initControls() {
        positionCol.setCellValueFactory(new PropertyValueFactory<FixedExpenseAdapter, String>("position"));
        typeCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().paymentTypeProperty().get().getType()));
    }

    @FXML
    private void openNewExpense(ActionEvent event) {
        displayNewEntity();
        // Window primaryStage = ((Button) event.getSource()).getScene().getWindow();
        // try {
        //     Stage stage = StageBuilder.create()
        //             .withModality(Modality.APPLICATION_MODAL)
        //             .withOwner(primaryStage)
        //             .withFXMLResource("/fxml/NewExpense.fxml")
        //             .withView(viewComponent.getNewExpenseView())
        //             .build();
        //     stage.setOnCloseRequest(closeEvent -> reloadTable());
        //     stage.show();
        // } catch (Exception e) {
        //     Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
        //     alert.showAndWait();
        // }
    }
}
