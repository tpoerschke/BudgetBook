package timkodiert.budgetbook.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class UniqueTurnover extends BaseEntity {

    @Setter
    @NotBlank(message = "Es muss ein Rechnungsteller angegeben werden.")
    private String biller;

    @Setter
    @NotNull(message = "Es muss ein Datum angegeben werden.")
    private LocalDate date;

    @Setter
    private String note;

    @Setter
    private String receiptImagePath;

    @Setter
    @Size(min = 1)
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<UniqueTurnoverInformation> paymentInformations = new ArrayList<>();

    @OneToOne(mappedBy = "uniqueExpense", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private AccountTurnover accountTurnover;

    public double getTotalValue() {
        return paymentInformations.stream().mapToDouble(UniqueTurnoverInformation::getValueSigned).sum();
    }

    public boolean hasImport() {
        return accountTurnover != null;
    }
}
