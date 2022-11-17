package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class PaymentInformation {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    protected int id;
    
    @Setter
    @NotEmpty
    private List<Integer> monthsOfPayment;

    @Setter
    @PositiveOrZero(message = "Die Höhe der Ausgabe darf nicht negativ sein.")
    protected double value;

    @Setter
    @NotNull
    private LocalDate startDate;

    @Setter
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name="expense_id", nullable=false)
    private Expense expense;

    public PaymentInformation(double value, List<Integer> monthsOfPayment, LocalDate startDate) {
        this.value = value;
        this.monthsOfPayment = monthsOfPayment;
        this.startDate = startDate;
    }
}