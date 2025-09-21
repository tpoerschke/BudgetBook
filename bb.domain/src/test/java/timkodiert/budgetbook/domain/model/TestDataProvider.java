package timkodiert.budgetbook.domain.model;

import java.time.Month;
import java.util.Arrays;

import org.jetbrains.annotations.Nullable;

public class TestDataProvider {

    private TestDataProvider() {}

    public static FixedTurnover createTurnover(double value, MonthYear start, @Nullable MonthYear end) {
        PaymentInformation info = new PaymentInformation();
        info.setType(PaymentType.MONTHLY);
        info.setMonthsOfPayment(Arrays.stream(Month.values()).map(Month::getValue).toList());
        info.setValue(value);
        info.setStart(start);
        info.setEnd(end);
        FixedTurnover turnover = new FixedTurnover();
        turnover.setPosition("Test-Ausgabe");
        turnover.setDirection(TurnoverDirection.OUT);
        turnover.getPaymentInformations().add(info);
        return turnover;
    }
}
