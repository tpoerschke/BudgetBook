package timkodiert.budgetbook.converter;

import javafx.util.StringConverter;

import timkodiert.budgetbook.domain.model.FixedTurnover;

import static timkodiert.budgetbook.converter.Converters.NULL_STRING;
import static timkodiert.budgetbook.util.ObjectUtils.nvl;

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
