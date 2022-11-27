package timkodiert.budgetBook.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import timkodiert.budgetBook.view.View;

public class StageBuilder {

    private Window owner;
    private Modality modality;
    private String resourcePath;
    private View viewController;    
    private String title;

    private StageBuilder() {
    }

    public static StageBuilder create() {
        return new StageBuilder();
    }

    public StageBuilder withModality(Modality modality) {
        this.modality = modality;
        return this;
    }

    public StageBuilder withOwner(Window owner) {
        this.owner = owner;
        return this;
    }

    public StageBuilder withFXMLResource(String resourcePath)  {
        this.resourcePath = resourcePath;
        return this;
    }

    public StageBuilder withView(View viewController) {
        this.viewController = viewController;
        return this;
    }

    public StageBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public Stage build() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
        loader.setController(viewController);

        Parent parent = (Parent)loader.load();
        Scene scene = new Scene(parent);

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.initModality(modality);
        stage.initOwner(owner);

        return stage;
    }
}
