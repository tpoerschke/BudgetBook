package timkodiert.budgetbook.view.fixed_turnover;

import javax.inject.Inject;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.adapter.FixedTurnoverAdapter;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.PaymentType;
import timkodiert.budgetbook.domain.model.TurnoverDirection;
import timkodiert.budgetbook.domain.repository.FixedExpensesRepository;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.view.mdv_base.BaseListManageView;

import static timkodiert.budgetbook.view.FxmlResource.FIXED_TURNOVER_DETAIL_VIEW;

public class FixedTurnoverManageView extends BaseListManageView<FixedTurnover, FixedTurnoverAdapter> {

    @FXML
    private TableColumn<FixedTurnoverAdapter, String> positionCol, typeCol, directionCol;

    @Inject
    public FixedTurnoverManageView(FixedExpensesRepository repository, DialogFactory dialogFactory, FXMLLoader fxmlLoader, LanguageManager languageManager) {
        super(FixedTurnover::new, repository, fxmlLoader, dialogFactory, languageManager);
    }

    @Override
    protected void initControls() {
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        StringConverter<PaymentType> paymentTypeConverter = Converters.get(PaymentType.class);
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(paymentTypeConverter.toString(cellData.getValue().paymentTypeProperty().get())));
        StringConverter<TurnoverDirection> enumStrConverter = Converters.get(TurnoverDirection.class);
        directionCol.setCellValueFactory(cellData -> new SimpleStringProperty(enumStrConverter.toString(cellData.getValue().directionProperty().get())));
    }

    @Override
    public String getDetailViewFxmlLocation() {
        return FIXED_TURNOVER_DETAIL_VIEW.toString();
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
