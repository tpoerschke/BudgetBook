package timkodiert.budgetBook.validation;

import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;

public class ValidationWrapper<T> {

    private static final String VALIDATION_ERROR_STYLE = "validation-error";

    private final Map<String, Control> propertyNodeMap;

    public ValidationWrapper(Map<String, Control> propertyNodeMap) {
        this.propertyNodeMap = propertyNodeMap;
    }

    public boolean validate(T entity) {
        propertyNodeMap.values().forEach(node -> {
            node.getStyleClass().remove(VALIDATION_ERROR_STYLE);
            node.setTooltip(null);
        });

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            violations.stream().forEach(violation -> {
                String prop = violation.getPropertyPath().toString();
                if (propertyNodeMap.containsKey(prop)) {
                    propertyNodeMap.get(prop).getStyleClass().add(VALIDATION_ERROR_STYLE);
                    propertyNodeMap.get(prop).setTooltip(new Tooltip(violation.getMessage()));
                }
            });
            return false;
        }
        return true;
    }
}
