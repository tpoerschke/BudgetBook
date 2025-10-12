package timkodiert.budgetbook.validation;

import java.util.concurrent.Callable;

import dagger.assisted.AssistedFactory;
import javafx.beans.Observable;
import javafx.scene.control.Control;

@AssistedFactory
public interface CustomValidationFactory {
    CustomValidation create(Control control, Callable<ValidationResult> validationSupplier, Observable... observables);
}
