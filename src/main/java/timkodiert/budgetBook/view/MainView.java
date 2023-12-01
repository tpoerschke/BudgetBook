package timkodiert.budgetBook.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.inject.Inject;

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

import timkodiert.budgetBook.controller.FixedExpenseController;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.util.PropertiesService;
import timkodiert.budgetBook.util.StageBuilder;

public class MainView implements Initializable {

    private Stage primaryStage;

    @FXML
    private BorderPane root;
    @FXML
    private MenuBar menuBar;

    @FXML
    private RadioMenuItem viewMenuItem1, viewMenuItem2;

    private FixedExpenseController fixedExpenseController;
    private ViewComponent viewComponent;
    private final ControllerFactory controllerFactory;

    @Inject
    public MainView(ViewComponent viewComponennt, FixedExpenseController fixedExpenseController, ControllerFactory controllerFactory) {
        this.viewComponent = viewComponennt;
        this.fixedExpenseController = fixedExpenseController;
        this.controllerFactory = controllerFactory;
    }

    public void setAndShowPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(windowEvent -> EntityManager.getInstance().closeSession());

        try {
            FXMLLoader templateLoader = new FXMLLoader();
            templateLoader.setResources(LanguageManager.getInstance().getResourceBundle());
            templateLoader.setLocation(getClass().getResource("/fxml/Main.fxml"));
            templateLoader.setController(this);
            this.primaryStage.setScene(new Scene(templateLoader.load()));
            this.primaryStage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, LanguageManager.getInstance().getLocString("alert.mainViewCouldNotBeOpened"));
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boolean useSystemMenuBar = "true".equals(PropertiesService.getInstance()
                .getProperties().getProperty("useSystemMenuBar", "false")); // <- das mach ich noch besser ... bestimmt :D
        menuBar.useSystemMenuBarProperty().set(useSystemMenuBar);

        // Das Kind laden (default)
        loadViewPartial("/fxml/MonthlyOverview.fxml", LanguageManager.getInstance().getLocString("stageTitle.monthlyOverView"));
    }

    private String getVersion() {
        return "Version " + getClass().getPackage().getImplementationVersion();
    }

    private @Nullable View loadViewPartial(String resource, String stageTitle) {
        try {
            FXMLLoader templateLoader = new FXMLLoader();
            templateLoader.setLocation(getClass().getResource(resource));
            templateLoader.setControllerFactory(controllerFactory::create);
            templateLoader.setResources(LanguageManager.getInstance().getResourceBundle());
            this.root.setCenter(templateLoader.load());
            this.primaryStage.setTitle(String.format("%s – JBudgetBook – %s", stageTitle, getVersion()));
            return templateLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, LanguageManager.getInstance().getLocString("alert.viewCouldNotBeOpened"));
            alert.showAndWait();
        }
        return null;
    }

    @FXML
    public void showMonthlyOverview(ActionEvent event) {
        loadViewPartial("/fxml/MonthlyOverview.fxml", LanguageManager.getInstance().getLocString("stageTitle.monthlyOverView"));
    }

    @FXML
    public void showAnnualOverview(ActionEvent event) {
        loadViewPartial("/fxml/AnnualOverview.fxml", LanguageManager.getInstance().getLocString("stageTitle.annualOverView"));
    }

    @FXML
    private void openManageExpensesView(ActionEvent event) {
        loadViewPartial("/fxml/ManageExpenses.fxml", LanguageManager.getInstance().getLocString("stageTitle.regularExpensesOverview"));
    }

    @FXML
    private void openUniqueExpensesManageView(ActionEvent event) {
        loadViewPartial("/fxml/UniqueExpenses/Manage.fxml", LanguageManager.getInstance().getLocString("stageTitle.uniqueExpensesOverview"));
    }

    @FXML
    public void openImportView(ActionEvent event) {
        loadViewPartial("/fxml/Importer/ImportView.fxml", LanguageManager.getInstance().getLocString("stageTitle.importView"));
    }

    @FXML
    private void openManageCategoriesView(ActionEvent event) {
        try {
            Stage stage = StageBuilder.create()
                    .withModality(Modality.APPLICATION_MODAL)
                    .withOwner(this.primaryStage)
                    .withFXMLResource("/fxml/ManageCategories.fxml")
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
                    .withFXMLResource("/fxml/NewCategory.fxml")
                    .withView(viewComponent.getNewCategoryView())
                    .build();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, LanguageManager.getInstance().getLocString("alert.viewCouldNotBeOpened"));
            alert.showAndWait();
        }
    }

    @FXML
    private void reloadData(ActionEvent event) {
        fixedExpenseController.loadAll();
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
        View view = loadViewPartial("/fxml/Importer/ImportView.fxml", LanguageManager.getInstance().getLocString("stageTitle.importView"));
        if (view instanceof ImportView importView) {
            e.getDragboard().getFiles().stream().filter(this::isCsvFile).findFirst().ifPresent(importView.selectedFileProperty()::set);
        }
    }
}
