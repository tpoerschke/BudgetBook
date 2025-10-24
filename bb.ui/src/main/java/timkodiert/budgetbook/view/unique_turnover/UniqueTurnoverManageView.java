package timkodiert.budgetbook.view.unique_turnover;

import java.time.LocalDate;
import java.util.Optional;
import javax.inject.Inject;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetbook.converter.ReferenceStringConverter;
import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.FixedTurnoverCrudService;
import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.UniqueTurnoverCrudService;
import timkodiert.budgetbook.domain.UniqueTurnoverDTO;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.table.cell.CurrencyTableCell;
import timkodiert.budgetbook.table.cell.DateTableCell;
import timkodiert.budgetbook.view.mdv_base.BaseListManageView;

import static timkodiert.budgetbook.view.FxmlResource.UNIQUE_TURNOVER_DETAIL_VIEW;

public class UniqueTurnoverManageView extends BaseListManageView<UniqueTurnoverDTO> {

    @FXML
    private TableColumn<UniqueTurnoverDTO, String> billerCol;
    @FXML
    private TableColumn<UniqueTurnoverDTO, LocalDate> dateCol;
    @FXML
    private TableColumn<UniqueTurnoverDTO, Number> valueCol;

    @FXML
    private ComboBox<Reference<FixedTurnoverDTO>> fixedTurnoverComboBox;

    private final UniqueTurnoverCrudService crudService;
    private final FixedTurnoverCrudService fixedTurnoverCrudService;

    private final SimpleObjectProperty<Reference<FixedTurnoverDTO>> selectedReference = new SimpleObjectProperty<>();

    @Inject
    public UniqueTurnoverManageView(DialogFactory dialogFactory,
                                    FXMLLoader fxmlLoader,
                                    LanguageManager languageManager,
                                    UniqueTurnoverCrudService crudService,
                                    FixedTurnoverCrudService fixedTurnoverCrudService) {
        super(fxmlLoader, dialogFactory, languageManager);
        this.crudService = crudService;
        this.fixedTurnoverCrudService = fixedTurnoverCrudService;
    }

    @Override
    public void displayEntityById(int id) {
        detailView.setBean(crudService.readById(id));
    }

    @Override
    public void initControls() {
        billerCol.setCellValueFactory(new PropertyValueFactory<>("biller"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(col -> new DateTableCell<>());
        valueCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalValue()));
        valueCol.setCellFactory(col -> new CurrencyTableCell<>());

        fixedTurnoverComboBox.getItems().add(null);
        fixedTurnoverComboBox.getItems().addAll(fixedTurnoverCrudService.findAllAsReference());
        fixedTurnoverComboBox.setConverter(new ReferenceStringConverter<>());
        selectedReference.bind(fixedTurnoverComboBox.getSelectionModel().selectedItemProperty());
        selectedReference.addListener((observable, oldValue, newValue) -> reloadTable(null));

        entityTable.getSortOrder().add(dateCol);
    }

    @Override
    protected UniqueTurnoverDTO createEmptyEntity() {
        return new UniqueTurnoverDTO();
    }

    @Override
    protected void reloadTable(@Nullable UniqueTurnoverDTO updatedBean) {
        entityTable.getItems().setAll(crudService.readAll(selectedReference.get()));
    }

    @Override
    public String getDetailViewFxmlLocation() {
        return UNIQUE_TURNOVER_DETAIL_VIEW.toString();
    }

    @FXML
    private void newUniqueExpense(ActionEvent event) {
        entityTable.getSelectionModel().clearSelection();
        lastSelectedRow = null;
        displayNewEntity();
    }

    @Override
    protected UniqueTurnoverDTO discardChanges(UniqueTurnoverDTO beanToDiscard) {
        return Optional.ofNullable(crudService.readById(beanToDiscard.getId())).orElseGet(this::createEmptyEntity);
    }
}
