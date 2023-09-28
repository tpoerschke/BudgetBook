package timkodiert.budgetBook.importer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import timkodiert.budgetBook.domain.model.AccountTurnover;

public class TurnoverImporter {

    public void read() throws FileNotFoundException {
        // TODO: Dem Nutzer sinnvolle Fehlermeldungen zeigen, wenn nicht geparst werden kann
        var builder = new CsvToBeanBuilder<AccountTurnover>(new FileReader("Umsatzanzeige_TEST_20230926.csv"));
        CsvToBean<AccountTurnover> toBean = builder.withSkipLines(13).withType(AccountTurnover.class).withSeparator(';').build();
        List<AccountTurnover> turnoverList = toBean.parse();
    }
}
