package timkodiert.budgetbook.domain.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@Getter
@Entity
@ToString
public class FixedTurnover extends BaseEntity implements IFixedTurnover, Categorizable {

    @Setter
    @NotBlank(message = "Die Ausgabe muss benannt werden.")
    private String position;

    @Setter
    private String note;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TurnoverDirection direction = TurnoverDirection.OUT;

    @ManyToMany(cascade = { CascadeType.PERSIST })
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentInformation> paymentInformations = new ArrayList<>();

    @OneToMany(mappedBy = "fixedTurnover", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<UniqueTurnover> uniqueTurnovers = new ArrayList<>();

    @OneToMany(mappedBy = "linkedFixedExpense", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ImportRule> importRules = new ArrayList<>();

    // Future bedeutet hier: der aktuelle Monat und alle zukünftigen Monate
    @Setter
    private boolean usePaymentInfoForFutureOnly;

    public static FixedTurnover create(String position, TurnoverDirection direction, ImportRule importRule) {
        FixedTurnover turnover = new FixedTurnover();
        turnover.setPosition(position);
        turnover.setDirection(direction);
        turnover.getImportRules().add(importRule);
        importRule.setLinkedFixedExpense(turnover);
        return turnover;
    }

    @Override
    public PaymentType getType() {
        // TODO: Sinnvolle Ausgabe
        if (this.paymentInformations.isEmpty()) {
            return PaymentType.MONTHLY;
        }
        return this.paymentInformations.get(0).getType();
    }

    @Override
    public int getValueForYear(int year) {
        return IntStream.rangeClosed(1, 12).map(month -> this.getValueFor(year, month)).sum();
    }

    @Override
    public int getValueFor(MonthYear monthYear) {
        return getValueFor(monthYear.getYear(), monthYear.getMonth());
    }

    public @Nullable LocalDate getImportDate(MonthYear monthYear) {
        return getImports().stream()
                           .filter(at -> monthYear.containsDate(at.getDate()))
                           .findFirst()
                           .map(AccountTurnover::getDate)
                           .orElse(null);
    }

    public boolean hasImport(MonthYear monthYear) {
        return getImportDate(monthYear) != null;
    }

    private int getValueFor(int year, int month) {
        // Importe auswerten
        List<UniqueTurnover> uniqueTurnoverForMonth = findUniqueTurnover(MonthYear.of(month, year));
        if (!uniqueTurnoverForMonth.isEmpty()) {
            return uniqueTurnoverForMonth.stream().mapToInt(UniqueTurnover::getTotalValue).sum();
        }
        // Konfigurierten Rhythmus auswerten
        if (YearMonth.of(year, month).isBefore(YearMonth.now()) && isUsePaymentInfoForFutureOnly()) {
            return 0;
        }
        PaymentInformation payInfo = findPaymentInformation(MonthYear.of(month, year));
        if (payInfo != null) {
            return direction.getSign() * payInfo.getValueFor(MonthYear.of(month, year));
        }
        return 0;
    }

    private PaymentInformation findPaymentInformation(MonthYear monthYear) {
        for (PaymentInformation payInfo : this.paymentInformations) {
            if (payInfo.validFor(monthYear)) {
                return payInfo;
            }
        }
        return null;
    }

    public List<AccountTurnover> getImports() {
        return uniqueTurnovers.stream()
                              .map(UniqueTurnover::getAccountTurnover)
                              .filter(Objects::nonNull)
                              .toList();
    }

    private List<UniqueTurnover> findUniqueTurnover(MonthYear monthYear) {
        return uniqueTurnovers.stream().filter(uq -> monthYear.containsDate(uq.getDate())).toList();
    }
}
