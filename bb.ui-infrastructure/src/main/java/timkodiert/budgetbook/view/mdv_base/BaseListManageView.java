package timkodiert.budgetbook.view.mdv_base;

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
import javafx.scene.input.MouseEvent;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetbook.dialog.DialogFactory;
import timkodiert.budgetbook.domain.adapter.Adaptable;
import timkodiert.budgetbook.domain.adapter.Adapter;
import timkodiert.budgetbook.domain.model.BaseEntity;
import timkodiert.budgetbook.domain.repository.Repository;
import timkodiert.budgetbook.i18n.LanguageManager;

public abstract class BaseListManageView<T extends BaseEntity & Adaptable<A>, A extends Adapter<T>> extends BaseManageView<T, A> {

    @FXML
    protected TableView<A> entityTable;

    protected final Repository<T> repository;
    private final DialogFactory dialogFactory;

    protected BaseListManageView(Supplier<T> emptyEntityProducer,
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
            row.setOnMouseClicked(event -> handleMouseClickOnTableRow(event, row));
            return row;
        });

        initControls();
    }

    private void handleMouseClickOnTableRow(MouseEvent event, TableRow<A> row) {
        if (event.getClickCount() != 1 || row.isEmpty()) {
            return;
        }
        if (!detailView.isDirty()) {
            detailView.setEntity(row.getItem().getBean());
            return;
        }

        Alert alert = dialogFactory.buildConfirmationDialog();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElseThrow().equals(DialogFactory.CANCEL)) {
            entityTable.getSelectionModel().select(detailView.getEntity().get().getAdapter());
            return;
        }
        if (result.orElseThrow().equals(DialogFactory.SAVE_CHANGES) && !detailView.save()) {
            entityTable.getSelectionModel().select(detailView.getEntity().get().getAdapter());
            return;
        }
        detailView.setEntity(row.getItem().getBean());
    }

    @Override
    protected void reloadTable(@Nullable T updatedEntity) {
        entityTable.getItems().setAll(repository.findAll().stream().map(T::getAdapter).toList());
        entityTable.sort();
    }

}
