package timkodiert.budgetBook.view.importer;

import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import timkodiert.budgetBook.converter.Converters;
import timkodiert.budgetBook.domain.model.TurnoverDirection;
import timkodiert.budgetBook.importer.ImportInformation;
import timkodiert.budgetBook.ui.control.MoneyTextField;
import timkodiert.budgetBook.view.View;

public class FixedTurnoverWizardView implements View, Initializable {

    private final ObjectProperty<ImportInformation> importInformation = new SimpleObjectProperty<>();

    @FXML
    private MoneyTextField valueTextField;
    @FXML
    private ComboBox<TurnoverDirection> directionComboBox;

    @Inject
    public FixedTurnoverWizardView() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directionComboBox.setConverter(Converters.get(TurnoverDirection.class));
        directionComboBox.getItems().setAll(TurnoverDirection.values());

        importInformation.addListener((observable, oldVal, newVal) -> {
            directionComboBox.getSelectionModel().select(TurnoverDirection.valueOf(newVal.amountProperty().getValue()));
            valueTextField.setValue(Math.abs(newVal.amountProperty().getValue()));
        });
    }

    public ObjectProperty<ImportInformation> importInformationProperty() {
        return importInformation;
    }
}
