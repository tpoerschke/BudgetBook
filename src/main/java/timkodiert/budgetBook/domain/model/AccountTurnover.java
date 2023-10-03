package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class AccountTurnover extends BaseEntity {

    @CsvBindByName(column = "Valuta")
    @CsvDate("dd.MM.yyyy")
    private LocalDate date;

    @CsvBindByName(column = "Auftraggeber/Empfänger")
    private String receiver;

    @CsvBindByName(column = "Buchungstext")
    private String postingText;

    @CsvBindByName(column = "Verwendungszweck")
    private String reference;

    @CsvBindByName(column = "Betrag")
    @CsvNumber("#.###,##")
    private double amount;

    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fixed_expense_id")
    private FixedExpense fixedExpense;

    @Setter
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "unique_expense_id")
    private UniqueExpense uniqueExpense;

    @Override
    public boolean contentEquals(Object other) {
        return false;
    }
}
