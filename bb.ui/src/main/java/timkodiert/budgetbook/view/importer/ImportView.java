package timkodiert.budgetbook.view.importer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Provider;

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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.util.converter.DefaultStringConverter;
import lombok.Setter;

import timkodiert.budgetbook.converter.FixedExpenseStringConverter;
import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.repository.Repository;
import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.importer.ImportInformation;
import timkodiert.budgetbook.importer.TurnoverImporter;
import timkodiert.budgetbook.table.cell.CurrencyTableCell;
import timkodiert.budgetbook.table.cell.DateTableCell;
import timkodiert.budgetbook.util.StageBuilder;
import timkodiert.budgetbook.view.FxmlResource;
import timkodiert.budgetbook.view.MainView;
import timkodiert.budgetbook.view.View;

import static timkodiert.budgetbook.util.StageBuilder.StageTuple;
import static timkodiert.budgetbook.view.FxmlResource.MONTHLY_OVERVIEW;

public class ImportView implements View, Initializable {

    private final LanguageManager languageManager;
    private final Repository<FixedTurnover> fixedExpenseRepository;
    private final TurnoverImporter importer;
    private final DialogFactory dialogFactory;
    private final Provider<StageBuilder> stageBuilderProvider;

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
    private TableColumn<ImportInformation, FixedTurnover> associatedCol;
    @FXML
    public TableColumn<ImportInformation, String> annotationCol;

    @FXML
    private StackPane root;
    @FXML
    private Label filePathLabel;

    private final ObjectProperty<File> selectedFile = new SimpleObjectProperty<>();

    private final CheckBox selectAll = new CheckBox();
    private final BooleanProperty allSelected = new SimpleBooleanProperty();

    private final ObservableList<FixedTurnover> fixedTurnovers = FXCollections.observableArrayList();

    @Setter
    private MainView mainView;

    @Inject
    public ImportView(LanguageManager languageManager,
                      Repository<FixedTurnover> fixedExpenseRepository,
                      TurnoverImporter importer,
                      DialogFactory dialogFactory,
                      Provider<StageBuilder> stageBuilderProvider) {
        this.languageManager = languageManager;
        this.fixedExpenseRepository = fixedExpenseRepository;
        this.importer = importer;
        this.dialogFactory = dialogFactory;
        this.stageBuilderProvider = stageBuilderProvider;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reloadFixedTurnover();

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
        associatedCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(new FixedExpenseStringConverter(), fixedTurnovers));
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

        MenuItem menuItem = new MenuItem("Wiederkehrenden Umsatz anlegen");
        menuItem.setOnAction(this::openWizard);
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(menuItem);
        importTable.contextMenuProperty()
                   .bind(Bindings.when(importTable.getSelectionModel().selectedItemProperty().isNotNull()).then(contextMenu).otherwise((ContextMenu) null));

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

    private void reloadFixedTurnover() {
        List<FixedTurnover> turnovers = new ArrayList<>();
        turnovers.add(null);
        turnovers.addAll(fixedExpenseRepository.findAll());
        fixedTurnovers.setAll(turnovers);
    }

    public ObjectProperty<File> selectedFileProperty() {
        return selectedFile;
    }

    private void readFile() {

        try {
            ObservableList<ImportInformation> importInformation = importer.parse(selectedFile.get())
                                                                          .linkWithExpenses()
                                                                          .filterDuplicates()
                                                                          .getImportInformationList();
            importTable.setItems(importInformation);

            if (importInformation.isEmpty()) {
                displayNotification(Styles.WARNING, languageManager.get("alert.noTurnOversFoundInFile_maybeNotCompatible"));
            }
        } catch (Exception e) {
            //            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            //            alert.showAndWait();
            displayNotification(Styles.DANGER, e.getMessage());
        }
    }

    private void openWizard(ActionEvent event) {
        try {
            StageTuple wizardTuple = stageBuilderProvider.get()
                                                         .withModality(Modality.APPLICATION_MODAL)
                                                         .withFXMLResource(FxmlResource.FIXED_TURNOVER_WIZARD_VIEW.getPath())
                                                         .build();
            FixedTurnoverWizardView wizardView = (FixedTurnoverWizardView) wizardTuple.view();
            wizardView.setOnSaveCallback(this::selectCreatedFixedTurnover);
            wizardView.importInformationProperty().set(importTable.getSelectionModel().getSelectedItem());
            wizardTuple.stage().showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void selectCreatedFixedTurnover(FixedTurnover turnover) {
        reloadFixedTurnover();
        importTable.getSelectionModel().getSelectedItem().fixedExpenseProperty().set(turnover);
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(languageManager.get("fileChooser.description.csv-files"), "*.csv"));
        File file = fileChooser.showOpenDialog(((Node) e.getSource()).getScene().getWindow());
        selectedFile.set(file);
    }

    @FXML
    private void importSelected(ActionEvent e) {
        importer.doImport();
        dialogFactory.buildInformationDialog(languageManager.get("importView.dialog.expensesImported")).showAndWait();
        mainView.loadViewPartial(MONTHLY_OVERVIEW);
    }
}
