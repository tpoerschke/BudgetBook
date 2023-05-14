package timkodiert.budgetBook.view;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import lombok.Getter;
import lombok.Setter;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.util.EntityManager;
import timkodiert.budgetBook.validation.ValidationWrapper;

public abstract class BaseDetailView<T> implements View {

    @Setter
    protected Runnable onUpdate;

    protected Map<String, Control> validationMap = new HashMap<>();

    protected Supplier<T> emptyEntityProducer;
    protected Repository<T> repository;

    @Getter
    protected ObjectProperty<T> entity = new SimpleObjectProperty<>();

    protected BaseDetailView(Supplier<T> emptyEntityProducer, Repository<T> repository) {
        this.emptyEntityProducer = emptyEntityProducer;
        this.repository = repository;
    }

    public void setEntity(T entity) {
        this.entity.set(entity);

        if (entity == null) {
            return;
        }

        patchUi(entity);
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

    protected boolean validate() {
        T entityToValidate = patchEntity(emptyEntityProducer.get());

        ValidationWrapper<T> validation = new ValidationWrapper<>(validationMap);
        if (!validation.validate(entityToValidate)) {
            return false;
        }
        return true;
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

    protected abstract T patchEntity(T entity);

    protected abstract void patchUi(T entity);
}
