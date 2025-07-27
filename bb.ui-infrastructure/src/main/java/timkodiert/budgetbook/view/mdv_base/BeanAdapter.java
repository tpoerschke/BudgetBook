package timkodiert.budgetbook.view.mdv_base;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

public class BeanAdapter<B> {

    private final ReadOnlyBooleanWrapper isEmpty = new ReadOnlyBooleanWrapper(true);

    private final Map<String, BeanPropertyContainer<B, ?>> propertyMap = new HashMap<>();

    @Getter
    private B bean;

    @Getter
    private boolean isDirty = false;

    public void setBean(B bean) {
        this.bean = bean;
        isEmpty.setValue(bean == null);

        if(bean != null) {
            propertyMap.values().forEach(propContainer -> propContainer.setValue(bean));
        }
        isDirty = false;
    }

    public ReadOnlyBooleanProperty isEmpty() {
        return isEmpty.getReadOnlyProperty();
    }

    @SuppressWarnings("unchecked")
    public <T> ObjectProperty<T> getProperty(SerializableFunction<B, T> getter, BiConsumer<B, T> setter) {

        String getterName;
        try {
            getterName = resolveMethodName(getter);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return (ObjectProperty<T>) propertyMap.computeIfAbsent(getterName, k -> createProperty(getter, setter)).property();
    }

    private <T> BeanPropertyContainer<B, T> createProperty(Function<B, T> getter, BiConsumer<B, T> setter) {
        ObjectProperty<T> property = new SimpleObjectProperty<>();
        property.addListener((obs, oldVal, newVal) -> {
            if(!Objects.equals(oldVal, newVal)) {
                isDirty = true;
            }
            setter.accept(bean, newVal);
        });
        BeanPropertyContainer<B, T> propContainer = new BeanPropertyContainer<>(property, getter, setter);
        if(bean != null) {
            propContainer.setValue(bean);
        }
        return propContainer;
    }

    private <T> String resolveMethodName(SerializableFunction<B, T> func) throws ReflectiveOperationException {
        Method writeReplace = func.getClass().getDeclaredMethod("writeReplace");
        writeReplace.setAccessible(true);
        SerializedLambda serialized = (SerializedLambda) writeReplace.invoke(func);
        return serialized.getImplMethodName();
    }

    record BeanPropertyContainer<D, T>(ObjectProperty<T> property, Function<D, T> getter, BiConsumer<D, T> setter) {
         void setValue(D bean) {
            property.setValue(getter.apply(bean));
        }
    }

    @FunctionalInterface
    public interface SerializableFunction<B, R> extends Function<B, R>, Serializable {}
}
