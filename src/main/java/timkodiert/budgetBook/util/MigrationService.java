package timkodiert.budgetBook.util;

import javax.inject.Inject;
import javax.inject.Singleton;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.util.dialog.StackTraceAlert;
import timkodiert.budgetBook.view.ControllerFactory;
import timkodiert.budgetBook.view.ViewComponent;

@Singleton
public class MigrationService {

    private final ViewComponent viewComponent;
    private final ControllerFactory controllerFactory;

    @Inject
    public MigrationService(ViewComponent viewComponent, ControllerFactory controllerFactory) {
        this.viewComponent = viewComponent;
        this.controllerFactory = controllerFactory;
    }

    public void show() {
        try {
            FXMLLoader viewLoader = new FXMLLoader();
            viewLoader.setLocation(getClass().getResource("/fxml/MigrationView.fxml"));
            viewLoader.setControllerFactory(controllerFactory::create);
            viewLoader.setResources(LanguageManager.getInstance().getResourceBundle());

            Stage stage = new Stage();
            stage.setTitle("Migration – JBudgetBook");
            stage.setScene(new Scene(viewLoader.load()));
            stage.setWidth(600);
            stage.showAndWait();
        } catch (Exception e) {
            StackTraceAlert.of(LanguageManager.get("alert.viewCouldNotBeOpened"), e).showAndWait();
        }
    }

}
