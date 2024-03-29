package timkodiert.budgetBook.view.mdv_base;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import timkodiert.budgetBook.domain.model.Adaptable;
import timkodiert.budgetBook.domain.model.Adapter;
import timkodiert.budgetBook.domain.model.ContentEquals;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.util.dialog.DialogFactory;
import timkodiert.budgetBook.util.dialog.StackTraceAlert;
import timkodiert.budgetBook.view.ControllerFactory;
import timkodiert.budgetBook.view.View;

public abstract class BaseManageView<T extends Adaptable<A> & ContentEquals, A extends Adapter<T>> implements View, Initializable {

    @FXML
    protected Pane detailViewContainer;

    @FXML
    protected TableView<A> entityTable;

    protected final Repository<T> repository;
    private final Supplier<T> emptyEntityProducer;
    private final ControllerFactory controllerFactory;
    private final DialogFactory dialogFactory;

    private EntityBaseDetailView<T> detailView;

    public BaseManageView(Supplier<T> emptyEntityProducer,
                          Repository<T> repository,
                          ControllerFactory controllerFactory,
                          DialogFactory dialogFactory) {
        this.emptyEntityProducer = emptyEntityProducer;
        this.repository = repository;
        this.dialogFactory = dialogFactory;
        this.controllerFactory = controllerFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reloadTable();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(getDetailViewFxmlLocation()));
        loader.setControllerFactory(controllerFactory::create);
        loader.setResources(LanguageManager.getInstance().getResourceBundle());
        try {
            detailViewContainer.getChildren().add(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
            StackTraceAlert.of(LanguageManager.get("alert.viewCouldNotBeOpened"), e).showAndWait();
            return;
        }

        detailView = loader.getController();
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
