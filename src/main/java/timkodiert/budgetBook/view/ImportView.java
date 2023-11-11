package timkodiert.budgetBook.view;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javax.inject.Inject;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.converter.DefaultStringConverter;

import timkodiert.budgetBook.domain.model.AccountTurnover;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.importer.ImportInformation;
import timkodiert.budgetBook.importer.TurnoverImporter;
import timkodiert.budgetBook.table.cell.CurrencyTableCell;
import timkodiert.budgetBook.table.cell.DateTableCell;
import timkodiert.budgetBook.util.FixedExpenseStringConverter;

public class ImportView implements View, Initializable {

    private final Repository<FixedExpense> fixedExpenseRepository;
    private final Repository<AccountTurnover> accountTurnoverRepository;

    @FXML
    private TableView<ImportInformation> importTable;
    @FXML
    private TableColumn<ImportInformation, Boolean> selectedCol;
    @FXML
    private TableColumn<ImportInformation, String> receiverCol, referenceCol, postingTextCol;
    @FXML
    private TableColumn<ImportInformation, LocalDate> dateCol;
    @FXML
    private TableColumn<ImportInformation, Number> amountCol;
    @FXML
    private TableColumn<ImportInformation, FixedExpense> associatedCol;
    @FXML
    public TableColumn<ImportInformation, String> annotationCol;

    @FXML
    private StackPane root;
    @FXML
    private Label filePathLabel;

    private final ObjectProperty<File> selectedFile = new SimpleObjectProperty<>();

    private final CheckBox selectAll = new CheckBox();
    private final BooleanProperty allSelected = new SimpleBooleanProperty();

    @Inject
    public ImportView(Repository<FixedExpense> fixedExpenseRepository, Repository<AccountTurnover> accountTurnoverRepository) {
        this.fixedExpenseRepository = fixedExpenseRepository;
        this.accountTurnoverRepository = accountTurnoverRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<FixedExpense> fixedExpenses = FXCollections.observableArrayList();
        fixedExpenses.add(null);
        fixedExpenses.addAll(fixedExpenseRepository.findAll());

        selectedCol.setCellValueFactory(new PropertyValueFactory<>("selectedForImport"));
        selectedCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectedCol));
        selectedCol.setGraphic(selectAll);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(cellData -> new DateTableCell<>());
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setCellFactory(col -> new CurrencyTableCell<>());
        receiverCol.setCellValueFactory(new PropertyValueFactory<>("receiver"));
        receiverCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        postingTextCol.setCellValueFactory(new PropertyValueFactory<>("postingText"));
        referenceCol.setCellValueFactory(new PropertyValueFactory<>("reference"));
        associatedCol.setCellValueFactory(new PropertyValueFactory<>("fixedExpense"));
        associatedCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(new FixedExpenseStringConverter(), fixedExpenses));
        annotationCol.setCellValueFactory(new PropertyValueFactory<>("annotation"));

        importTable.getItems().addListener((ListChangeListener<? super ImportInformation>) change -> {
            if (allSelected.isBound()) {
                allSelected.unbind();
            }

            List<BooleanProperty> importObservables = importTable.getItems()
                                                                 .stream()
                                                                 .map(ImportInformation::selectedForImportProperty)
                                                                 .toList();
            allSelected.bind(Bindings.createBooleanBinding(() -> importObservables.stream().allMatch(BooleanProperty::get),
                                                           importObservables.toArray(Observable[]::new)));
        });

        ChangeListener<Boolean> selectAllListener = (observableValue, oldVal, newVal) -> {
            importTable.getItems()
                       .stream()
                       .map(ImportInformation::selectedForImportProperty)
                       .forEach(prop -> prop.setValue(newVal));
        };
        selectAll.selectedProperty().addListener(selectAllListener);

        allSelected.addListener((observableValue, oldVal, newVal) -> {
            selectAll.selectedProperty().removeListener(selectAllListener);
            selectAll.setSelected(newVal);
            selectAll.selectedProperty().addListener(selectAllListener);
        });

        selectedFile.addListener((observableValue, oldVal, newVal) -> {
            if (newVal == null) {
                filePathLabel.setText("");
                return;
            }
            filePathLabel.setText(newVal.getAbsolutePath());
            readFile();
        });
    }

    public ObjectProperty<File> selectedFileProperty() {
        return selectedFile;
    }

    private void readFile() {
        TurnoverImporter importer = new TurnoverImporter();
        try {
            List<ImportInformation> importInformation = importer.parse(selectedFile.get());
            importTable.getItems().setAll(importInformation);

            if (importInformation.isEmpty()) {
                displayNotification(Styles.WARNING,
                                    "Keine Umsätze in der Datei gefunden. Möglicherweise ist die Struktur der Datei nicht kompatibel.");
            }
        } catch (Exception e) {
            //            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            //            alert.showAndWait();
            displayNotification(Styles.DANGER, e.getMessage());
        }
    }

    private void displayNotification(String style, String text) {
        Notification notification = new Notification(text);
        notification.setPrefHeight(150.0);
        notification.setPrefWidth(300.0);
        notification.setMaxHeight(Region.USE_PREF_SIZE);
        notification.setMaxWidth(Region.USE_PREF_SIZE);
        notification.getStyleClass().add(style);
        notification.setOnClose(event -> {
            root.getChildren().remove(notification);
        });
        StackPane.setAlignment(notification, Pos.TOP_RIGHT);
        StackPane.setMargin(notification, new Insets(20, 20, 20, 20));
        root.getChildren().add(notification);
    }

    @FXML
    private void onSelectFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Dateien", "*.csv"));
        File file = fileChooser.showOpenDialog(((Node) e.getSource()).getScene().getWindow());
        selectedFile.set(file);
    }

    @FXML
    private void importSelected(ActionEvent e) {
        List<ImportInformation> importInformation = importTable.getItems().stream().filter(ImportInformation::isSelectedForImport).toList();

        // TODO 01.11.23: Automatische Zuordnung via ImportRules

        List<AccountTurnover> importsWithFixedExpense = importInformation.stream()
                                                                         .filter(ImportInformation::hasFixedExpense)
                                                                         .map(ImportInformation::accountTurnoverWithFixedExpense)
                                                                         .toList();

        List<AccountTurnover> importsWithUniqueExpense = importInformation.stream()
                                                                          .filter(Predicate.not(ImportInformation::hasFixedExpense))
                                                                          .map(ImportInformation::accountTurnoverWithUniqueExpense)
                                                                          .toList();

        accountTurnoverRepository.persist(importsWithFixedExpense);
        accountTurnoverRepository.persist(importsWithUniqueExpense);
    }
}