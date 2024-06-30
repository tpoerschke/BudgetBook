package timkodiert.budgetBook;

import atlantafx.base.theme.Theme;
import javafx.application.Application;
import javafx.stage.Stage;

import timkodiert.budgetBook.converter.Converters;
import timkodiert.budgetBook.db.MigrationService;
import timkodiert.budgetBook.injector.DaggerViewComponent;
import timkodiert.budgetBook.injector.ViewComponent;
import timkodiert.budgetBook.properties.OperationMode;
import timkodiert.budgetBook.properties.PropertiesServiceImpl;

import static timkodiert.budgetBook.Constants.OPERATION_MODE_ARGUMENT_NAME;

/**
 * Hello world!
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewComponent viewComponent = DaggerViewComponent.create();

        PropertiesServiceImpl propsService = viewComponent.getPropertiesService();
        Parameters params = getParameters();
        if (params.getNamed().containsKey(OPERATION_MODE_ARGUMENT_NAME)) {
            OperationMode operationMode = OperationMode.valueOf(params.getNamed().get(OPERATION_MODE_ARGUMENT_NAME));
            propsService.setOperationMode(operationMode);
        }
        propsService.load();

        Class<? extends Theme> theme = propsService.getTheme();
        Application.setUserAgentStylesheet(theme.getConstructor().newInstance().getUserAgentStylesheet());

        Converters converters = viewComponent.getConverters();
        converters.register();

        // Migration & Programmstart
        MigrationService migrationService = viewComponent.getMigrationService();
        if (migrationService.hasPendingMigrations()) {
            migrationService.show();
        }
        viewComponent.getMainView().setAndShowPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
