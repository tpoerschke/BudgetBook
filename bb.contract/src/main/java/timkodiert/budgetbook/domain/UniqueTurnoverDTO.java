package timkodiert.budgetbook.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import timkodiert.budgetbook.domain.table.RowType;
import timkodiert.budgetbook.domain.util.HasType;

@Getter
@Setter
public class UniqueTurnoverDTO implements HasType<RowType> {

    private int id;
    private String biller;
    private LocalDate date;
    private String note;
    private String receiptImagePath;
    private List<UniqueTurnoverInformationDTO> paymentInformations = new ArrayList<>();

    public double getTotalValue() {
        return paymentInformations.stream().mapToDouble(UniqueTurnoverInformationDTO::getValueSigned).sum();
    }

    @Override
    public RowType getType() {
        return RowType.UNIQUE_EXPENSE;
    }
}
