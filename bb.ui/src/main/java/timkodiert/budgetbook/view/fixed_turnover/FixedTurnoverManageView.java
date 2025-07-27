package timkodiert.budgetbook.view.fixed_turnover;

import javax.inject.Inject;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.FixedTurnoverCrudService;
import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.adapter.FixedTurnoverAdapter;
import timkodiert.budgetbook.domain.model.PaymentType;
import timkodiert.budgetbook.domain.model.TurnoverDirection;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.view.mdv_base.BaseListManageView;

import static timkodiert.budgetbook.view.FxmlResource.FIXED_TURNOVER_DETAIL_VIEW;

public class FixedTurnoverManageView extends BaseListManageView<FixedTurnoverDTO> {

    @FXML
    private TableColumn<FixedTurnoverAdapter, String> positionCol, typeCol, directionCol;

    private final FixedTurnoverCrudService crudService;

    @Inject
    public FixedTurnoverManageView(DialogFactory dialogFactory, FXMLLoader fxmlLoader, LanguageManager languageManager, FixedTurnoverCrudService crudService) {
        super(fxmlLoader, dialogFactory, languageManager);
        this.crudService = crudService;
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
    }

    // Wird später über den Service abgebildet, der bekommt dann ein readById o.ä. und das wird in den Adapter gewrappt
    @Override
    public void displayEntityById(int id) {
        detailView.setBean(crudService.readById(id));
    }

    @Override
    protected void reloadTable(@Nullable FixedTurnoverDTO updatedBean) {
        // Wird später über den Service gemacht
        entityTable.getItems().setAll(crudService.readAll());
        entityTable.sort();
    }

    @Override
    protected FixedTurnoverDTO createEmptyEntity() {
        return new FixedTurnoverDTO();
    }

    @Override
    protected FixedTurnoverDTO discardChanges(FixedTurnoverDTO beanToDiscard) {
        return null;
    }
}
