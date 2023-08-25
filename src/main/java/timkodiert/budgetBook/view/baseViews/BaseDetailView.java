package timkodiert.budgetBook.view.baseViews;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import timkodiert.budgetBook.validation.ValidationWrapper;
import timkodiert.budgetBook.view.View;

@RequiredArgsConstructor
public abstract class BaseDetailView<T> implements View {

    protected final Map<String, Control> validationMap = new HashMap<>();

    protected final Supplier<T> emptyEntityProducer;

    @Getter
    protected final ObjectProperty<T> entity = new SimpleObjectProperty<>();

    public void setEntity(T entity) {
        this.entity.set(entity);

        if (entity == null) {
            return;
        }

        patchUi(entity);
    }

    protected boolean validate() {
        T entityToValidate = patchEntity(emptyEntityProducer.get());

        ValidationWrapper<T> validation = new ValidationWrapper<>(validationMap);
        if (!validation.validate(entityToValidate)) {
            return false;
        }
        return true;
    }

    protected abstract T patchEntity(T entity);

    protected abstract void patchUi(T entity);
}
