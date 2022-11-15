package timkodiert.budgetBook.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.MapKeyColumn;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
public class FixedExpense extends Expense {

    @Getter
    @Setter
    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "payments", joinColumns = {@JoinColumn(name = "expense_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "month")
    @Column(name = "value")
    private Map<Integer, Double> payments = new HashMap<>();
    
    public FixedExpense() {
        super();
        initAdapter();
    }

    public FixedExpense(String position, double value, ExpenseType type, List<Integer> datesOfPayment) {
        super(position, value, type);

        for(int month : datesOfPayment) {
            payments.put(month, value);
        }
        
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

    public double getTotalValue() {
        return this.getPayments().values().stream().mapToDouble(v -> v.doubleValue()).sum();
    }
}
