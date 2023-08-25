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
    private String type;

    public static PaymentType fromString(String type) {
        switch (type) {
            case "monatlich":
                return MONTHLY;
            case "jährlich":
                return ANNUAL;
            case "halbjährlich":
                return SEMIANNUAL;
            case "vierteljährlich":
                return QUARTERLY;
            default:
                return null;
        }
    }
}
