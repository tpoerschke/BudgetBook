package timkodiert.budgetbook.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import timkodiert.budgetbook.domain.model.PaymentType;
import timkodiert.budgetbook.domain.model.TurnoverDirection;

@Getter
@Setter
public class FixedTurnoverDTO {

    private int id;
    private String position;
    private String note;
    private List<Reference<CategoryDTO>> categories = new ArrayList<>();
    private PaymentType paymentType;
    private TurnoverDirection direction;
    private boolean usePaymentInfoForFutureOnly;
    private List<PaymentInformationDTO> paymentInformations = new ArrayList<>();
    private List<AccountTurnoverDTO> accountTurnover = new ArrayList<>();

    public void setPaymentInformations(List<PaymentInformationDTO> paymentInformations) {
        this.paymentInformations.clear();
        this.paymentInformations.addAll(paymentInformations);
    }

}
