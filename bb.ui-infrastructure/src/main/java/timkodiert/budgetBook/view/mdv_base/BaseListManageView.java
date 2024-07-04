package timkodiert.budgetBook.view.mdv_base;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import timkodiert.budgetBook.dialog.DialogFactory;
import timkodiert.budgetBook.domain.adapter.Adaptable;
import timkodiert.budgetBook.domain.adapter.Adapter;
import timkodiert.budgetBook.domain.model.BaseEntity;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.i18n.LanguageManager;

public abstract class BaseListManageView<T extends BaseEntity & Adaptable<A>, A extends Adapter<T>> extends BaseManageView<T, A> {

    @FXML
    protected TableView<A> entityTable;

    protected final Repository<T> repository;
    private final DialogFactory dialogFactory;

    public BaseListManageView(Supplier<T> emptyEntityProducer,
                              Repository<T> repository,
                              FXMLLoader fxmlLoader,
                              DialogFactory dialogFactory,
                              LanguageManager languageManager) {
        super(fxmlLoader, languageManager, repository, emptyEntityProducer);
        this.repository = repository;
        this.dialogFactory = dialogFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

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

    @Override
    protected void reloadTable() {
        entityTable.getItems().setAll(repository.findAll().stream().map(T::getAdapter).toList());
        entityTable.sort();
    }

}
