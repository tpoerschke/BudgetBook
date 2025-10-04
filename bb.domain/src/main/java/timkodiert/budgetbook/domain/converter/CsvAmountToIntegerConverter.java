package timkodiert.budgetbook.domain.converter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

// Eigentlich müsste man die Annotationen aus der AccountTurnover-Entity lösen
// und eine Bean mit den Annotationen im BL-Modul anlegen, dann kann auch dieser
// Converter hier verschoben werden
public class CsvAmountToIntegerConverter extends AbstractBeanField<Integer, String> {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.GERMANY);

    @Override
    protected Integer convert(String value) throws CsvDataTypeMismatchException {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            double number = NUMBER_FORMAT.parse(value.trim()).doubleValue();
            return (int)(number * 100);
        } catch (ParseException e) {
            throw new CsvDataTypeMismatchException(e.getMessage());
        }
    }
}
