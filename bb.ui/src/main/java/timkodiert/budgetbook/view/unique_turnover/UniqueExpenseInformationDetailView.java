package timkodiert.budgetbook.view.unique_turnover;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
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
import timkodiert.budgetbook.ui.helper.Bind;
import timkodiert.budgetbook.view.mdv_base.BaseDetailView;

public class UniqueExpenseInformationDetailView extends BaseDetailView<UniqueTurnoverInformationDTO> implements Initializable {

    @FXML
    private AutoCompleteTextField positionTextField;
    @FXML
    private TextField valueTextField;
    @FXML
    private ComboBox<TurnoverDirection> directionComboBox;
    @FXML
    private CheckListView<Reference<CategoryDTO>> categoriesListView;

    private final UniqueTurnoverCrudService uniqueTurnoverCrudService;
    private final CategoryCrudService categoryCrudService;
    private final BiConsumer<UniqueTurnoverInformationDTO, UniqueTurnoverInformationDTO> updateCallback;

    @AssistedInject
    public UniqueExpenseInformationDetailView(UniqueTurnoverCrudService uniqueTurnoverCrudService,
                                              CategoryCrudService categoryCrudService,
                                              @Assisted BiConsumer<UniqueTurnoverInformationDTO, UniqueTurnoverInformationDTO> updateCallback) {
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

        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter());
        formatter.valueProperty().bindBidirectional(beanAdapter.getProperty(UniqueTurnoverInformationDTO::getValue, UniqueTurnoverInformationDTO::setValue));
        valueTextField.setTextFormatter(formatter);

        Bind.comboBox(directionComboBox,
                      beanAdapter.getProperty(UniqueTurnoverInformationDTO::getDirection, UniqueTurnoverInformationDTO::setDirection),
                      Arrays.asList(TurnoverDirection.values()),
                      TurnoverDirection.class);

        // Validierungen
        validationMap.put("label", positionTextField);
    }

    @Override
    protected void beanSet() {
        categoriesListView.getCheckModel().clearChecks();
        UniqueTurnoverInformationDTO bean = beanAdapter.getBean();
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
        beanAdapter.getBean().setCategories(categoriesListView.getCheckModel().getCheckedItems());
        updateCallback.accept(null, beanAdapter.getBean());

        // Das macht mich traurig ._.
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void onRevert() {
        UniqueTurnoverInformationDTO modified = beanAdapter.getBean();
        UniqueTurnoverInformationDTO unmodified = uniqueTurnoverCrudService.readUniqueTurnoverInformationById(modified.getId());
        beanAdapter.setBean(unmodified);
        updateCallback.accept(modified, unmodified);
    }

}
