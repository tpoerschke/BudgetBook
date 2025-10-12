package timkodiert.budgetbook.view.importer;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;
import javax.inject.Inject;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;

import timkodiert.budgetbook.converter.Converters;
import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.ImportRule;
import timkodiert.budgetbook.domain.model.TurnoverDirection;
import timkodiert.budgetbook.domain.repository.Repository;
import timkodiert.budgetbook.importer.ImportInformation;
import timkodiert.budgetbook.ui.control.MoneyTextField;
import timkodiert.budgetbook.view.View;

public class FixedTurnoverWizardView implements View, Initializable {

    private final Repository<FixedTurnover> fixedTurnoverRepository;
    private final DialogFactory dialogFactory;

    private final ObjectProperty<ImportInformation> importInformation = new SimpleObjectProperty<>();

    @FXML
    private TextField positionTextField;
    @FXML
    private MoneyTextField valueTextField;
    @FXML
    private ComboBox<TurnoverDirection> directionComboBox;
    @FXML
    private CheckBox importActiveCheckbox;
    @FXML
    private TextField importReceiverTextField;
    @FXML
    private TextField importReferenceTextField;

    @Setter
    private Consumer<FixedTurnover> onSaveCallback;

    @Inject
    public FixedTurnoverWizardView(Repository<FixedTurnover> fixedTurnoverRepository, DialogFactory dialogFactory) {
        this.fixedTurnoverRepository = fixedTurnoverRepository;
        this.dialogFactory = dialogFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        valueTextField.setDisable(true);
        directionComboBox.setConverter(Converters.get(TurnoverDirection.class));
        directionComboBox.getItems().setAll(TurnoverDirection.values());

        importInformation.addListener((observable, oldVal, newVal) -> {
            positionTextField.setText(newVal.receiverProperty().get());
            directionComboBox.getSelectionModel().select(TurnoverDirection.valueOf(newVal.amountProperty().getValue()));
            valueTextField.setValue(Math.abs(newVal.amountProperty().getValue()) / 100.0);
            importActiveCheckbox.setSelected(true);
            importReceiverTextField.setText(newVal.receiverProperty().get());
            importReferenceTextField.setText(newVal.referenceProperty().get());
        });
    }

    @FXML
    private void createTurnover(ActionEvent event) {
        ImportRule importRule = new ImportRule(importActiveCheckbox.isSelected(), importReceiverTextField.getText(), importReferenceTextField.getText());
        FixedTurnover turnover = FixedTurnover.create(positionTextField.getText(), directionComboBox.getSelectionModel().getSelectedItem(), importRule);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<FixedTurnover>> violationSet = validator.validate(turnover);

        if (!violationSet.isEmpty()) {
            dialogFactory.buildValidationErrorDialog(violationSet).showAndWait();
            return;
        }

        fixedTurnoverRepository.persist(turnover);
        onSaveCallback.accept(turnover);

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    public ObjectProperty<ImportInformation> importInformationProperty() {
        return importInformation;
    }
}
