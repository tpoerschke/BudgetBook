package timkodiert.budgetBook.view;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.IntStream;
import javax.inject.Inject;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.CurrencyStringConverter;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.MonthYear;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.util.CategoryTreeHelper;
import timkodiert.budgetBook.view.widget.MonthYearPickerWidget;

import static timkodiert.budgetBook.util.CategoryTreeHelper.from;

public class NewExpenseView implements Initializable, View {

    @FXML
    private TextField positionTextField, valueTextField;

    @FXML
    private TextArea noteTextField;

    @FXML
    private ChoiceBox<String> typeChoiceBox, month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox;

    @FXML
    private Label month1Label, month2Label, month3Label, month4Label;

    @FXML
    private TreeView<Category> categoriesTreeView;
    private CategoryTreeHelper categoryTreeHelper;

    @FXML
    private HBox widgetContainer;

    private Repository<FixedExpense> expensesRepository;
    private Repository<Category> categoriesRepository;

    private MonthYearPickerWidget startMonthWidget, endMonthWidget;

    @Inject
    public NewExpenseView(Repository<FixedExpense> expensesRepository, Repository<Category> categoiesRepository) {
        this.expensesRepository = expensesRepository;
        this.categoriesRepository = categoiesRepository;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // ValueTextField initialisieren
        valueTextField.setTextFormatter(
                new TextFormatter<>(new CurrencyStringConverter(NumberFormat.getInstance(Locale.GERMAN))));

        // Monatsauswahlen initialisieren
        List.of(month1ChoiceBox,month2ChoiceBox,month3ChoiceBox,month4ChoiceBox).forEach(e->e.getItems().addAll(FXCollections.observableArrayList(LanguageManager.getInstance().getMonths())));

        month1ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener((ObservableValue<? extends Number> obs, Number oldIndex, Number newIndex) -> {
                    int month = newIndex.intValue();
                    if (typeChoiceBox.getSelectionModel().getSelectedItem().equals(LanguageManager.getInstance().getLocString("time.half-yearly"))) {
                        month2ChoiceBox.getSelectionModel().select(((month + 6) % 12));
                    } else if (typeChoiceBox.getSelectionModel().getSelectedItem().equals("vierteljährlich")) {
                        month2ChoiceBox.getSelectionModel().select(((month + 3) % 12));
                        month3ChoiceBox.getSelectionModel().select(((month + 6) % 12));
                        month4ChoiceBox.getSelectionModel().select(((month + 9) % 12));
                    }
                });

        List<String> typeList = List.of(
            LanguageManager.getInstance().getLocString("time.monthly"),
            LanguageManager.getInstance().getLocString("time.yearly"),
            LanguageManager.getInstance().getLocString("time.half-yearly"),
            LanguageManager.getInstance().getLocString("time.quarterly"));
        typeChoiceBox.getItems().addAll(typeList);
        // Monatsboxen mit ChangeListener ausblenden
        typeChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> obsValue, String oldValue, String newValue) -> {
                    switch (newValue) {
                        case "jährlich":
                            manageChoiceBoxes(List.of(month1Label, month1ChoiceBox), List.of(month2Label,
                                    month2ChoiceBox, month3Label, month3ChoiceBox, month4Label, month4ChoiceBox));
                            break;
                        case "halbjährlich":
                            manageChoiceBoxes(List.of(month1Label, month1ChoiceBox, month2Label, month2ChoiceBox),
                                    List.of(month3Label, month3ChoiceBox, month4Label, month4ChoiceBox));
                            break;
                        case "monatlich":
                            manageChoiceBoxes(List.of(), List.of(month1Label, month1ChoiceBox, month2Label,
                                    month2ChoiceBox, month3Label, month3ChoiceBox, month4Label, month4ChoiceBox));
                            break;
                        default:
                            // Alles anzeigen
                            manageChoiceBoxes(List.of(month1Label, month1ChoiceBox, month2Label, month2ChoiceBox,
                                    month3Label, month3ChoiceBox, month4Label, month4ChoiceBox), List.of());
                    }
                });
        typeChoiceBox.getSelectionModel().select(0);

        // Kategorieauswahl initialisieren
        List<Category> categories = categoriesRepository.findAll();
        categoryTreeHelper = from(categoriesTreeView, categories);

        startMonthWidget = MonthYearPickerWidget.builder()
                .labelStr("Erster Monat")
                .parent(widgetContainer)
                .initialValue(MonthYear.now())
                .build();
        endMonthWidget = MonthYearPickerWidget.builder()
                .labelStr("Letzter Monat (optional)")
                .parent(widgetContainer)
                .showResetBtn(true)
                .build();
    }

    @FXML
    private void createNewExpense(ActionEvent e) {
        positionTextField.getStyleClass().remove("validation-error");
        valueTextField.getStyleClass().remove("validation-error");
        typeChoiceBox.getStyleClass().remove("validation-error");
        month1ChoiceBox.getStyleClass().remove("validation-error");
        month2ChoiceBox.getStyleClass().remove("validation-error");
        month3ChoiceBox.getStyleClass().remove("validation-error");
        month4ChoiceBox.getStyleClass().remove("validation-error");

        String position = positionTextField.getText().trim();
        double value = Double.parseDouble(valueTextField.getText().replace(".", "").replace(",", "."));
        List<Integer> datesOfPayment = IntStream.rangeClosed(1, 12).boxed().toList();
        if (!typeChoiceBox.getSelectionModel().getSelectedItem().equals(LanguageManager.getInstance().getLocString("time.monthly"))) {
            datesOfPayment = List.of(month1ChoiceBox, month2ChoiceBox, month3ChoiceBox, month4ChoiceBox)
                    .stream()
                    .map(box -> box.getSelectionModel().getSelectedIndex())
                    .filter(selectedIndex -> selectedIndex > -1)
                    .map(selectedIndex -> selectedIndex + 1)
                    .toList();
        }

        PaymentType type = PaymentType.fromString(typeChoiceBox.getSelectionModel().getSelectedItem());
        List<Category> categories = categoryTreeHelper.getSelectedCategories();

        FixedExpense newExpense = new FixedExpense(position, value, type, datesOfPayment, startMonthWidget.getValue(),
                endMonthWidget.getValue());
        newExpense.getCategories().addAll(categories);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<FixedExpense>> violations = validator.validate(newExpense);
        if (!violations.isEmpty()) {
            violations.stream().forEach(violation -> {
                switch (violation.getPropertyPath().toString()) {
                    case "position":
                        positionTextField.getStyleClass().add("validation-error");
                        break;
                    case "value":
                        valueTextField.getStyleClass().add("validation-error");
                        break;
                    case "type":
                        typeChoiceBox.getStyleClass().add("validation-error");
                        break;
                    case "datesOfPayment":
                        month1ChoiceBox.getStyleClass().add("validation-error");
                        month2ChoiceBox.getStyleClass().add("validation-error");
                        month3ChoiceBox.getStyleClass().add("validation-error");
                        month4ChoiceBox.getStyleClass().add("validation-error");
                        break;
                }
            });
        } else {
            expensesRepository.persist(newExpense);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText(LanguageManager.get("alert.expenseAdded").formatted(newExpense.getPosition()));
            alert.showAndWait();
        }
    }

    @FXML
    private void closeWindow(ActionEvent e) {
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    private void manageChoiceBoxes(List<Control> elementsToShow, List<Control> elementsToHide) {
        elementsToHide.forEach(el -> el.setVisible(false));
        elementsToShow.forEach(el -> el.setVisible(true));
    }
}
