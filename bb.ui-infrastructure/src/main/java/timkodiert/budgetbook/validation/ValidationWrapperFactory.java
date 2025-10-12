package timkodiert.budgetbook.validation;

import java.util.Map;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import javafx.scene.control.Control;

import timkodiert.budgetbook.view.mdv_base.BeanAdapter;

@AssistedFactory
public interface ValidationWrapperFactory<T> {
    ValidationWrapper<T> create(@Assisted Map<String, Control> propertyNodeMap, @Assisted BeanAdapter<T> beanAdapter);
}
