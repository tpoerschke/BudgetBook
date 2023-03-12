package timkodiert.budgetBook.domain.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class UniqueExpenseInformation {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;

    @Setter
    private String label;

    @Setter
    private double value;

    @Setter
    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private UniqueExpense expense;
}
