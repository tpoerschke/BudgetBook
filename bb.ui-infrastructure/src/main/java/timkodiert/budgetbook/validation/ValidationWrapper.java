package timkodiert.budgetbook.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import atlantafx.base.theme.Styles;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;

import timkodiert.budgetbook.exception.TechnicalException;
import timkodiert.budgetbook.view.mdv_base.BeanAdapter;

public class ValidationWrapper<T> {

    private static final String STYLE_CLASS_ERROR = "validation-error"; // Für Controls, für die die PseudoClass (s.u.) nicht funktioniert

    private final MessageInterpolator messageInterpolator;
    private final CustomValidationFactory customValidationFactory;
    private final BeanAdapter<T> beanAdapter;
    private final Map<String, Control> propertyNodeMap;
    private final Map<String, CustomValidation> customValidationMap = new HashMap<>();

    @AssistedInject
    public ValidationWrapper(CustomValidationFactory customValidationFactory,
                             MessageInterpolator messageInterpolator,
                             @Assisted Map<String, Control> propertyNodeMap,
                             @Assisted BeanAdapter<T> beanAdapter) {
        this.customValidationFactory = customValidationFactory;
        this.messageInterpolator = messageInterpolator;
        this.beanAdapter = beanAdapter;
        this.propertyNodeMap = propertyNodeMap;
    }

    public boolean validate() {
        propertyNodeMap.values().forEach(control -> {
            control.getStyleClass().remove(STYLE_CLASS_ERROR);
            control.pseudoClassStateChanged(Styles.STATE_DANGER, false);
            control.setTooltip(null);
        });
        customValidationMap.values().forEach(customValidation -> {
            customValidation.getControl().getStyleClass().remove(STYLE_CLASS_ERROR);
            customValidation.getControl().pseudoClassStateChanged(Styles.STATE_DANGER, false);
            customValidation.getControl().setTooltip(null);
        });

        Validator validator = Validation.byDefaultProvider().configure().messageInterpolator(messageInterpolator).buildValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(beanAdapter.getBean());

        Map<Control, List<String>> controlsWithMessages = new HashMap<>();
        violations.forEach(violation -> {
            String prop = violation.getPropertyPath().toString();
            if (propertyNodeMap.containsKey(prop)) {
                controlsWithMessages.computeIfAbsent(propertyNodeMap.get(prop), c -> new ArrayList<>()).add(violation.getMessage());
            }
        });

        customValidationMap.forEach((name, customValidation) -> {
            try {
                ValidationResult result = customValidation.getValidationSupplier().call();
                if (result.getType() != ValidationResult.ResultType.VALID) {
                    String interpolatedMessage = messageInterpolator.interpolate(result.getMessage(), new EmptyInterpolatorContext());
                    controlsWithMessages.computeIfAbsent(customValidation.getControl(), c -> new ArrayList<>()).add(interpolatedMessage);
                }
            } catch (Exception e) {
                throw TechnicalException.forProgrammingError(e.getMessage(), e);
            }
        });
        controlsWithMessages.forEach((control, messages) -> {
            control.setTooltip(new Tooltip(String.join("\n", messages)));
            if (control instanceof TableView<?> tv) {
                tv.getStyleClass().add(STYLE_CLASS_ERROR);
            } else {
                control.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            }
        });
        return controlsWithMessages.isEmpty();
    }

    public void register(Observable... observables) {
        Arrays.stream(observables).forEach(observable -> observable.addListener(o -> Platform.runLater(this::validate)));
    }

    public void registerCustomValidation(String name, Control control, Callable<ValidationResult> validationSupplier, Observable... observables) {
        CustomValidation customValidation = customValidationFactory.create(control, validationSupplier, observables);
        customValidationMap.put(name, customValidation);
        customValidation.getValidationResultProperty().addListener(observable -> validate());
    }
}
