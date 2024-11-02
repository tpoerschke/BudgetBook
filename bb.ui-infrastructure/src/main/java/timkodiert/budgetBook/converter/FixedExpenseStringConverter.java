package timkodiert.budgetBook.converter;

import javafx.util.StringConverter;

import timkodiert.budgetBook.domain.model.FixedTurnover;

import static timkodiert.budgetBook.converter.Converters.NULL_STRING;
import static timkodiert.budgetBook.util.ObjectUtils.nvl;

public class FixedExpenseStringConverter extends StringConverter<FixedTurnover> {

    @Override
    public String toString(FixedTurnover fixedExpense) {
        return nvl(fixedExpense, FixedTurnover::getPosition, NULL_STRING);
    }

    @Override
    public FixedTurnover fromString(String s) {
        throw new UnsupportedOperationException();
    }
}
