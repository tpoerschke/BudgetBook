package timkodiert.budgetBook.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class CumulativeExpense extends Expense {

    private List<PaymentInformation> paymentInformations = new ArrayList<>();
    
    public CumulativeExpense(List<? extends Expense> expenses, int startYear, int endYear) {
        this.position = "Gesamt";

        // List<Integer> months = IntStream.rangeClosed(1, 12).boxed().toList();
        // IntStream.rangeClosed(startYear, endYear).forEach(year -> {
        //     PaymentInformation payInfo = new PaymentInformation(this, year, 0, months, PaymentType.CUMULATIVE);

        //     IntStream.rangeClosed(1, 12).forEach(i -> {
        //         for(Expense expense : expenses) {
        //             double added = payInfo.getPayments().get(i) + expense.getValueFor(year, i);
        //             payInfo.getPayments().put(i, added);
        //         }
        //     });

        //     this.paymentInformations.add(payInfo);
        // });
    }

    @Override
    public double getValueFor(int year, int month) {
        PaymentInformation payInfo = this.findPaymentInformation(year);
        if(payInfo != null) {
            return payInfo.getValueFor(MonthYear.of(month, year));
        }
        return 0;
    }

    @Override
    public double getValueForYear(int year) {
        // PaymentInformation payInfo = this.findPaymentInformation(year);
        // if(payInfo != null) {
        //     return payInfo.getPayments().values().stream().mapToDouble(v -> v).sum();
        // }
        return 0;
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

    private PaymentInformation findPaymentInformation(int year) {
        // for(PaymentInformation payInfo : this.paymentInformations) {
        //     if(payInfo.getYear() == year) {
        //         return payInfo;
        //     }
        // }
        return null;
    }
}
