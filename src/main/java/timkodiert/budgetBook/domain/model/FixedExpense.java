package timkodiert.budgetBook.domain.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
public class FixedExpense extends Expense {

    @Getter
    @Setter
    @NotEmpty
    private List<Integer> datesOfPayment; // Enthält zunächst nur die Monate, in denen zu zahlen ist
    
    public FixedExpense() {
        super();
        initAdapter();
    }

    public FixedExpense(String position, double value, ExpenseType type, List<Integer> datesOfPayment) {
        super(position, value, type);
        this.datesOfPayment = datesOfPayment;
        initAdapter();
    }

    @Override
    protected void initAdapter() {
        try {
            this.adapter = new FixedExpenseAdapter(this);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
