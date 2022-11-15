package timkodiert.budgetBook.domain.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Das Model
@Getter
@Entity
@NoArgsConstructor
public abstract class Expense {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    protected int id;

    @Setter
    @NotBlank(message = "Die Ausgabe muss benannt werden.")
    protected String position;
    @Setter
    @PositiveOrZero(message = "Die Höhe der Ausgabe darf nicht negativ sein.")
    protected double value;
    @Setter
    protected String note;
    @Setter
    @NotNull
    protected ExpenseType type;

    @Transient
    protected ExpenseAdapter adapter;

    public Expense(String position, double value, ExpenseType type) {
        this.position = position;
        this.value = value;
        this.type = type;
    }

    protected abstract void initAdapter();

    public double getTotalValue() {
        return getValue();
    }
}
