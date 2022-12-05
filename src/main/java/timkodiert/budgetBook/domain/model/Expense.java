package timkodiert.budgetBook.domain.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Das Model
@Getter
@Entity
@NoArgsConstructor
public abstract class Expense implements Turnover {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    protected int id;

    @Setter
    @NotBlank(message = "Die Ausgabe muss benannt werden.")
    protected String position;

    @Setter
    protected String note;

    @Transient
    protected ExpenseAdapter adapter;

    public Expense(String position) {
        this.position = position;
    }

    public abstract double getCurrentMonthValue();
    public abstract double getNextMonthValue();
    public abstract double getValueForYear(int year);

    public abstract PaymentType getPaymentType();
}
