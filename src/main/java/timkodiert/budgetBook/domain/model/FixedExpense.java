package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FixedExpense extends Expense implements Adaptable {

    @OneToMany(mappedBy="expense", cascade=CascadeType.ALL)
    private List<PaymentInformation> paymentInformations = new ArrayList<>();

    private MonthYear start;
    private MonthYear end;
    
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
    public void initAdapter() {
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
    public double getValueForYear(int year) {
        PaymentInformation payInfo = this.findPaymentInformation(year);
        if(payInfo != null) {
            return payInfo.getPayments().values().stream().mapToDouble(v -> v).sum();
        }
        return 0;
    }

    public double getValueFor(int year, int month) {
        // Untere Grenze erreicht?
        if(this.getStart() != null && this.getStart().compareTo(MonthYear.of(month, year)) == 1) {
            return 0;
        } 
        // Obere Grenze erreicht?
        if(this.getEnd() != null && this.getEnd().compareTo(MonthYear.of(month, year)) == -1) {
            return 0;
        } 

        PaymentInformation payInfo = this.findPaymentInformation(year);
        if(payInfo != null) {
            return payInfo.getValueFor(month);
        }
        return 0;
    }

    public void addPaymentInformationForNextYear() {
        int currentYear = LocalDate.now().getYear();
        int nextYear = LocalDate.now().plusYears(1).getYear();
        PaymentInformation payInfo = this.findPaymentInformation(currentYear);
        if(payInfo != null) {
            PaymentInformation payInfoNextYear = PaymentInformation.of(nextYear, payInfo);
            this.paymentInformations.add(payInfoNextYear);
        }
    }

    private PaymentInformation findPaymentInformation(int year) {
        for(PaymentInformation payInfo : this.paymentInformations) {
            if(payInfo.getYear() == year) {
                return payInfo;
            }
        }
        return null;
    }
}
