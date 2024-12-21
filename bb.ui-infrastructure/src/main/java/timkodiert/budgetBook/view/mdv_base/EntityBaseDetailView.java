package timkodiert.budgetBook.view.mdv_base;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.Setter;

import timkodiert.budgetBook.domain.model.ContentEquals;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.domain.util.EntityManager;

public abstract class EntityBaseDetailView<T extends ContentEquals> extends BaseDetailView<T> {

    @Setter
    protected Consumer<T> onUpdate;

    protected final Repository<T> repository;
    protected final EntityManager entityManager;

    protected EntityBaseDetailView(Supplier<T> emptyEntityProducer, Repository<T> repository, EntityManager entityManager) {
        super(emptyEntityProducer);
        this.repository = repository;
        this.entityManager = entityManager;
    }

    public boolean isDirty() {
        if (this.entity.get() == null) {
            return false;
        }

        T fromUi = patchEntity(emptyEntityProducer.get(), false);
        return !fromUi.contentEquals(this.entity.get());
    }

    public boolean save() {

        if (!validate()) {
            return false;
        }

        T exp = patchEntity(this.entity.get(), true);
        repository.persist(exp);
        entityManager.refresh(exp);
        onUpdate.accept(exp);
        return true;
    }

    @FXML
    private void discardChanges(ActionEvent event) {
        // Durch erneutes Setzen der Expense werden
        // die Änderungen verworfen.
        setEntity(entity.get());
    }

    @FXML
    private void save(ActionEvent event) {
        save();
    }
}
