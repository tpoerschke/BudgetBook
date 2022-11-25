package timkodiert.budgetBook.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
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

    private int year;

    private PaymentType type;

    @ElementCollection
    @CollectionTable(name = "paymentinfo_month_mapping", joinColumns = {
            @JoinColumn(name = "paymentinfo_id", referencedColumnName = "id") })
    @MapKeyColumn(name = "month")
    @Column(name = "value")
    private Map<Integer, Double> payments = new HashMap<>();

    @Setter
    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private Expense expense;

    public PaymentInformation(Expense expense, int year, double value, List<Integer> monthsOfPayment, PaymentType type) {
        this.expense = expense;
        this.type = type;
        this.year = year;
        for(int month : monthsOfPayment) {
            this.payments.put(month, value);
        }
    }

    public double getValueFor(int month) {
        return this.payments.getOrDefault(month, 0.0);
    }

    public List<Integer> getMonthsOfPayment() {
        return this.payments.keySet().stream().toList();
    }
}