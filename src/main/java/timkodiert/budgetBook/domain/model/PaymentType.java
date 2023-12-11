package timkodiert.budgetBook.domain.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentType {

    MONTHLY("monatlich"),
    ANNUAL("jährlich"),
    SEMIANNUAL("halbjährlich"),
    QUARTERLY("vierteljährlich"),
    CUMULATIVE("Gesamt");

    @Getter
    @NonNull
    private final String type;

    public static PaymentType fromString(String type) {
        return switch (type) {
            case "monatlich" -> MONTHLY;
            case "jährlich" -> ANNUAL;
            case "halbjährlich" -> SEMIANNUAL;
            case "vierteljährlich" -> QUARTERLY;
            default -> null;
        };
    }
}
