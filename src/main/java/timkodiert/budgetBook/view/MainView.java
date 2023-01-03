package timkodiert.budgetBook.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timkodiert.budgetBook.controller.FixedExpenseController;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;
import timkodiert.budgetBook.util.CurrencyTableCell;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.util.StageBuilder;

public class MainView implements Initializable {

    private Stage primaryStage;

    @FXML
    private BorderPane root;

    public MainView(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(windowEvent -> EntityManager.getInstance().closeSession());

        FXMLLoader templateLoader = new FXMLLoader();
        templateLoader.setLocation(getClass().getResource("/fxml/Main.fxml"));
        templateLoader.setController(this);
        primaryStage.setScene(new Scene(templateLoader.load()));
        primaryStage.setTitle("Ausgabenübersicht");
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Das Kind laden
        try {
            FXMLLoader templateLoader = new FXMLLoader();
            templateLoader.setLocation(getClass().getResource("/fxml/CompactOverview.fxml"));
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
            Stage stage = StageBuilder.create().withFXMLResource("/fxml/AnnualOverview.fxml").build();
            stage.show();
        } catch(Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @FXML
    private void reloadData(ActionEvent event) {
        // TODO: Über Dependency Injection müsste das doch funktionieren, oder?
        // Wenn sich alle Views den Controller teilen, könnte man hier aktualisieren
        // und es wirkt sich auf alle anderen Views aus.
        //fixedExpenseController.loadAll();
        //fixedExpenseController.addNextYearToAllExpenses();
    }
}
