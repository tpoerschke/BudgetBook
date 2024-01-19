package timkodiert.budgetBook.domain.model;

public interface IFixedTurnover {

    String getPosition();

    PaymentType getType();

    double getValueFor(MonthYear monthYear);

    double getValueForYear(int year);
}
