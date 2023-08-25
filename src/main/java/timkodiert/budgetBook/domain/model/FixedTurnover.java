package timkodiert.budgetBook.domain.model;

public interface FixedTurnover {

    String getPosition();

    PaymentType getType();

    double getValueFor(MonthYear monthYear);

    double getValueForYear(int year);
}
