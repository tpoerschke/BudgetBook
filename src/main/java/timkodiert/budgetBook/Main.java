package timkodiert.budgetBook;

import atlantafx.base.theme.Theme;
import javafx.application.Application;
import javafx.stage.Stage;

import timkodiert.budgetBook.util.PropertiesService;
import timkodiert.budgetBook.view.DaggerViewComponent;
import timkodiert.budgetBook.view.ViewComponent;

/**
 * Hello world!
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PropertiesService.getInstance().load();
        Class<? extends Theme> theme = PropertiesService.getInstance().getTheme();
        Application.setUserAgentStylesheet(theme.getConstructor().newInstance().getUserAgentStylesheet());
        ViewComponent viewComponent = DaggerViewComponent.create();
        viewComponent.getMainView().setAndShowPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
