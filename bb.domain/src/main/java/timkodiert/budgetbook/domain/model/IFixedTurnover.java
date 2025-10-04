package timkodiert.budgetbook.domain.model;

import timkodiert.budgetbook.domain.util.HasType;

public interface IFixedTurnover extends HasType<PaymentType> {

    String getPosition();

    int getValueFor(MonthYear monthYear);

    int getValueForYear(int year);
}
