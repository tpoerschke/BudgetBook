package timkodiert.budgetBook.domain.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentType {
    MONTHLY, ANNUAL, SEMIANNUAL, QUARTERLY, CUMULATIVE
}
