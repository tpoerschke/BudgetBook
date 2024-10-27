package timkodiert.budgetBook.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetBook.dialog.StackTraceAlert;
import timkodiert.budgetBook.domain.util.EntityManager;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.injector.ControllerFactory;
import timkodiert.budgetBook.injector.ViewComponent;
import timkodiert.budgetBook.properties.PropertiesService;

import static timkodiert.budgetBook.view.FxmlResource.ANNUAL_OVERVIEW;
import static timkodiert.budgetBook.view.FxmlResource.IMPORT_VIEW;
import static timkodiert.budgetBook.view.FxmlResource.MAIN_VIEW;
import static timkodiert.budgetBook.view.FxmlResource.MANAGE_REGULAR_TURNOVER_VIEW;
import static timkodiert.budgetBook.view.FxmlResource.MANAGE_UNIQUE_TURNOVER_VIEW;
import static timkodiert.budgetBook.view.FxmlResource.MONTHLY_OVERVIEW;

@Singleton
public class MainViewImpl implements Initializable, MainView {

    private Stage primaryStage;

    @FXML
    private BorderPane root;
    @FXML
    private MenuBar menuBar;

    @FXML
    private RadioMenuItem viewMenuItem1, viewMenuItem2;

    private final ViewComponent viewComponent;
    private final ControllerFactory controllerFactory;
    private final EntityManager entityManager;
    private final PropertiesService propertiesService;
    private final LanguageManager languageManager;

    @Inject
    public MainViewImpl(ViewComponent viewComponent, ControllerFactory controllerFactory, EntityManager entityManager, PropertiesService propertiesService, LanguageManager languageManager) {
        this.viewComponent = viewComponent;
        this.controllerFactory = controllerFactory;
        this.entityManager = entityManager;
        this.propertiesService = propertiesService;
        this.languageManager = languageManager;
    }

    public void setAndShowPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(windowEvent -> entityManager.closeSession());

        try {
            FXMLLoader templateLoader = new FXMLLoader();
            templateLoader.setResources(languageManager.getResourceBundle());
            templateLoader.setLocation(getClass().getResource(MAIN_VIEW.toString()));
            templateLoader.setController(this);
            Scene scene = new Scene(templateLoader.load());
            scene.getStylesheets().add(getClass().getResource("/css/general-styles.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/validation.css").toExternalForm());
            this.primaryStage.setScene(scene);
            this.primaryStage.show();
        } catch (Exception e) {
            StackTraceAlert.of(languageManager.get("alert.mainViewCouldNotBeOpened"), e).showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean useSystemMenuBar = "true".equals(propertiesService.getProperties().getProperty("useSystemMenuBar", "false")); // <- das mach ich noch besser ... bestimmt :D
        menuBar.useSystemMenuBarProperty().set(useSystemMenuBar);

        // Das Kind laden (default)
        loadViewPartial(MONTHLY_OVERVIEW);
    }

    private String getVersion() {
        return "Version " + getClass().getPackage().getImplementationVersion();
    }

    @Override
    public @Nullable View loadViewPartial(FxmlResource resource) {
        try {
            FXMLLoader templateLoader = new FXMLLoader();
            templateLoader.setLocation(getClass().getResource(resource.getPath()));
            templateLoader.setControllerFactory(controllerFactory::create);
            templateLoader.setResources(languageManager.getResourceBundle());
            this.root.setCenter(templateLoader.load());
            this.primaryStage.setTitle(String.format("%s – JBudgetBook – %s", languageManager.get(resource.getStageTitle()), getVersion()));
            return templateLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceAlert.of(languageManager.get("alert.viewCouldNotBeOpened"), e).showAndWait();
        }
        return null;
    }

    @FXML
    public void showMonthlyOverview(ActionEvent event) {
        loadViewPartial(MONTHLY_OVERVIEW);
    }

    @FXML
    public void showAnnualOverview(ActionEvent event) {
        loadViewPartial(ANNUAL_OVERVIEW);
    }

    @FXML
    private void openManageExpensesView(ActionEvent event) {
        loadViewPartial(MANAGE_REGULAR_TURNOVER_VIEW);
    }

    @FXML
    private void openUniqueExpensesManageView(ActionEvent event) {
        loadViewPartial(MANAGE_UNIQUE_TURNOVER_VIEW);
    }

    @FXML
    public void openImportView(ActionEvent event) {
        View view = loadViewPartial(IMPORT_VIEW);
        if (view instanceof ImportView importView) {
            importView.setMainView(this);
        }
    }

    @FXML
    private void openManageCategoriesView(ActionEvent event) {
        loadViewPartial(FxmlResource.MANAGE_CATEGORY_VIEW);
    }

    @FXML
    private void openManageCategoryGroupsView(ActionEvent event) {
        loadViewPartial(FxmlResource.CATEGORY_GROUP_MANAGE_VIEW);
    }

    @FXML
    private void openSettingsView() {
        propertiesService.buildWindow().showAndWait();
    }


    @FXML
    private void onDragOver(DragEvent e) {
        Dragboard dragboard = e.getDragboard();
        if (dragboard.hasFiles() && dragboard.getFiles().stream().anyMatch(this::isCsvFile)) {
            e.acceptTransferModes(TransferMode.COPY);
        }
        e.consume();
    }

    private boolean isCsvFile(File file) {
        String name = file.getName();
        return "csv".equals(name.substring(name.lastIndexOf('.') + 1));
    }

    @FXML
    private void onDragDropped(DragEvent e) {
        View view = loadViewPartial(IMPORT_VIEW);
        if (view instanceof ImportView importView) {
            importView.setMainView(this);
            e.getDragboard().getFiles().stream().filter(this::isCsvFile).findFirst().ifPresent(importView.selectedFileProperty()::set);
        }
    }
}
