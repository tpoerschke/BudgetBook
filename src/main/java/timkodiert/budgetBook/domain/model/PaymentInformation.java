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

    public double getValueFor(MonthYear monthYear) {
        return this.validFor(monthYear) && this.monthsOfPayment.contains(monthYear.getMonth()) ? this.value : 0.0;
    }

    public boolean validFor(MonthYear monthYear) {

        if(this.start != null && this.start.compareTo(monthYear) == 1) {
            return false;
        }
        if(this.end != null && this.end.compareTo(monthYear) == 1) {
            return false;
        }

        return true;
    } 
}