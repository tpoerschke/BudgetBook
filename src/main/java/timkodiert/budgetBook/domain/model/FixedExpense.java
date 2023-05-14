package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Entity
@EqualsAndHashCode
@ToString
public class FixedExpense implements Turnover, Categorizable, Adaptable<FixedExpenseAdapter> {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;

    @Setter
    @NotBlank(message = "Die Ausgabe muss benannt werden.")
    private String position;

    @Setter
    private String note;

    @ManyToMany(cascade = { CascadeType.PERSIST })
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<PaymentInformation> paymentInformations = new ArrayList<>();

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

    public PaymentType getPaymentType() {
        // TODO: Sinnvolle Ausgabe
        if (this.paymentInformations.size() == 0) {
            return PaymentType.MONTHLY;
        }
        return this.paymentInformations.get(0).getType();
    }

    public double getCurrentMonthValue() {
        LocalDate currentMonth = LocalDate.now();
        return this.getValueFor(currentMonth.getYear(), currentMonth.getMonth().getValue());
    }

    public double getNextMonthValue() {
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        return this.getValueFor(nextMonth.getYear(), nextMonth.getMonth().getValue());
    }

    public double getValueForYear(int year) {
        return IntStream.rangeClosed(1, 12).mapToDouble(month -> this.getValueFor(year, month)).sum();
    }

    public double getValueFor(int year, int month) {
        PaymentInformation payInfo = this.findPaymentInformation(MonthYear.of(month, year));
        if (payInfo != null) {
            return payInfo.getValueFor(MonthYear.of(month, year));
        }
        return 0;
    }

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
}
