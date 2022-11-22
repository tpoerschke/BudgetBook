package timkodiert.budgetBook.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import atlantafx.base.util.DoubleStringConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timkodiert.budgetBook.controller.FixedExpenseController;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;
import timkodiert.budgetBook.util.CurrencyTableCell;
import timkodiert.budgetBook.util.EntityManager;

public class MainView implements Initializable {
    
    @FXML
    private TableColumn<ExpenseAdapter, String> monthlyPositionCol, monthlyTypeCol, currentMonthPositionCol, currentMonthTypeCol, nextMonthPositionCol, nextMonthTypeCol;
    @FXML
    private TableColumn<ExpenseAdapter, Double> monthlyValueCol, currentMonthValueCol, nextMonthValueCol;
    @FXML
    private TableView<ExpenseAdapter> monthlyTable, currentMonthTable, nextMonthTable;

    @FXML 
    private Label monthlySumLabel, nextMonthSumLabel, nextMonthTotalSumLabel;

    private Stage primaryStage;

    private FixedExpenseController fixedExpenseController;

    public MainView(Stage primaryStage) throws IOException {
        this.fixedExpenseController = new FixedExpenseController();
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
        monthlyPositionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        monthlyPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        monthlyValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("currentMonthValue"));
        monthlyValueCol.setCellFactory(col -> new CurrencyTableCell<>());
        monthlyTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().typeProperty().get().getType()));

        currentMonthPositionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        currentMonthPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        currentMonthValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("currentMonthValue"));
        currentMonthValueCol.setCellFactory(col -> new CurrencyTableCell<>());
        currentMonthTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().typeProperty().get().getType()));

        nextMonthPositionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        nextMonthPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nextMonthValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("nextMonthValue"));
        nextMonthValueCol.setCellFactory(col -> new CurrencyTableCell<>());
        nextMonthTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().typeProperty().get().getType()));

        // Ausgaben anzeigen
        monthlyTable.setItems(fixedExpenseController.getMonthlyExpenses());
        currentMonthTable.setItems(fixedExpenseController.getCurrentMonthExpenses());
        nextMonthTable.setItems(fixedExpenseController.getNextMonthExpenses());

        // Summen
        monthlySumLabel.textProperty().bind(fixedExpenseController.monthlyExpensesSumTextProperty());
        nextMonthSumLabel.textProperty().bind(fixedExpenseController.nextMonthExpensesSumTextProperty());
        nextMonthTotalSumLabel.textProperty().bind(fixedExpenseController.nextMonthExpensesTotalSumTextProperty());
    }

    @FXML
    private void openNewExpenseView(ActionEvent event) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(this.primaryStage);
            // Das Template laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewExpense.fxml"));
            Parent parent = (Parent)loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @FXML
    private void openAnnualOverviewView(ActionEvent event) {
        try {
            Stage stage = new Stage();
            // Das Template laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AnnualOverview.fxml"));
            Parent parent = (Parent)loader.load();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
        }
    }

    @FXML
    private void reloadData(ActionEvent event) {
        fixedExpenseController.loadAll();
    }
}
