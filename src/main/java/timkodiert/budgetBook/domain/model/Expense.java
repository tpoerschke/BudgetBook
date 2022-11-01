package timkodiert.budgetBook.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

// Das Model
@Entity
public abstract class Expense {

    @Getter
    @Id
    protected final int id;

    @Getter
    @Setter
    protected String position;
    @Getter
    @Setter
    protected double value;

    @Getter
    @Transient
    protected ExpenseAdapter adapter;

    public Expense(int id, String position, double value) {
        this.id = id;
        this.position = position;
        this.value = value;
    }
}
