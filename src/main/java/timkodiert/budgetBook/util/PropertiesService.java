package timkodiert.budgetBook.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Theme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;

import timkodiert.budgetBook.Constants;
import timkodiert.budgetBook.i18n.LanguageManager;

public class PropertiesService {

    private static PropertiesService INSTANCE;

    @Getter
    private Properties properties;

    private List<ThemeOption> themeComboBoxItems;

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
            this.properties.setProperty("theme", "0");
            this.properties.store(new FileWriter(propsFile), "Store initial props");
        } else {
            this.properties.load(new FileInputStream(Constants.PROPERTIES_PATH));
        }
        initializeDataStructures();
    }

    private void initializeDataStructures() {
        themeComboBoxItems = List.of(
                new ThemeOption("0", LanguageManager.get("settings.comboItem.light"), PrimerLight.class),
                new ThemeOption("1", LanguageManager.get("settings.comboItem.dark"), PrimerDark.class)
        );
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
        languageComboBox.setPromptText(LanguageManager.get("settings.prompt.selectLanguage")); // replace label according to current language
        languageComboBox.setValue(properties.getProperty("language"));
        grid.add(new Label(LanguageManager.get("settings.label.language")), 0, 0);
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

        ComboBox<ThemeOption> themeComboBox = new ComboBox<>();
        themeComboBox.getItems().addAll(themeComboBoxItems);
        themeComboBox.setPromptText(LanguageManager.get("settings.prompt.selectTheme"));
        themeComboBox.setValue(getCurrentThemeOption());
        themeComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(ThemeOption themeOption) {
                return themeOption != null ? themeOption.name() : null;
            }
            @Override
            public ThemeOption fromString(String string) {
                return null;
            }
        });
        grid.add(new Label(LanguageManager.get("settings.label.theme")), 0, 3);
        grid.add(themeComboBox, 1, 3);


        Button saveBtn = new Button(LanguageManager.get("button.save"));
        saveBtn.setOnAction(event -> {
            try {

                Properties newProps = new Properties();
                newProps.setProperty("language", languageComboBox.getValue());
                newProps.setProperty("db", jdbcPathTextField.getText());
                newProps.setProperty("useSystemMenuBar", String.valueOf(useSystemMenuBarCheckBox));
                newProps.setProperty("theme", themeComboBox.getValue().id());
                // Speichern
                File propsFile = Path.of(Constants.PROPERTIES_PATH).toFile();
                this.properties = newProps;
                this.properties.store(new FileWriter(propsFile), "JBudgetBook properties");
                Alert alert = new Alert(AlertType.INFORMATION,
                                        LanguageManager.get("settings.alert.savedPleaseRestart"));
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR, LanguageManager.get("settings.alert.saveError"));
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
        stage.setTitle(LanguageManager.get("settings.stage.title"));
        return stage;
    }

    private ThemeOption getCurrentThemeOption() {
        return themeComboBoxItems
                .stream()
                .filter(e -> e.id()
                              .equals(
                                      properties.getProperty("theme")
                              )
                ).findFirst()
                .orElse(themeComboBoxItems.get(0));
    }

    public Class<? extends Theme> getTheme() {
        return getCurrentThemeOption().theme();
    }

    public static PropertiesService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PropertiesService();
        }
        return INSTANCE;
    }
}

