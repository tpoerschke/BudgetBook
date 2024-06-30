package timkodiert.budgetBook.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class CumulativeExpense implements IFixedTurnover {

    private final Map<MonthYear, Double> valueMap = new HashMap<>();

    public CumulativeExpense(List<? extends IFixedTurnover> turnovers, int startYear, int endYear) {
        IntStream.rangeClosed(startYear, endYear).boxed().forEach(year -> {
            IntStream.rangeClosed(1, 12).forEach(month -> {
                valueMap.put(MonthYear.of(month, year), 0.0);
            });
        });

        turnovers.forEach(t -> {
            valueMap.keySet().forEach(monthYear -> {
                valueMap.put(monthYear, valueMap.get(monthYear) + t.getValueFor(monthYear));
            });
        });

    }

    @Override
    public String getPosition() {
        // An der Stelle an der diese Methode aufgerufen wird, sollte im Falle
        // einer kumulativen Ausgabe der LanguageManager verwendet werden:
        // LanguageManager.get("domain.term.total");
        return "FIXME Total (Cumulative Expense)";
    }

    @Override
    public PaymentType getType() {
        return PaymentType.CUMULATIVE;
    }

    @Override
    public double getValueFor(MonthYear monthYear) {
        return valueMap.get(monthYear);
    }

    @Override
    public double getValueForYear(int year) {
        return IntStream.rangeClosed(1, 12).mapToDouble(month -> valueMap.get(MonthYear.of(month, year))).sum();
    }

}
