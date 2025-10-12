package timkodiert.budgetbook.validation;

import java.util.concurrent.Callable;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import jakarta.validation.MessageInterpolator;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import lombok.Getter;

public class CustomValidation {

    private final MessageInterpolator messageInterpolator;

    @Getter
    private final Control control;
    @Getter
    private final Callable<ValidationResult> validationSupplier;

    @Getter
    private final ObjectProperty<ValidationResult> validationResultProperty = new SimpleObjectProperty<>(ValidationResult.valid());

    @AssistedInject
    public CustomValidation(MessageInterpolator messageInterpolator,
                            @Assisted Control control,
                            @Assisted Callable<ValidationResult> validationSupplier,
                            @Assisted Observable... observables) {
        this.messageInterpolator = messageInterpolator;
        this.control = control;
        this.validationSupplier = validationSupplier;
        validationResultProperty.bind(Bindings.createObjectBinding(validationSupplier, observables));
        validationResultProperty.addListener((observable, oldValue, newValue) -> {
            //tooltipHandler(newValue);
        });
    }

    //    private void tooltipHandler(ValidationResult validationResult) {
    //        if (validationResult == null || validationResult.getType() == ValidationResult.ResultType.VALID) {
    //            control.setTooltip(null);
    //            control.pseudoClassStateChanged(Styles.STATE_DANGER, false);
    //            return;
    //        }
    //        control.setTooltip(new Tooltip(messageInterpolator.interpolate(validationResult.getMessage(), new MessageInterpolatorContext())));
    //        control.pseudoClassStateChanged(Styles.STATE_DANGER, true);
    //    }
}
