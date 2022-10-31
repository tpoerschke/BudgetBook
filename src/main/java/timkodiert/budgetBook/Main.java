package timkodiert.budgetBook;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class Main extends Application implements Initializable {

    @FXML
    private TableColumn monthlyPositionCol, monthlyValueCol, monthlyTypeCol;
    @FXML
    private TableView monthlyTable; 

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader templateLoader = new FXMLLoader();
        templateLoader.setLocation(getClass().getResource("/Main.fxml"));
        templateLoader.setController(this);
        primaryStage.setScene(new Scene(templateLoader.load()));
        primaryStage.setTitle("Test");
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final ObservableList<Expense> data = FXCollections.observableArrayList(
            new Expense("Netflix", 7.00, "monatlich"),
            new Expense("Weitere Ausgabe", 4.99, "jährlich")
        );

        monthlyPositionCol.setCellValueFactory(new PropertyValueFactory<Expense, String>("position"));
        monthlyValueCol.setCellValueFactory(new PropertyValueFactory<Expense, Double>("value"));
        monthlyTypeCol.setCellValueFactory(new PropertyValueFactory<Expense, String>("type"));

        monthlyTable.getItems().addAll(data);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
