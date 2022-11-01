package timkodiert.budgetBook;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

/**
 * Hello world!
 *
 */
public class Main extends Application implements Initializable {

    @FXML
    private TableColumn<ExpenseAdapter, String> monthlyPositionCol, monthlyTypeCol;
    @FXML
    private TableColumn<ExpenseAdapter, Double> monthlyValueCol;
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

        List<Expense> expenses = List.of(
            new Expense(1, "Netflix", 7.00, "monatlich"),
            new Expense(2, "Weitere Ausgabe", 4.99, "jährlich")
        );

        final ObservableList<ExpenseAdapter> data = FXCollections.observableArrayList(expenses.get(0).getAdapter(), expenses.get(1).getAdapter());

        monthlyPositionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        monthlyPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        monthlyValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("value"));
        monthlyValueCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        monthlyTypeCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("type"));

        monthlyTable.getItems().addAll(data);

        expenses.get(0).setPosition("TEST");

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();

        try {
            SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(expenses.get(0));
            session.persist(expenses.get(1));
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble
            // building the SessionFactory
            // so destroy it manually.
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
