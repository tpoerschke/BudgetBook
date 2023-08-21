package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import static timkodiert.budgetBook.domain.model.ContentEquals.listsContentEquals;

@Getter
@Entity
public class UniqueExpense extends BaseEntity implements Adaptable<UniqueExpenseAdapter> {

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
    private List<UniqueExpenseInformation> paymentInformations = new ArrayList<>();

    @Transient
    private transient UniqueExpenseAdapter adapter;

    public UniqueExpense() {
        initAdapter();
    }

    @Override
    public void initAdapter() {
        try {
            this.adapter = new UniqueExpenseAdapter(this);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public double getTotalValue() {
        return paymentInformations.stream().mapToDouble(UniqueExpenseInformation::getValue).sum();
    }

    @Override
    public boolean contentEquals(Object other) {

        if (other instanceof UniqueExpense uniqueExpense) {
            boolean equals = Objects.equals(this.getBiller(), uniqueExpense.getBiller())
                    && Objects.equals(this.getNote(), uniqueExpense.getNote())
                    && Objects.equals(this.getDate(), uniqueExpense.getDate())
                    && Objects.equals(this.getReceiptImagePath(), uniqueExpense.getReceiptImagePath());

            return equals && listsContentEquals(this.getPaymentInformations(), uniqueExpense.getPaymentInformations());
        }

        return false;
    }
}
