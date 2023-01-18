package timkodiert.budgetBook;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.stage.Stage;
import timkodiert.budgetBook.view.DaggerViewComponent;
import timkodiert.budgetBook.view.ViewComponent;

/**
 * Hello world!
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        ViewComponent viewComponent = DaggerViewComponent.create();
        viewComponent.getMainView().setAndShowPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
