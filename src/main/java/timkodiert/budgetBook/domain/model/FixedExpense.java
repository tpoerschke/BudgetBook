package timkodiert.budgetBook.domain.model;

import java.util.List;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
public class FixedExpense extends Expense {

    @Getter
    @Setter
    private List<Integer> datesOfPayment; // Enthält zunächst nur die Monate, in denen zu zahlen ist
    
    public FixedExpense(String position, double value, ExpenseType type, List<Integer> datesOfPayment) {
        super(position, value, type);
        this.datesOfPayment = datesOfPayment;

        try {
            this.adapter = new FixedExpenseAdapter(this);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
