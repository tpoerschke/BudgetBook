package timkodiert.budgetbook.domain.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class UniqueTurnoverInformation extends BaseEntity implements Categorizable {

    @Setter
    @NotBlank(message = "Die Position muss benannt werden.")
    private String label;

    @Setter
    private int value;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TurnoverDirection direction = TurnoverDirection.OUT;

    @Setter
    @ManyToOne
    @JoinColumn(name = "turnover_id", nullable = false)
    private UniqueTurnover expense;

    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Category> categories = new ArrayList<>();

    public static UniqueTurnoverInformation total(UniqueTurnover exp, int value) {
        UniqueTurnoverInformation info = new UniqueTurnoverInformation();
        info.setLabel("Gesamt");
        info.setValue(Math.abs(value));
        info.setDirection(value > 0 ? TurnoverDirection.IN : TurnoverDirection.OUT);
        info.setExpense(exp);
        return info;
    }

    public void setId(int id) {
        super.id = id;
    }

    public int getValueSigned() {
        return this.value * this.getDirection().getSign();
    }

}
