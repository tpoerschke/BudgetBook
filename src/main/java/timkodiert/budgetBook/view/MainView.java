package timkodiert.budgetBook.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timkodiert.budgetBook.controller.FixedExpenseController;
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

    @Inject
    public MainView(ViewComponent viewComponennt, FixedExpenseController fixedExpenseController) {
        this.viewComponent = viewComponennt;
        this.fixedExpenseController = fixedExpenseController;
    }

    public void setAndShowPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(windowEvent -> EntityManager.getInstance().closeSession());

        try {
            FXMLLoader templateLoader = new FXMLLoader();
            templateLoader.setLocation(getClass().getResource("/fxml/Main.fxml"));
            templateLoader.setController(this);
            this.primaryStage.setScene(new Scene(templateLoader.load()));
            this.primaryStage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "Hauptansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuBar.useSystemMenuBarProperty().set(true);

        ToggleGroup viewMenuItemToggleGroup = new ToggleGroup();
        viewMenuItem1.setToggleGroup(viewMenuItemToggleGroup);
        viewMenuItem2.setToggleGroup(viewMenuItemToggleGroup);

        viewMenuItem1.setOnAction(event -> {
            viewMenuItem1.setSelected(true);
            loadViewPartial("/fxml/AnnualOverview.fxml", viewComponent.getAnnualOverviewView(), "Jahresübersicht");
        });

        viewMenuItem2.setOnAction(event -> {
            viewMenuItem2.setSelected(true);
            loadViewPartial("/fxml/CompactOverview.fxml", viewComponent.getCompactOverviewView(), "Ausgabenübersicht");
        });

        // Das Kind laden (default)
        loadViewPartial("/fxml/CompactOverview.fxml", viewComponent.getCompactOverviewView(), "Ausgabenübersicht");
        viewMenuItem2.setSelected(true);
    }

    private void loadViewPartial(String resource, View view, String stageTitle) {
        try {
            FXMLLoader templateLoader = new FXMLLoader();
            templateLoader.setLocation(getClass().getResource(resource));
            templateLoader.setController(view);
            this.root.setCenter(templateLoader.load());
            this.primaryStage.setTitle(String.format("%s – JBudgetBook", stageTitle));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @FXML
    public void showMonthlyOverview(ActionEvent event) {
        loadViewPartial("/fxml/MonthlyOverview.fxml", viewComponent.getMonthlyOverview(),
                "Monatsübersicht");
        viewMenuItem1.setSelected(false);
        viewMenuItem2.setSelected(false);
    }

    @FXML
    private void openManageExpensesView(ActionEvent event) {
        loadViewPartial("/fxml/ManageExpenses.fxml", viewComponent.getManageExpensesView(),
                "Regelmäßige Ausgaben verwalten");
        viewMenuItem1.setSelected(false);
        viewMenuItem2.setSelected(false);
    }

    @FXML
    private void openUniqueExpensesManageView(ActionEvent event) {
        loadViewPartial("/fxml/UniqueExpenses/Manage.fxml", viewComponent.getUniqueExpensesManageView(),
                "Einzigartige Ausgaben verwalten");
        viewMenuItem1.setSelected(false);
        viewMenuItem2.setSelected(false);
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
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
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
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @FXML
    private void reloadData(ActionEvent event) {
        fixedExpenseController.loadAll();
        fixedExpenseController.addNextYearToAllExpenses();
    }

    @FXML
    private void openSettingsView() {
        PropertiesService.getInstance().buildWindow().showAndWait();
    }
}
