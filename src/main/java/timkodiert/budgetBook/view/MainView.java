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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timkodiert.budgetBook.controller.FixedExpenseController;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.util.StageBuilder;

public class MainView implements Initializable {

    private Stage primaryStage;

    @FXML
    private BorderPane root;

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
            this.primaryStage.setTitle("Ausgabenübersicht");
            this.primaryStage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "Hauptansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Das Kind laden
        try {
            FXMLLoader templateLoader = new FXMLLoader();
            templateLoader.setLocation(getClass().getResource("/fxml/CompactOverview.fxml"));
            templateLoader.setController(viewComponent.getCompactOverviewView());
            this.root.setCenter(templateLoader.load());
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @FXML
    private void openNewExpenseView(ActionEvent event) {
        try {
            Stage stage = StageBuilder.create()
                .withModality(Modality.APPLICATION_MODAL)
                .withOwner(this.primaryStage)
                .withFXMLResource("/fxml/NewExpense.fxml")
                .build();
            stage.show();
        } catch(Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @FXML
    private void openManageCategoriesView(ActionEvent event) {
        try {
            Stage stage = StageBuilder.create()
                .withModality(Modality.APPLICATION_MODAL)
                .withOwner(this.primaryStage)
                .withFXMLResource("/fxml/ManageCategories.fxml")
                .build();
            stage.show();
        } catch(Exception e) {
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
                .build();
            stage.show();
        } catch(Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @FXML
    private void openAnnualOverviewView(ActionEvent event) {
        try {
            View view = viewComponent.getAnnualOverviewView();
            Stage stage = StageBuilder.create().withFXMLResource("/fxml/AnnualOverview.fxml").withView(view).build();
            stage.show();
        } catch(Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @FXML
    private void reloadData(ActionEvent event) {
        fixedExpenseController.loadAll();
        fixedExpenseController.addNextYearToAllExpenses();
    }
}
