package timkodiert.budgetBook.domain.model;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class PaymentInformation {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    protected int id;

    @Setter
    private MonthYear start;
    @Setter
    private MonthYear end;

    @Setter
    @AttributeOverrides({
        @AttributeOverride(name = "type", column = @Column(name = "type"))
    })
    private PaymentType type;

    @Setter
    private List<Integer> monthsOfPayment;
    @Setter
    private double value;

    @Setter
    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private FixedExpense expense;

    public PaymentInformation(FixedExpense expense, double value, List<Integer> monthsOfPayment, PaymentType type, MonthYear start, MonthYear end) {
        this.expense = expense;
        this.type = type;
        this.monthsOfPayment = monthsOfPayment;
        this.value = value;
        this.start = start;
        this.end = end;
    }

    // TODO: wird nicht mehr benötigt
    private PaymentInformation(int year, PaymentInformation payInfo) {
        this.expense = payInfo.getExpense();
        this.type = payInfo.getType();
    }

    // Zum "kopieren" bzw. übertragen in ein neues Jahr
    // TODO: wird nicht mehr benötigt
    public static PaymentInformation of(int year, PaymentInformation payInfo) {
        return new PaymentInformation(year, payInfo);
    }

    public double getValueFor(MonthYear monthYear) {
        return this.validFor(monthYear) && this.monthsOfPayment.contains(monthYear.getMonth()) ? this.value : 0.0;
    }

    public boolean validFor(MonthYear monthYear) {

        if(List.of(0, -1).contains(this.start.compareTo(monthYear))) {
            return true;
        }
        if(List.of(0, 1).contains(this.end.compareTo(monthYear))) {
            return true;
        }

        return false;
    } 
}