package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FixedExpense extends Expense implements Adaptable {
    
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
        // TODO: Sinnvolle Ausgabe
        if(this.paymentInformations.size() == 0) {
            return PaymentType.MONTHLY;
        }
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
        return IntStream.rangeClosed(1, 12).mapToDouble(month -> this.getValueFor(year, month)).sum();
    }

    public double getValueFor(int year, int month) {
        PaymentInformation payInfo = this.findPaymentInformation(MonthYear.of(month, year));
        if(payInfo != null) {
            return payInfo.getValueFor(MonthYear.of(month, year));
        }
        return 0;
    }

    private PaymentInformation findPaymentInformation(MonthYear monthYear) {
        for(PaymentInformation payInfo : this.paymentInformations) {
            if(payInfo.validFor(monthYear)) {
                return payInfo;
            }
        }
        return null;
    }
}
