package timkodiert.budgetbook.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import static timkodiert.budgetbook.domain.model.ContentEquals.listsContentEquals;

@Getter
@Entity
@NoArgsConstructor
public class UniqueTurnoverInformation implements Categorizable, ContentEquals {

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
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TurnoverDirection direction = TurnoverDirection.OUT;

    @Setter
    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private UniqueTurnover expense;

    @Setter
    @ManyToMany(cascade = { CascadeType.PERSIST })
    private List<Category> categories = new ArrayList<>();

    public static UniqueTurnoverInformation total(UniqueTurnover exp, double value) {
        UniqueTurnoverInformation info = new UniqueTurnoverInformation();
        info.setLabel("Gesamt");
        info.setValue(Math.abs(value));
        info.setDirection(value > 0 ? TurnoverDirection.IN : TurnoverDirection.OUT);
        info.setExpense(exp);
        return info;
    }

    public double getValueSigned() {
        return this.value * this.getDirection().getSign();
    }

    @Override
    public boolean contentEquals(Object other) {

        if (other instanceof UniqueTurnoverInformation info) {
            boolean equals = Objects.equals(this.getLabel(), info.getLabel())
                    && this.getValue() == info.getValue()
                    && this.getExpense().getId() == info.getExpense().getId();

            return equals && listsContentEquals(this.getCategories(), info.getCategories());
        }

        return false;
    }
}
