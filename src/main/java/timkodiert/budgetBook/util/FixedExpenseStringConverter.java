package timkodiert.budgetBook.util;

import javafx.util.StringConverter;

import timkodiert.budgetBook.domain.model.FixedExpense;

import static timkodiert.budgetBook.util.ObjectUtils.nvl;

public class FixedExpenseStringConverter extends StringConverter<FixedExpense> {

    private static final String NULL_STRING = "---";

    @Override
    public String toString(FixedExpense fixedExpense) {
        return nvl(fixedExpense, FixedExpense::getPosition, NULL_STRING);
    }

    @Override
    public FixedExpense fromString(String s) {
        throw new UnsupportedOperationException();
    }
}
