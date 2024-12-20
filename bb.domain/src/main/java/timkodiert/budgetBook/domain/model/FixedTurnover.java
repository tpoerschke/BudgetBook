package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetBook.domain.adapter.Adaptable;
import timkodiert.budgetBook.domain.adapter.FixedTurnoverAdapter;

@Getter
@Entity
@ToString
public class FixedTurnover extends BaseEntity implements IFixedTurnover, Categorizable, Adaptable<FixedTurnoverAdapter> {

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

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<PaymentInformation> paymentInformations = new ArrayList<>();

    @OneToMany(mappedBy = "fixedExpense", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<AccountTurnover> accountTurnover = new ArrayList<>();

    @Setter
    @OneToOne(mappedBy = "linkedFixedExpense", cascade = {CascadeType.ALL})
    private ImportRule importRule;

    @Transient
    private transient FixedTurnoverAdapter adapter;

    public FixedTurnover() {
        importRule = new ImportRule(this);
        initAdapter();
    }

    @Deprecated(forRemoval = true)
    public FixedTurnover(String position, double value, PaymentType type, List<Integer> datesOfPayment, MonthYear start, MonthYear end) {
        this.paymentInformations.add(new PaymentInformation(this, value, datesOfPayment, type, start, end));
        importRule = new ImportRule(this);
        initAdapter();
    }

    public static FixedTurnover create(String position, TurnoverDirection direction, ImportRule importRule) {
        FixedTurnover turnover = new FixedTurnover();
        turnover.setPosition(position);
        turnover.setDirection(direction);
        turnover.setImportRule(importRule);
        importRule.setLinkedFixedExpense(turnover);
        return turnover;
    }

    @Override
    public void initAdapter() {
        try {
            this.adapter = new FixedTurnoverAdapter(this);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public PaymentType getType() {
        // TODO: Sinnvolle Ausgabe
        if (this.paymentInformations.size() == 0) {
            return PaymentType.MONTHLY;
        }
        return this.paymentInformations.get(0).getType();
    }

    @Override
    public double getValueForYear(int year) {
        return IntStream.rangeClosed(1, 12).mapToDouble(month -> this.getValueFor(year, month)).sum();
    }

    @Override
    public double getValueFor(MonthYear monthYear) {
        return getValueFor(monthYear.getYear(), monthYear.getMonth());
    }

    public @Nullable LocalDate getImportDate(MonthYear monthYear) {
        List<AccountTurnover> accountTurnover = findImports(monthYear);
        if (!accountTurnover.isEmpty()) {
            return accountTurnover.get(0).getDate();
        }
        return null;
    }

    public boolean hasImport(MonthYear monthYear) {
        return getImportDate(monthYear) != null;
    }

    private double getValueFor(int year, int month) {
        // Importe auswerten
        List<AccountTurnover> accountTurnover = findImports(MonthYear.of(month, year));
        if(!accountTurnover.isEmpty()) {
            return accountTurnover.stream().mapToDouble(AccountTurnover::getAmount).sum();
        }
        // Konfigurierten Rhythmus auswerten
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

    private List<AccountTurnover> findImports(MonthYear monthYear) {
        return accountTurnover.stream().filter(at -> monthYear.containsDate(at.getDate())).sorted().toList();
    }

    @Override
    public boolean contentEquals(Object other) {

        if (other instanceof FixedTurnover expense) {
            boolean equals = Objects.equals(this.getPosition(), expense.getPosition())
                    && Objects.equals(this.getNote(), expense.getNote());

            boolean importRuleEquals = this.getImportRule() == null
                    ? expense.getImportRule() == null
                    : this.getImportRule().contentEquals(expense.getImportRule());

            return equals
                    && ContentEquals.listsContentEquals(this.getPaymentInformations(), expense.getPaymentInformations())
                    && ContentEquals.listsContentEquals(this.getCategories(), expense.getCategories())
                    && importRuleEquals;
        }

        return false;
    }
}
