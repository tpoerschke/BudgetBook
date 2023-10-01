package timkodiert.budgetBook.domain.model;

import java.time.LocalDate;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import lombok.Getter;

@Getter
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

    @Override
    public boolean contentEquals(Object other) {
        return false;
    }
}
