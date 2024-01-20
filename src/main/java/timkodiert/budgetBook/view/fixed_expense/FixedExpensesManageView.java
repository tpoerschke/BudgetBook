package timkodiert.budgetBook.view.fixed_expense;

import javax.inject.Inject;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import timkodiert.budgetBook.domain.model.FixedTurnover;
import timkodiert.budgetBook.domain.model.FixedTurnoverAdapter;
import timkodiert.budgetBook.domain.model.TurnoverDirection;
import timkodiert.budgetBook.domain.repository.FixedExpensesRepository;
import timkodiert.budgetBook.util.DialogFactory;
import timkodiert.budgetBook.util.string_converter.EnumStringConverter;
import timkodiert.budgetBook.view.ControllerFactory;
import timkodiert.budgetBook.view.mdv_base.BaseManageView;

public class FixedExpensesManageView extends BaseManageView<FixedTurnover, FixedTurnoverAdapter> {

    @FXML
    private TableColumn<FixedTurnoverAdapter, String> positionCol, typeCol, directionCol;

    @Inject
    public FixedExpensesManageView(FixedExpensesRepository repository, DialogFactory dialogFactory, ControllerFactory controllerFactory) {
        super(FixedTurnover::new, repository, controllerFactory, dialogFactory);
    }

    @Override
    protected void initControls() {
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().paymentTypeProperty().get().getType()));
        EnumStringConverter<TurnoverDirection> enumStrConverter = new EnumStringConverter<>();
        directionCol.setCellValueFactory(cellData -> new SimpleStringProperty(enumStrConverter.toString(cellData.getValue().directionProperty().get())));
    }

    @Override
    public String getDetailViewFxmlLocation() {
        return "/fxml/EditExpense.fxml";
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
