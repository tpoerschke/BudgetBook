package timkodiert.budgetbook.domain.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import org.jetbrains.annotations.Nullable;

public class TestDataProvider {

    private TestDataProvider() {}

    public static UniqueTurnover createUniqueTurnover(LocalDate date, String receiver, double amount) {
        UniqueTurnover turnover = new UniqueTurnover();
        turnover.setDate(date);
        turnover.setBiller(receiver);
        UniqueTurnoverInformation info = UniqueTurnoverInformation.total(turnover, amount);
        turnover.getPaymentInformations().add(info);
        return turnover;
    }

    public static UniqueTurnover createUniqueTurnoverWithAccountTurnover(LocalDate date, String receiver, double amount) {
        UniqueTurnover turnover = createUniqueTurnover(date, receiver, amount);
        AccountTurnover at = new AccountTurnover(date, receiver, "Test", "Test", amount);
        at.setUniqueExpense(turnover);
        turnover.setAccountTurnover(at);
        return turnover;
    }

    public static FixedTurnover createFixedTurnover(double value, MonthYear start, @Nullable MonthYear end) {
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
