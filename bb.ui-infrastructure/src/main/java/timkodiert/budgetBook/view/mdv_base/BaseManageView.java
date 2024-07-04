package timkodiert.budgetBook.view.mdv_base;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import timkodiert.budgetBook.dialog.StackTraceAlert;
import timkodiert.budgetBook.domain.adapter.Adaptable;
import timkodiert.budgetBook.domain.adapter.Adapter;
import timkodiert.budgetBook.domain.model.BaseEntity;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.view.View;

public abstract class BaseManageView<T extends BaseEntity & Adaptable<A>, A extends Adapter<T>> implements View, Initializable {

    @FXML
    protected Pane detailViewContainer;

    private final FXMLLoader fxmlLoader;
    private final LanguageManager languageManager;
    private final Repository<T> repository;
    private final Supplier<T> emptyEntityProducer;

    protected EntityBaseDetailView<T> detailView;

    protected BaseManageView(FXMLLoader fxmlLoader, LanguageManager languageManager, Repository<T> repository, Supplier<T> emptyEntityProducer) {
        this.fxmlLoader = fxmlLoader;
        this.languageManager = languageManager;
        this.repository = repository;
        this.emptyEntityProducer = emptyEntityProducer;
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

        initControls();
    }

    public void displayEntityById(int id) {
        repository.findAll().stream().filter(e -> e.getId() == id).findAny().ifPresent(detailView::setEntity);
    }

    protected void displayNewEntity() {
        detailView.setEntity(emptyEntityProducer.get());
    }

    protected abstract void initControls();

    protected abstract void reloadTable();

    protected abstract String getDetailViewFxmlLocation();
}
