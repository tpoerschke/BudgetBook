package timkodiert.budgetBook.domain.model;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

// TODO: ENUM DRAUS MACHEN

@Embeddable
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public final class PaymentType {

    public static final PaymentType MONTHLY = new PaymentType("monatlich");
    public static final PaymentType ANNUAL = new PaymentType("jährlich");
    public static final PaymentType SEMIANNUAL = new PaymentType("halbjährlich");
    public static final PaymentType QUARTERLY = new PaymentType("vierteljährlich");

    // Für die Jahresübersicht (letzte Zeile)
    public static final PaymentType CUMULATIVE = new PaymentType("kummulativ");

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
