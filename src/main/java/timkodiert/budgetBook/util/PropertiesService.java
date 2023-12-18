package timkodiert.budgetBook.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
            this.properties.setProperty("language", "Deutsch");
            this.properties.store(new FileWriter(propsFile), "Store initial props");
        } else {
            this.properties.load(new FileInputStream(Constants.PROPERTIES_PATH));
        }
    }

    public void verifySettings() throws IOException {
        // TODO
        // Throw exceptions if settings are broken and offer a regeneration of the settings file.
    }

    public Stage buildWindow() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // language dropdown
        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("Deutsch", "English");
        languageComboBox.setPromptText("Select Language"); // replace label according to current language
        languageComboBox.setValue(properties.getProperty("language"));
        grid.add(new Label("Language"), 0, 0);
        grid.add(languageComboBox, 1, 0);

        // DB JDBC Path TextField
        TextField jdbcPathTextField = new TextField();
        jdbcPathTextField.setPromptText("Enter JDBC Path");
        jdbcPathTextField.setText(properties.getProperty("db"));
        grid.add(new Label("DB JDBC Path:"), 0, 1);
        grid.add(jdbcPathTextField, 1, 1);

        // Use System Menu Bar CheckBox
        CheckBox useSystemMenuBarCheckBox = new CheckBox("Use System Menu Bar");
        useSystemMenuBarCheckBox.setSelected(Boolean.parseBoolean(properties.getProperty("useSystemMenuBar")));
        grid.add(useSystemMenuBarCheckBox, 0, 2, 2, 1);


        Button saveBtn = new Button("Speichern");
        saveBtn.setOnAction(event -> {
            try {

                Properties newProps = new Properties();
                newProps.setProperty("language", languageComboBox.getValue());
                newProps.setProperty("db", jdbcPathTextField.getText());
                newProps.setProperty("useSystemMenuBar", String.valueOf(useSystemMenuBarCheckBox));
                // Speichern
                File propsFile = Path.of(Constants.PROPERTIES_PATH).toFile();
                this.properties = newProps;
                this.properties.store(new FileWriter(propsFile), "JBudgetBook properties");
                Alert alert = new Alert(AlertType.INFORMATION,
                                        "Einstellungen wurden gespeichert. Die Anwendung muss neugestartet werden!");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR, "Einstellungen konnten nicht gespeichert werden!");
                alert.showAndWait();
            }
        });

        HBox buttonBox = new HBox(saveBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        BorderPane root = new BorderPane();
        root.setCenter(grid);
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
