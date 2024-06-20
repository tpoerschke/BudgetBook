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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.util.PropertiesService;
import timkodiert.budgetBook.util.StageBuilder;
import timkodiert.budgetBook.util.dialog.StackTraceAlert;

import static timkodiert.budgetBook.view.FxmlResource.ANNUAL_OVERVIEW;
import static timkodiert.budgetBook.view.FxmlResource.IMPORT_VIEW;
import static timkodiert.budgetBook.view.FxmlResource.MAIN_VIEW;
import static timkodiert.budgetBook.view.FxmlResource.MANAGE_CATEGORIES_VIEW;
import static timkodiert.budgetBook.view.FxmlResource.MANAGE_REGULAR_TURNOVER_VIEW;
import static timkodiert.budgetBook.view.FxmlResource.MANAGE_UNIQUE_TURNOVER_VIEW;
import static timkodiert.budgetBook.view.FxmlResource.MONTHLY_OVERVIEW;
import static timkodiert.budgetBook.view.FxmlResource.NEW_CATEGORY_VIEW;

@Singleton
public class MainView implements Initializable {

    private Stage primaryStage;

    @FXML
    private BorderPane root;
    @FXML
    private MenuBar menuBar;

    @FXML
    private RadioMenuItem viewMenuItem1, viewMenuItem2;

    private ViewComponent viewComponent;
    private final ControllerFactory controllerFactory;

    @Inject
    public MainView(ViewComponent viewComponennt, ControllerFactory controllerFactory) {
        this.viewComponent = viewComponennt;
        this.controllerFactory = controllerFactory;
    }

    public void setAndShowPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(windowEvent -> EntityManager.getInstance().closeSession());

        try {
            FXMLLoader templateLoader = new FXMLLoader();
            templateLoader.setResources(LanguageManager.getInstance().getResourceBundle());
            templateLoader.setLocation(getClass().getResource(MAIN_VIEW.toString()));
            templateLoader.setController(this);
            this.primaryStage.setScene(new Scene(templateLoader.load()));
            this.primaryStage.show();
        } catch (Exception e) {
            StackTraceAlert.of(LanguageManager.get("alert.mainViewCouldNotBeOpened"), e).showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean useSystemMenuBar = "true".equals(PropertiesService.getInstance()
                .getProperties().getProperty("useSystemMenuBar", "false")); // <- das mach ich noch besser ... bestimmt :D
        menuBar.useSystemMenuBarProperty().set(useSystemMenuBar);

        // Das Kind laden (default)
        loadViewPartial(MONTHLY_OVERVIEW);
    }

    private String getVersion() {
        return "Version " + getClass().getPackage().getImplementationVersion();
    }

    public @Nullable View loadViewPartial(FxmlResource resource) {
        try {
            FXMLLoader templateLoader = new FXMLLoader();
            templateLoader.setLocation(getClass().getResource(resource.getPath()));
            templateLoader.setControllerFactory(controllerFactory::create);
            templateLoader.setResources(LanguageManager.getInstance().getResourceBundle());
            this.root.setCenter(templateLoader.load());
            this.primaryStage.setTitle(String.format("%s – JBudgetBook – %s", LanguageManager.get(resource.getStageTitle()), getVersion()));
            return templateLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceAlert.of(LanguageManager.get("alert.viewCouldNotBeOpened"), e).showAndWait();
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
        try {
            Stage stage = StageBuilder.create()
                    .withModality(Modality.APPLICATION_MODAL)
                    .withOwner(this.primaryStage)
                    .withFXMLResource(MANAGE_CATEGORIES_VIEW.toString())
                    .withView(viewComponent.getManageCategoriesView())
                    .build();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, LanguageManager.getInstance().getLocString("alert.viewCouldNotBeOpened"));
            alert.showAndWait();
        }
    }

    @FXML
    private void openNewCategoryView(ActionEvent event) {
        try {
            Stage stage = StageBuilder.create()
                    .withModality(Modality.APPLICATION_MODAL)
                    .withOwner(this.primaryStage)
                    .withFXMLResource(NEW_CATEGORY_VIEW.toString())
                    .withView(viewComponent.getNewCategoryView())
                    .build();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, LanguageManager.getInstance().getLocString("alert.viewCouldNotBeOpened"));
            alert.showAndWait();
        }
    }

    @FXML
    private void openSettingsView() {
        PropertiesService.getInstance().buildWindow().showAndWait();
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
