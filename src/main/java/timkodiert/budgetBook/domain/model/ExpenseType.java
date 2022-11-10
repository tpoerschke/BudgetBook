package timkodiert.budgetBook.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Embeddable
@RequiredArgsConstructor
@NoArgsConstructor
public final class ExpenseType {
    
    public static final ExpenseType MONTHLY = new ExpenseType("monatlich");
    public static final ExpenseType ANNUAL = new ExpenseType("jährlich");
    public static final ExpenseType SEMIANNUAL = new ExpenseType("halbjährlich");
    public static final ExpenseType QUARTERLY = new ExpenseType("vierteljährlich");

    @Getter
    @NonNull
    private String type;

    public static ExpenseType fromString(String type) {
        switch(type) {
            case "monatlich": return MONTHLY;
            case "jährlich": return ANNUAL;
            case "halbjährlich": return SEMIANNUAL;
            case "vierteljährlich": return QUARTERLY;
            default: return null;
        }
    }
}
