package timkodiert.budgetBook.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import atlantafx.base.util.DoubleStringConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import timkodiert.budgetBook.domain.model.Expense;
import timkodiert.budgetBook.domain.model.ExpenseAdapter;
import timkodiert.budgetBook.domain.model.ExpenseType;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.util.EntityManager;

public class MainView implements Initializable {
    
    @FXML
    private TableColumn<ExpenseAdapter, String> monthlyPositionCol, monthlyTypeCol, nextMonthPositionCol, nextMonthTypeCol;
    @FXML
    private TableColumn<ExpenseAdapter, Double> monthlyValueCol, nextMonthValueCol;
    @FXML
    private TableView<ExpenseAdapter> monthlyTable, nextMonthTable;

    @FXML 
    private Label monthlySumLabel;

    private Stage primaryStage;

    private EntityManager entityManager;

    public MainView(Stage primaryStage) throws IOException {
        this.entityManager = EntityManager.getInstance();
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
        monthlyValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("value"));
        monthlyValueCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        monthlyTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().typeProperty().get().getType()));

        nextMonthPositionCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, String>("position"));
        nextMonthPositionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nextMonthValueCol.setCellValueFactory(new PropertyValueFactory<ExpenseAdapter, Double>("value"));
        nextMonthValueCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        nextMonthTypeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().typeProperty().get().getType()));

        List<FixedExpense> expenses = List.of(
            new FixedExpense("Netflix", 7.00, ExpenseType.MONTHLY, List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)),
            new FixedExpense("Weitere Ausgabe", 4.99, ExpenseType.SEMIANNUAL, List.of(6, 12))
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
            .filter(exp -> !exp.getType().equals(ExpenseType.MONTHLY))
            .filter(exp -> exp.getDatesOfPayment().contains(nextMonth))
            .toList();

        final ObservableList<ExpenseAdapter> nextMonthData = FXCollections.observableArrayList(expensesNextMonth.stream().map(Expense::getAdapter).toList());
        nextMonthTable.getItems().addAll(nextMonthData);

        // TODO: Persist sollte Liste akzeptieren
        entityManager.persist(expenses.get(0));
        entityManager.persist(expenses.get(1));
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
    private void reloadData(ActionEvent event) {
        EntityManager em = EntityManager.getInstance();
        List<FixedExpense> expenses = em.findAll(FixedExpense.class);

        final ObservableList<ExpenseAdapter> data = FXCollections.observableArrayList(expenses.stream().map(Expense::getAdapter).toList());
        monthlyTable.getItems().setAll(data);
    }
}
