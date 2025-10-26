package timkodiert.budgetbook.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.PaymentType;
import timkodiert.budgetbook.domain.model.TurnoverDirection;

@Getter
@Setter
public class FixedTurnoverDTO {

    private int id;
    @NotBlank(message = "{fixedTurnover.position.notBlank}")
    private String position;
    private String note;
    private List<Reference<CategoryDTO>> categories = new ArrayList<>();
    private PaymentType paymentType;
    @NotNull(message = "{attribute.notNull}")
    private TurnoverDirection direction;
    private boolean usePaymentInfoForFutureOnly;
    private List<PaymentInformationDTO> paymentInformations = new ArrayList<>();
    private List<ImportRuleDTO> importRules = new ArrayList<>();
    private List<AccountTurnoverDTO> accountTurnover = new ArrayList<>();

    public void setPaymentInformations(List<PaymentInformationDTO> paymentInformations) {
        this.paymentInformations.clear();
        this.paymentInformations.addAll(paymentInformations);
    }


    public boolean hasOverlappingPaymentInformations() {
        return paymentInformations.stream().noneMatch(info -> {
            List<MonthYear> monthYears = info.getEnd() != null ? MonthYear.range(info.getEnd(), info.getStart()) : List.of(info.getStart());
            return anyPaymentInformationInMonthYearList(monthYears, info);
        });
    }

    private boolean anyPaymentInformationInMonthYearList(List<MonthYear> monthYears, PaymentInformationDTO ignore) {
        return paymentInformations.stream()
                                  .filter(info -> info != ignore)
                                  .anyMatch(info -> monthYears.contains(info.getStart()) || info.getEnd() != null && monthYears.contains(info.getEnd()));
    }
}
