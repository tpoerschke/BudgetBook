package timkodiert.budgetbook.view.billing;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import timkodiert.budgetbook.domain.BillingCrudService;
import timkodiert.budgetbook.domain.BillingDTO;
import timkodiert.budgetbook.validation.ValidationWrapperFactory;
import timkodiert.budgetbook.view.mdv_base.EntityBaseDetailView;

public class BillingDetailView extends EntityBaseDetailView<BillingDTO> implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private TextField titleTextField;

    @FXML
    private Button saveButton;
    @FXML
    private Button discardButton;

    private final BillingCrudService crudService;

    @Inject
    public BillingDetailView(ValidationWrapperFactory<BillingDTO> validationWrapperFactory, BillingCrudService crudService) {
        super(validationWrapperFactory);
        this.crudService = crudService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.disableProperty().bind(beanAdapter.isEmpty());
        saveButton.disableProperty().bind(beanAdapter.dirty().not());
        discardButton.disableProperty().bind(beanAdapter.dirty().not());

        titleTextField.textProperty().bindBidirectional(beanAdapter.getProperty(BillingDTO::getTitle, BillingDTO::setTitle));

        // Validierungen
        validationMap.put("title", titleTextField);
        validationWrapper.register(beanAdapter.getProperty(BillingDTO::getTitle, BillingDTO::setTitle));
    }

    @Override
    protected BillingDTO createEmptyEntity() {
        return new BillingDTO();
    }

    @Override
    public boolean save() {
        BillingDTO bean = getBean();
        if (bean == null) {
            return false;
        }
        Predicate<BillingDTO> servicePersistMethod = bean.isNew() ? crudService::create : crudService::update;
        boolean success = validate() && servicePersistMethod.test(bean);
        if (success) {
            beanAdapter.setDirty(false);
            onUpdate.accept(bean);
            return true;
        }
        return false;
    }

    @Override
    protected BillingDTO discardChanges() {
        return Optional.ofNullable(crudService.readById(Objects.requireNonNull(getBean()).getId())).orElseGet(this::createEmptyEntity);
    }

    @FXML
    private void delete(ActionEvent event) {
        BillingDTO billingDTO = this.getBean();
        crudService.delete(billingDTO.getId());
        beanAdapter.setBean(null);
        onUpdate.accept(null);
    }
}
