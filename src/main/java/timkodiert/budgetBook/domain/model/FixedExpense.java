package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
public class FixedExpense extends Expense {

    @Getter
    @Setter
    @OneToMany(mappedBy="expense", cascade=CascadeType.ALL)
    private List<PaymentInformation> payments = new ArrayList<>();
    
    public FixedExpense() {
        super();
        initAdapter();
    }

    public FixedExpense(String position, double value, ExpenseType type, List<Integer> datesOfPayment) {
        super(position, type);

        this.payments.add(new PaymentInformation(this, value, datesOfPayment, LocalDate.now()));
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
    public double getCurrentMonthValue() {
        // TODO Auto-generated method stub
        return this.payments.isEmpty() ? 0 : this.payments.get(0).getValue();
    }

    @Override
    public double getNextMonthValue() {
        // TODO Auto-generated method stub
        return this.payments.isEmpty() ? 0 : this.payments.get(0).getValue();
    }

    @Override
    public double getCurrentYearValue() {
        // Sollte die Summe aller Payments für das aktuelle Jahr ausgeben
        // Dazu müssen ggf. mehrere PaymentInformation ausgewertet werden, falls
        // sich die Zahlungen geändert haben (PaymentInformation.startDate überprüfen)
        //return this.getPayments().values().stream().mapToDouble(v -> v.doubleValue()).sum();
        return this.getCurrentMonthValue() * this.payments.get(0).getFactor();
    }

    public double getValueFor(int year, int month) {
        return this.payments.get(0).getValue();
    }
}
