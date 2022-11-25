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
    private List<PaymentInformation> paymentInformations = new ArrayList<>();
    
    public FixedExpense() {
        super();
        initAdapter();
    }

    public FixedExpense(String position, double value, PaymentType type, List<Integer> datesOfPayment) {
        super(position);

        this.paymentInformations.add(new PaymentInformation(this, LocalDate.now().getYear(), value, datesOfPayment, type));
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
    public PaymentType getPaymentType() {
        return this.paymentInformations.get(0).getType();
    }

    @Override
    public double getCurrentMonthValue() {
        LocalDate currentMonth = LocalDate.now();
        return this.getValueFor(currentMonth.getYear(), currentMonth.getMonth().getValue());
    }

    @Override
    public double getNextMonthValue() {
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        return this.getValueFor(nextMonth.getYear(), nextMonth.getMonth().getValue());
    }

    @Override
    public double getCurrentYearValue() {
        return this.paymentInformations.get(0).getPayments().values().stream().mapToDouble(v -> v).sum();
    }

    public double getValueFor(int year, int month) {
        return this.paymentInformations.get(0).getValueFor(month);
    }
}
