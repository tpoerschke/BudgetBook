package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
public class FixedExpense extends Expense {

    @Getter
    @Setter
    @OneToMany(mappedBy="expense")
    private List<PaymentInformation> payments = new ArrayList<>();
    
    public FixedExpense() {
        super();
        initAdapter();
    }

    public FixedExpense(String position, double value, ExpenseType type, List<Integer> datesOfPayment) {
        super(position, type);

        this.payments.add(new PaymentInformation(value, datesOfPayment, LocalDate.now()));
        initAdapter();
    }

    @Override
    protected void initAdapter() {
        try {
            this.adapter = new FixedExpenseAdapter(this);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public double getValue() {
        // TODO Auto-generated method stub
        return this.payments.isEmpty() ? 0 : this.payments.get(0).getValue();
    }

    @Override
    public double getTotalValue() {
        // Sollte die Summe aller Payments für das aktuelle Jahr ausgeben
        // Dazu müssen ggf. mehrere PaymentInformation ausgewertet werden, falls
        // sich die Zahlungen geändert haben (PaymentInformation.startDate überprüfen)
        //return this.getPayments().values().stream().mapToDouble(v -> v.doubleValue()).sum();
        return 0;
    }
}
