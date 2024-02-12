package timkodiert.budgetBook;

import atlantafx.base.theme.Theme;
import javafx.application.Application;
import javafx.stage.Stage;
import org.flywaydb.core.Flyway;

import timkodiert.budgetBook.util.OperationMode;
import timkodiert.budgetBook.util.PropertiesService;
import timkodiert.budgetBook.view.DaggerViewComponent;
import timkodiert.budgetBook.view.ViewComponent;

import static timkodiert.budgetBook.Constants.OPERATION_MODE_ARGUMENT_NAME;

/**
 * Hello world!
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PropertiesService propsService = PropertiesService.getInstance();
        Parameters params = getParameters();
        if (params.getNamed().containsKey(OPERATION_MODE_ARGUMENT_NAME)) {
            OperationMode operationMode = OperationMode.valueOf(params.getNamed().get(OPERATION_MODE_ARGUMENT_NAME));
            propsService.setOperationMode(operationMode);
        }
        propsService.load();

        Flyway flyway = Flyway.configure().dataSource(propsService.getDbPath(), "sa", "").load();
        flyway.migrate();

        Class<? extends Theme> theme = propsService.getTheme();
        Application.setUserAgentStylesheet(theme.getConstructor().newInstance().getUserAgentStylesheet());
        ViewComponent viewComponent = DaggerViewComponent.create();
        viewComponent.getMainView().setAndShowPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
