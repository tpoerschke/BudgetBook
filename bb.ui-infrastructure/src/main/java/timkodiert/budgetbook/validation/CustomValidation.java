package timkodiert.budgetbook.validation;

import java.util.concurrent.Callable;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import lombok.Getter;

public class CustomValidation {

    @Getter
    private final Control control;
    @Getter
    private final Callable<ValidationResult> validationSupplier;

    @Getter
    private final ObjectProperty<ValidationResult> validationResultProperty = new SimpleObjectProperty<>(ValidationResult.valid());

    @AssistedInject
    public CustomValidation(@Assisted Control control,
                            @Assisted Callable<ValidationResult> validationSupplier,
                            @Assisted Observable... observables) {
        this.control = control;
        this.validationSupplier = validationSupplier;
        validationResultProperty.bind(Bindings.createObjectBinding(validationSupplier, observables));
    }
}
