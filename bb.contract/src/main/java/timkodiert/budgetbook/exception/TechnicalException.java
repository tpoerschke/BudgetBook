package timkodiert.budgetbook.exception;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class TechnicalException extends RuntimeException {

    public enum Reason {
        PROGRAMMING_ERROR
    }

    private final Reason reason;

    private TechnicalException(Reason reason, String message, @Nullable Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }

    public static TechnicalException forProgrammingError(String message, Throwable cause) {
        return new TechnicalException(Reason.PROGRAMMING_ERROR, message, cause);
    }
}
