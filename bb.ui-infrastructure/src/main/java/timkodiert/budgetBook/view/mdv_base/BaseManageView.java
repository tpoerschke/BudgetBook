package timkodiert.budgetBook.view.mdv_base;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import timkodiert.budgetBook.dialog.DialogFactory;
import timkodiert.budgetBook.dialog.StackTraceAlert;
import timkodiert.budgetBook.domain.adapter.Adaptable;
import timkodiert.budgetBook.domain.adapter.Adapter;
import timkodiert.budgetBook.domain.model.BaseEntity;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.view.BbFxmlLoader;
import timkodiert.budgetBook.view.View;

public abstract class BaseManageView<T extends BaseEntity & Adaptable<A>, A extends Adapter<T>> implements View, Initializable {

    @FXML
    protected Pane detailViewContainer;

    @FXML
    protected TableView<A> entityTable;

    protected final Repository<T> repository;
    private final Supplier<T> emptyEntityProducer;
    private final BbFxmlLoader fxmlLoader;
    private final DialogFactory dialogFactory;
    private final LanguageManager languageManager;

    private EntityBaseDetailView<T> detailView;

    public BaseManageView(Supplier<T> emptyEntityProducer,
                          Repository<T> repository,
                          BbFxmlLoader fxmlLoader,
                          DialogFactory dialogFactory,
                          LanguageManager languageManager) {
        this.emptyEntityProducer = emptyEntityProducer;
        this.repository = repository;
        this.dialogFactory = dialogFactory;
        this.fxmlLoader = fxmlLoader;
        this.languageManager = languageManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reloadTable();

        fxmlLoader.setLocation(getClass().getResource(getDetailViewFxmlLocation()));
        try {
            detailViewContainer.getChildren().add(fxmlLoader.load());
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceAlert.of(languageManager.get("alert.viewCouldNotBeOpened"), e).showAndWait();
            return;
        }

        detailView = fxmlLoader.getController();
        detailView.setOnUpdate(this::reloadTable);

        entityTable.setRowFactory(tableView -> {
            TableRow<A> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    if (detailView.isDirty()) {
                        Alert alert = dialogFactory.buildConfirmationDialog();
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get().equals(DialogFactory.CANCEL)) {
                            entityTable.getSelectionModel().select(detailView.getEntity().get().getAdapter());
                            return;
                        }
                        if (result.get().equals(DialogFactory.SAVE_CHANGES) && !detailView.save()) {
                            entityTable.getSelectionModel().select(detailView.getEntity().get().getAdapter());
                            return;
                        }
                    }

                    detailView.setEntity(row.getItem().getBean());
                }
            });
            return row;
        });

        initControls();
    }

    public void displayEntityById(int id) {
        repository.findAll().stream().filter(e -> e.getId() == id).findAny().ifPresent(detailView::setEntity);
    }

    protected abstract void initControls();

    protected abstract String getDetailViewFxmlLocation();

    protected void displayNewEntity() {
        detailView.setEntity(emptyEntityProducer.get());
    }

    private void reloadTable() {
        entityTable.getItems().setAll(repository.findAll().stream().map(T::getAdapter).toList());
        entityTable.sort();
    }

}
