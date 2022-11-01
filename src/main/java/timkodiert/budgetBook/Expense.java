package timkodiert.budgetBook;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

// Das Model
@Entity
public class Expense {

    @Getter
    @Id
    private final int id;

    @Getter
    @Setter
    private String position;
    @Getter
    @Setter
    private double value;
    @Getter
    @Setter
    private String type;

    @Getter
    @Transient
    private ExpenseAdapter adapter;

    public Expense(int id, String position, double value, String type) {
        this.id = id;
        this.position = position;
        this.value = value;
        this.type = type;

        try {
            this.adapter = new ExpenseAdapter(this);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
