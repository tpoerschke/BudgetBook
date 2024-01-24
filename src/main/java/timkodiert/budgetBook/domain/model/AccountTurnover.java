package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Nutzt die @CsvBindByPosition wegen Encoding-Problemen (s. Auftraggeber/Empfänger)
 */
@Getter
@NoArgsConstructor
@Entity
public class AccountTurnover extends BaseEntity {

    public static final int SKIP_LINES = 14;

    @CsvBindByPosition(position = 1)
    @CsvDate("dd.MM.yyyy")
    private LocalDate date;

    @CsvBindByPosition(position = 2)
    private String receiver;

    @CsvBindByPosition(position = 3)
    private String postingText;

    @CsvBindByPosition(position = 4)
    private String reference;

    @CsvBindByPosition(position = 7)
    @CsvNumber("#.###,##")
    private double amount;

    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fixed_expense_id")
    private FixedTurnover fixedExpense;

    @Setter
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "unique_expense_id")
    private UniqueTurnover uniqueExpense;

    public AccountTurnover(LocalDate date, String receiver, String postingText, String reference, double amount) {
        this.date = date;
        this.receiver = receiver;
        this.postingText = postingText;
        this.reference = reference;
        this.amount = amount;
    }

    @Override
    public boolean contentEquals(Object other) {
        if (other instanceof AccountTurnover accountTurnover) {
            return date.equals(accountTurnover.getDate())
                    && receiver.equals(accountTurnover.getReceiver())
                    && reference.equals(accountTurnover.getReference())
                    && amount == accountTurnover.getAmount();
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return contentEquals(obj);
    }
}
