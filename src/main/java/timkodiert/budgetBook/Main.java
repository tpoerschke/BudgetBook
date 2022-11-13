package timkodiert.budgetBook;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.stage.Stage;
import timkodiert.budgetBook.view.MainView;

/**
 * Hello world!
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        new MainView(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
