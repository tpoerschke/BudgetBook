package timkodiert.budgetBook.domain.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

// Das Model
@Getter
@Entity
public abstract class Expense {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    protected int id;

    @Setter
    protected String position;
    @Setter
    protected double value;
    @Setter
    protected String note;

    @Transient
    protected ExpenseAdapter adapter;

    public Expense(String position, double value) {
        this.position = position;
        this.value = value;
    }
}
