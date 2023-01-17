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
    
    public FixedExpense() {
        super();
        initAdapter();
    }

    public FixedExpense(String position, double value, PaymentType type, List<Integer> datesOfPayment, MonthYear start, MonthYear end) {
        super(position);

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
        // PaymentInformation payInfo = this.findPaymentInformation(year);
        // if(payInfo != null) {
        //     return payInfo.getPayments().values().stream().mapToDouble(v -> v).sum();
        // }
        return 0;
    }

    public double getValueFor(int year, int month) {
        PaymentInformation payInfo = this.findPaymentInformation(MonthYear.of(month, year));
        if(payInfo != null) {
            return payInfo.getValueFor(month);
        }
        return 0;
    }

    // TODO: wird nicht mehr benötigt
    // public void addPaymentInformationForNextYear() {
    //     int currentYear = LocalDate.now().getYear();
    //     int nextYear = LocalDate.now().plusYears(1).getYear();
    //     PaymentInformation payInfo = this.findPaymentInformation(currentYear);
    //     if(payInfo != null) {
    //         PaymentInformation payInfoNextYear = PaymentInformation.of(nextYear, payInfo);
    //         this.paymentInformations.add(payInfoNextYear);
    //     }
    // }

    private PaymentInformation findPaymentInformation(MonthYear monthYear) {
        for(PaymentInformation payInfo : this.paymentInformations) {
            if(payInfo.validFor(monthYear)) {
                return payInfo;
            }
        }
        return null;
    }
}
