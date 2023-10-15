package timkodiert.budgetBook.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Entity
@ToString
public class FixedExpense extends BaseEntity implements FixedTurnover, Categorizable, Adaptable<FixedExpenseAdapter> {

    @Setter
    @NotBlank(message = "Die Ausgabe muss benannt werden.")
    private String position;

    @Setter
    private String note;

    @ManyToMany(cascade = { CascadeType.PERSIST })
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<PaymentInformation> paymentInformations = new ArrayList<>();

    @OneToMany(mappedBy = "fixedExpense", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<AccountTurnover> accountTurnover = new ArrayList<>();

    @Transient
    private transient FixedExpenseAdapter adapter;

    public FixedExpense() {
        super();
        initAdapter();
    }

    public FixedExpense(String position, double value, PaymentType type, List<Integer> datesOfPayment, MonthYear start, MonthYear end) {
        this.paymentInformations.add(new PaymentInformation(this, value, datesOfPayment, type, start, end));
        initAdapter();
    }

    @Override
    public void initAdapter() {
        try {
            this.adapter = new FixedExpenseAdapter(this);
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

    private double getValueFor(int year, int month) {
        PaymentInformation payInfo = this.findPaymentInformation(MonthYear.of(month, year));
        if (payInfo != null) {
            return payInfo.getValueFor(MonthYear.of(month, year));
        }
        return 0;
    }

    @Override
    public double getValueFor(MonthYear monthYear) {
        return getValueFor(monthYear.getYear(), monthYear.getMonth());
    }

    private PaymentInformation findPaymentInformation(MonthYear monthYear) {
        for (PaymentInformation payInfo : this.paymentInformations) {
            if (payInfo.validFor(monthYear)) {
                return payInfo;
            }
        }
        return null;
    }

    @Override
    public boolean contentEquals(Object other) {

        if (other instanceof FixedExpense expense) {
            boolean equals = Objects.equals(this.getPosition(), expense.getPosition())
                    && Objects.equals(this.getNote(), expense.getNote());

            return equals
                    && ContentEquals.listsContentEquals(this.getPaymentInformations(), expense.getPaymentInformations())
                    && ContentEquals.listsContentEquals(this.getCategories(), expense.getCategories());
        }

        return false;
    }
}
