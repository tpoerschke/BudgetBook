package timkodiert.budgetbook.domain.model;

import java.time.LocalDate;
import java.util.Objects;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import timkodiert.budgetbook.domain.converter.CsvAmountToIntegerConverter;


/**
 * Nutzt die @CsvBindByPosition wegen Encoding-Problemen (s. Auftraggeber/Empfänger)
 */
@Getter
@NoArgsConstructor
@Entity
public class AccountTurnover extends BaseEntity implements Comparable<AccountTurnover> {

    public static final int SKIP_LINES = 14;

    @CsvBindByPosition(position = 1)
    @CsvDate("dd.MM.yyyy")
    @Column(nullable = false)
    private LocalDate date;

    @CsvBindByPosition(position = 2)
    private String receiver;

    @CsvBindByPosition(position = 3)
    private String postingText;

    @CsvBindByPosition(position = 4)
    private String reference;

    @CsvCustomBindByPosition(position = 7, converter = CsvAmountToIntegerConverter.class)
    @Column(nullable = false)
    private int amount;

    @Setter
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "unique_turnover_id", nullable = false)
    private UniqueTurnover uniqueExpense;

    public AccountTurnover(LocalDate date, String receiver, String postingText, String reference, int amount) {
        this.date = date;
        this.receiver = receiver;
        this.postingText = postingText;
        this.reference = reference;
        this.amount = amount;
    }

    @SuppressWarnings("java:S1210")
    @Override
    public int compareTo(@NotNull AccountTurnover o) {
        return date.compareTo(o.getDate());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        AccountTurnover that = (AccountTurnover) other;
        return date.equals(that.getDate())
                && receiver.equals(that.getReceiver())
                && reference.equals(that.getReference())
                && amount == that.getAmount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, receiver, reference, amount);
    }
}
