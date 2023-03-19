package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
public class UniqueExpense {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;

    @Setter
    private String biller;

    @Setter
    private LocalDate date;

    @Setter
    private String note;

    @Setter
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<UniqueExpenseInformation> paymentInformations = new ArrayList<>();

    @Transient
    private UniqueExpenseAdapter adapter;

    public UniqueExpense() {
        initAdapter();
    }

    public void initAdapter() {
        try {
            this.adapter = new UniqueExpenseAdapter(this);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public double getTotalValue() {
        return 0;
    }
}
