package timkodiert.budgetBook.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import timkodiert.budgetBook.Constants;

public class PropertiesService {

    private static PropertiesService INSTANCE;

    @Getter
    private Properties properties;

    private PropertiesService() {
        this.properties = new Properties();
    }

    public void load() throws IOException {
        File propsFile = Path.of(Constants.PROPERTIES_PATH).toFile();
        if (!propsFile.exists()) {
            Path.of(propsFile.getParent()).toFile().mkdirs();
            propsFile.createNewFile();
            this.properties.setProperty("db", "jdbc:sqlite:"
                    + Path.of(System.getProperty("user.home"), Constants.DATA_DIR, "sqlite.db").toString());
            this.properties.setProperty("useSystemMenuBar", "true");
            this.properties.store(new FileWriter(propsFile), "Store initial props");
        } else {
            this.properties.load(new FileInputStream(Constants.PROPERTIES_PATH));
        }
    }

    public Stage buildWindow() {
        List<HBox> propBoxes = properties.entrySet().stream().map(entry -> {
            Label label = new Label(entry.getKey().toString());
            label.getStyleClass().add(Styles.LEFT_PILL);
            label.setPrefWidth(150);
            TextField textField = new TextField(entry.getValue().toString());
            textField.getStyleClass().add(Styles.RIGHT_PILL);
            textField.setPrefWidth(300);
            return new HBox(label, textField);
        }).toList();

        VBox propBoxesContainer = new VBox();
        propBoxesContainer.getChildren().addAll(propBoxes);
        propBoxesContainer.setSpacing(10);

        Button saveBtn = new Button("Speichern");
        saveBtn.setOnAction(event -> {
            Properties newProps = new Properties();
            propBoxes.forEach(box -> {
                // Jede Box hat genau 2 Kinder (Label & TextField) -> s.o.
                String key = ((Label) box.getChildren().get(0)).getText();
                String value = ((TextField) box.getChildren().get(1)).getText();
                newProps.setProperty(key, value);
            });

            // Speichern
            File propsFile = Path.of(Constants.PROPERTIES_PATH).toFile();
            this.properties = newProps;
            try {
                this.properties.store(new FileWriter(propsFile), "JBudgetBook properties");
                Alert alert = new Alert(AlertType.INFORMATION,
                        "Einstellungen wurden gespeichert. Die Anwendung muss neugestartet werden!");
                alert.showAndWait();
            } catch (IOException ioe) {
                Alert alert = new Alert(AlertType.ERROR, "Einstellungen konnten nicht gespeichert werden!");
                alert.showAndWait();
            }
        });

        HBox buttonBox = new HBox(saveBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        BorderPane root = new BorderPane();
        root.setCenter(propBoxesContainer);
        root.setBottom(buttonBox);
        root.setPadding(new Insets(20));

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 500, 300));
        stage.setTitle("Einstellungen");
        return stage;
    }

    public static PropertiesService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PropertiesService();
        }
        return INSTANCE;
    }
}
