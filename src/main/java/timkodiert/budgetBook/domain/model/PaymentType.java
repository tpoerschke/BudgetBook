package timkodiert.budgetBook.domain.model;

import java.util.Arrays;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import timkodiert.budgetBook.i18n.LanguageManager;

@RequiredArgsConstructor
public enum PaymentType {

    MONTHLY(LanguageManager.get("time.monthly")),
    ANNUAL(LanguageManager.get("time.yearly")),
    SEMIANNUAL(LanguageManager.get("time.half-yearly")),
    QUARTERLY(LanguageManager.get("time.quarterly")),
    CUMULATIVE(LanguageManager.get("domain.term.total"));

    @Getter
    @NonNull
    private String type;

    public static PaymentType fromString(String type) {
        System.out.println(type);
        return Arrays.stream(PaymentType.values()).filter(
                e -> e.type.equals(type)
        ).findFirst().orElse(null);
    }
}
