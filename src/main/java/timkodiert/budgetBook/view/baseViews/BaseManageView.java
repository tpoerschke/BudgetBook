package timkodiert.budgetBook.view.baseViews;

import java.io.IOException;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import timkodiert.budgetBook.domain.model.Adaptable;
import timkodiert.budgetBook.domain.model.Adapter;
import timkodiert.budgetBook.domain.model.ContentEquals;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.util.DialogFactory;
import timkodiert.budgetBook.view.View;

public abstract class BaseManageView<T extends Adaptable<A> & ContentEquals, A extends Adapter<T>> implements View, Initializable {

    @FXML
    protected Pane detailViewContainer;

    @FXML
    protected TableView<A> entityTable;

    protected final Repository<T> repository;
    private final Supplier<T> emptyEntityProducer;
    private final EntityBaseDetailView<T> detailView;
    private final DialogFactory dialogFactory;

    public BaseManageView(Supplier<T> emptyEntityProducer, Repository<T> repository,
            EntityBaseDetailView<T> detailView, DialogFactory dialogFactory) {
        this.emptyEntityProducer = emptyEntityProducer;
        this.repository = repository;
        this.dialogFactory = dialogFactory;
        this.detailView = detailView;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reloadTable();
        detailView.setOnUpdate(this::reloadTable);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(detailView.getFxmlLocation()));
        loader.setController(detailView);
        try {
            detailViewContainer.getChildren().add(loader.load());
        } catch (IOException ioe) {
            Alert alert = new Alert(AlertType.ERROR, "Ansicht konnte nicht geöffnet werden!");
            alert.showAndWait();
            ioe.printStackTrace();
        }

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

    protected void displayNewEntity() {
        detailView.setEntity(emptyEntityProducer.get());
    }

    private void reloadTable() {
        entityTable.getItems().setAll(repository.findAll().stream().map(T::getAdapter).toList());
        entityTable.sort();
    }

}
