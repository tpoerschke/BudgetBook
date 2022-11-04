package timkodiert.budgetBook;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import timkodiert.budgetBook.domain.model.Expense;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;
import timkodiert.budgetBook.domain.model.FixedExpense;

/**
 * Hello world!
 *
 */
public class Main extends Application implements Initializable {

    @FXML
    private TableColumn<ExpenseAdapter, String> monthlyPositionCol, monthlyTypeCol, nextMonthPositionCol, nextMonthTypeCol;
    @FXML
    private TableColumn<ExpenseAdapter, Double> monthlyValueCol, nextMonthValueCol;
    @FXML
    private TableView<ExpenseAdapter> monthlyTable, nextMonthTable;

    @FXML 
    private Label monthlySumLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        FXMLLoader templateLoader = new FXMLLoader();
        templateLoader.setLocation(getClass().getResource("/Main.fxml"));
        templateLoader.setController(this);
        primaryStage.setScene(new Scene(templateLoader.load()));
        primaryStage.setTitle("Test");
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        monthlyPositionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        monthlyPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        monthlyValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("value"));
        monthlyValueCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        monthlyTypeCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("type"));

        nextMonthPositionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        nextMonthPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nextMonthValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("value"));
        nextMonthValueCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        nextMonthTypeCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("type"));

        List<FixedExpense> expenses = List.of(
            new FixedExpense(1, "Netflix", 7.00, "monatlich", List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)),
            new FixedExpense(2, "Weitere Ausgabe", 4.99, "halbjährlich", List.of(6, 12))
        );

        // Monatliche Ausgaben anzeigen
        final ObservableList<ExpenseAdapter> data = FXCollections.observableArrayList(expenses.stream().map(Expense::getAdapter).toList());
        monthlyTable.getItems().addAll(data);

        double monthlySum = expenses.stream().mapToDouble(Expense::getValue).sum();
        monthlySumLabel.setText(monthlySum + "€");

        // Ausgaben des nächsten Monats anzeigen
        int nextMonth = LocalDate.now().plusMonths(1).getMonth().getValue();
        List<FixedExpense> expensesNextMonth = expenses
            .stream()
            .filter(exp -> exp.getType() != "monatlich")
            .filter(exp -> exp.getDatesOfPayment().contains(nextMonth))
            .toList();

        final ObservableList<ExpenseAdapter> nextMonthData = FXCollections.observableArrayList(expensesNextMonth.stream().map(Expense::getAdapter).toList());
        nextMonthTable.getItems().addAll(nextMonthData);

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

        try {
            Stage stage = new Stage();
            // Das Template laden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewExpense.fxml"));
            Parent parent = (Parent) loader.load();

            Scene scene = new Scene(parent, 600, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
