package timkodiert.budgetBook.view.baseViews;

import java.util.function.Supplier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.Setter;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.util.EntityManager;

public abstract class EntityBaseDetailView<T> extends BaseDetailView<T> {

    @Setter
    protected Runnable onUpdate;

    protected Repository<T> repository;

    protected EntityBaseDetailView(Supplier<T> emptyEntityProducer, Repository<T> repository) {
        super(emptyEntityProducer);
        this.repository = repository;
    }

    public boolean isDirty() {
        if (this.entity.get() == null) {
            return false;
        }
        // Über Kopie und equals auf dirty prüfen? dann kann die Id auch verwendet werden
        T fromUi = patchEntity(emptyEntityProducer.get());
        System.out.println(entity.get() + " " + entity.get().hashCode());
        System.out.println(fromUi + " " + fromUi.hashCode());
        System.out.println("---");
        return !fromUi.equals(this.entity.get());
    }

    public boolean save() {

        if (!validate()) {
            return false;
        }

        T exp = patchEntity(this.entity.get());
        repository.persist(exp);
        EntityManager.getInstance().refresh(exp);
        onUpdate.run();
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

    public abstract String getFxmlLocation();
}
