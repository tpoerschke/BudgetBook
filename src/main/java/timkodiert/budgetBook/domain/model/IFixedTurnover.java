package timkodiert.budgetBook.domain.model;

import timkodiert.budgetBook.util.HasType;

public interface IFixedTurnover extends HasType<PaymentType> {

    String getPosition();

    double getValueFor(MonthYear monthYear);

    double getValueForYear(int year);
}
