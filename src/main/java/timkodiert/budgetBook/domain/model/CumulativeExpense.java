package timkodiert.budgetBook.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CumulativeExpense extends Expense {

    private List<PaymentInformation> paymentInformations = new ArrayList<>();
    
    public CumulativeExpense(List<? extends Expense> expenses, int startYear, int endYear) {
        this.position = "Gesamt";

        IntStream.rangeClosed(startYear, endYear).forEach(year -> {
            IntStream.rangeClosed(1, 12).forEach(month -> {
                MonthYear monthYear = MonthYear.of(month, year);
                PaymentInformation payInfo = new PaymentInformation(
                    this, 
                    0, 
                    List.of(month), 
                    PaymentType.CUMULATIVE, 
                    monthYear,
                    monthYear
                );

                for(Expense expense : expenses) {
                    double added = payInfo.getValue() + expense.getValueFor(year, month);
                    payInfo.setValue(added);
                }

                this.paymentInformations.add(payInfo);
            });
        });
    }

    @Override
    public double getValueFor(int year, int month) {
        PaymentInformation payInfo = this.findPaymentInformation(MonthYear.of(month, year));
        if(payInfo != null) {
            return payInfo.getValueFor(MonthYear.of(month, year));
        }
        return 0;
    }

    @Override
    public double getValueForYear(int year) {
        return IntStream.rangeClosed(1, 12).mapToDouble(month -> this.getValueFor(year, month)).sum();
    }

    @Override
    public PaymentType getPaymentType() {
       return PaymentType.CUMULATIVE;
    }

    @Override
    public double getCurrentMonthValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getNextMonthValue() {
        // TODO Auto-generated method stub
        return 0;
    }

    // TODO: Refactor, diese Methode gibt es auch in der FixedExpense
    // ggf. teil der Oberklasse machen
    private PaymentInformation findPaymentInformation(MonthYear monthYear) {
        for(PaymentInformation payInfo : this.paymentInformations) {
            if(payInfo.validFor(monthYear)) {
                return payInfo;
            }
        }
        return null;
    }
}
