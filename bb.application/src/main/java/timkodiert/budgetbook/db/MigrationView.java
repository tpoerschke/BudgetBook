package timkodiert.budgetbook.db;

import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.view.View;

public class MigrationView implements View, Initializable {

    @FXML
    private Label progressIndicatorText;
    @FXML
    private Label progressLabel;
    @FXML
    private Button startButton;

    private final MigrationService service;
    private final LanguageManager languageManager;
    private final DialogFactory dialogFactory;

    @Inject
    public MigrationView(MigrationService service, LanguageManager languageManager, DialogFactory dialogFactory) {
        this.service = service;
        this.languageManager = languageManager;
        this.dialogFactory = dialogFactory;
    }

    @FXML
    private void startMigration(ActionEvent event) {
        startButton.setDisable(true);
        service.migrate();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        progressLabel.textProperty().bind(service.currentScriptProperty());
        progressIndicatorText.textProperty()
                             .bind(Bindings.createStringBinding(() -> String.format("%d / %d", service.numMigratedProperty().get(), service.getPendingCount()),
                                                                service.numMigratedProperty()));

        service.migrationFinishedProperty().addListener((observableValue, oldVal, newVal) -> {
            getStage().close();
        });

        service.migrationErrorProperty().addListener((observableValue, oldVal, newVal) -> {
            dialogFactory.buildErrorDialog(languageManager.get("migration.error")).showAndWait();
            Platform.exit();
            System.exit(1);
        });
    }

    private Stage getStage() {
        return ((Stage) startButton.getScene().getWindow());
    }
}
