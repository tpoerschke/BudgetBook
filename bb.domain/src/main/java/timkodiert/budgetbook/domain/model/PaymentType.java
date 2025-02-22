package timkodiert.budgetbook.domain.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentType {
    MONTHLY, ANNUAL, SEMIANNUAL, QUARTERLY, CUMULATIVE
}
