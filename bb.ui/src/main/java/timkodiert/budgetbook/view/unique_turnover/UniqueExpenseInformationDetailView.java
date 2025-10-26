package timkodiert.budgetbook.view.unique_turnover;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;
import org.controlsfx.control.CheckListView;

import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.converter.ReferenceStringConverter;
import timkodiert.budgetbook.domain.CategoryCrudService;
import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.UniqueTurnoverCrudService;
import timkodiert.budgetbook.domain.UniqueTurnoverInformationDTO;
import timkodiert.budgetbook.domain.model.TurnoverDirection;
import timkodiert.budgetbook.ui.control.AutoCompleteTextField;
import timkodiert.budgetbook.ui.control.MoneyTextField;
import timkodiert.budgetbook.ui.helper.Bind;
import timkodiert.budgetbook.validation.ValidationResult;
import timkodiert.budgetbook.validation.ValidationWrapperFactory;
import timkodiert.budgetbook.view.mdv_base.BaseDetailView;

public class UniqueExpenseInformationDetailView extends BaseDetailView<UniqueTurnoverInformationDTO> implements Initializable {

    @FXML
    private AutoCompleteTextField positionTextField;
    @FXML
    private MoneyTextField valueTextField;
    @FXML
    private ComboBox<TurnoverDirection> directionComboBox;
    @FXML
    private CheckListView<Reference<CategoryDTO>> categoriesListView;

    private Stage stage;
    private UniqueTurnoverInformationDTO backupBean;

    private final UniqueTurnoverCrudService uniqueTurnoverCrudService;
    private final CategoryCrudService categoryCrudService;
    private final BiConsumer<UniqueTurnoverInformationDTO, UniqueTurnoverInformationDTO> updateCallback;

    @AssistedInject
    public UniqueExpenseInformationDetailView(ValidationWrapperFactory<UniqueTurnoverInformationDTO> validationWrapperFactory,
                                              UniqueTurnoverCrudService uniqueTurnoverCrudService,
                                              CategoryCrudService categoryCrudService,
                                              @Assisted BiConsumer<UniqueTurnoverInformationDTO, UniqueTurnoverInformationDTO> updateCallback) {
        super(validationWrapperFactory);
        this.uniqueTurnoverCrudService = uniqueTurnoverCrudService;
        this.categoryCrudService = categoryCrudService;
        this.updateCallback = updateCallback;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoriesListView.setCellFactory(lv -> new CheckBoxListCell<>(item -> categoriesListView.getItemBooleanProperty(item), new ReferenceStringConverter<>()));
        List<Reference<CategoryDTO>> categories = categoryCrudService.readAll().stream().map(c -> new Reference<>(CategoryDTO.class, c.getId(), c.getName())).toList();
        categoriesListView.getItems().setAll(categories);
        positionTextField.getAvailableEntries().addAll(uniqueTurnoverCrudService.getUniqueTurnoverInformationLabels());
        directionComboBox.getItems().setAll(TurnoverDirection.values());
        directionComboBox.setConverter(Converters.get(TurnoverDirection.class));

        // Bindings
        positionTextField.textProperty().bindBidirectional(beanAdapter.getProperty(UniqueTurnoverInformationDTO::getLabel, UniqueTurnoverInformationDTO::setLabel));
        valueTextField.integerValueProperty().bindBidirectional(beanAdapter.getProperty(UniqueTurnoverInformationDTO::getValue, UniqueTurnoverInformationDTO::setValue));
        Bind.comboBox(directionComboBox,
                      beanAdapter.getProperty(UniqueTurnoverInformationDTO::getDirection, UniqueTurnoverInformationDTO::setDirection),
                      Arrays.asList(TurnoverDirection.values()),
                      TurnoverDirection.class);

        // Validierungen
        validationMap.put("label", positionTextField);
        validationMap.put("direction", directionComboBox);
        validationWrapper.register(positionTextField.textProperty(), directionComboBox.getSelectionModel().selectedItemProperty());
        validationWrapper.registerCustomValidation("amountValid",
                                                   valueTextField.getTextField(),
                                                   () -> valueTextField.isStringFormatValid()
                                                           ? ValidationResult.valid()
                                                           : ValidationResult.error("{amount.format.valid}"),
                                                   beanAdapter.getProperty(UniqueTurnoverInformationDTO::getValue, UniqueTurnoverInformationDTO::setValue));
    }

    @Override
    protected void beanSet() {
        backupBean = SerializationUtils.clone(beanAdapter.getBean());
        UniqueTurnoverInformationDTO bean = beanAdapter.getBean();
        categoriesListView.getCheckModel().clearChecks();
        bean.getCategories().forEach(cat -> categoriesListView.getCheckModel().check(cat));
    }

    @Override
    protected UniqueTurnoverInformationDTO createEmptyEntity() {
        return new UniqueTurnoverInformationDTO();
    }

    @FXML
    private void onSave(ActionEvent e) {
        if (!validate()) {
            return;
        }
        beanAdapter.getBean().setCategories(new ArrayList<>(categoriesListView.getCheckModel().getCheckedItems()));
        updateCallback.accept(null, beanAdapter.getBean());
        stage.close();
    }

    @FXML
    private void onRevert() {
        UniqueTurnoverInformationDTO modified = beanAdapter.getBean();
        setBean(SerializationUtils.clone(backupBean));
        updateCallback.accept(modified, beanAdapter.getBean());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> onRevert());
    }
}
