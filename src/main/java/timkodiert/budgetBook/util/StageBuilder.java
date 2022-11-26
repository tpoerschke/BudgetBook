package timkodiert.budgetBook.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StageBuilder {

    private Stage stage;

    private StageBuilder() {
        this.stage = new Stage();
    }

    public static StageBuilder create() {
        return new StageBuilder();
    }

    public StageBuilder withModality(Modality modality) {
        this.stage.initModality(modality);
        return this;
    }

    public StageBuilder withOwner(Window owner) {
        this.stage.initOwner(owner);
        return this;
    }

    public StageBuilder withFXMLResource(String resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent parent = (Parent)loader.load();
        Scene scene = new Scene(parent);
        this.stage.setScene(scene);
        return this;
    }

    public Stage build() {
        return this.stage;
    }
}
