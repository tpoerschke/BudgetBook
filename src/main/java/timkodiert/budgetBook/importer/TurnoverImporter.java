package timkodiert.budgetBook.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.opencsv.bean.CsvToBeanBuilder;

import timkodiert.budgetBook.domain.model.AccountTurnover;

public class TurnoverImporter {

    public List<ImportInformation> parse(File file) throws FileNotFoundException, IllegalStateException {
        var builder = new CsvToBeanBuilder<AccountTurnover>(new FileReader(file));
        List<AccountTurnover> imports = builder.withSkipLines(13).withSeparator(';').withType(AccountTurnover.class).build().parse();
        return imports.stream().map(ImportInformation::from).toList();
    }
}
