package timkodiert.budgetBook.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static timkodiert.budgetBook.domain.model.ContentEquals.listsContentEquals;

@Getter
@Entity
@NoArgsConstructor
public class UniqueExpenseInformation implements Categorizable, ContentEquals {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;

    @Setter
    @NotBlank(message = "Die Position muss benannt werden.")
    private String label;

    @Setter
    private double value;

    @Setter
    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private UniqueExpense expense;

    @Setter
    @ManyToMany(cascade = { CascadeType.PERSIST })
    private List<Category> categories = new ArrayList<>();

    @Override
    public boolean contentEquals(Object other) {

        if (other instanceof UniqueExpenseInformation info) {
            boolean equals = Objects.equals(this.getLabel(), info.getLabel())
                    && this.getValue() == info.getValue()
                    && this.getExpense().getId() == info.getExpense().getId();

            return equals && listsContentEquals(this.getCategories(), info.getCategories());
        }

        return false;
    }
}
