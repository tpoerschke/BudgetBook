package timkodiert.budgetbook.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.PaymentType;

@Getter
@Setter
public class PaymentInformationDTO implements Serializable {

    private int id;
    @NotNull(message = "{attribute.notNull}")
    private MonthYear start;
    private MonthYear end;
    @NotNull(message = "{attribute.notNull}")
    private PaymentType type;
    private int value;
    private List<Integer> monthsOfPayment = new ArrayList<>();

    public PaymentInformationDTO() {
        id = new Random().nextInt(Integer.MIN_VALUE, 0);
    }
}
