package timkodiert.budgetbook.domain.model;

import timkodiert.budgetbook.util.HasType;

public interface IFixedTurnover extends HasType<PaymentType> {

    String getPosition();

    double getValueFor(MonthYear monthYear);

    double getValueForYear(int year);
}
