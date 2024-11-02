package timkodiert.budgetBook.converter;

import javafx.util.StringConverter;

import timkodiert.budgetBook.domain.model.CategoryGroup;

import static timkodiert.budgetBook.converter.Converters.NULL_STRING;
import static timkodiert.budgetBook.util.ObjectUtils.nvl;

public class CategoryGroupStringConverter extends StringConverter<CategoryGroup> {

    @Override
    public String toString(CategoryGroup categoryGroup) {
        return nvl(categoryGroup, CategoryGroup::getName, NULL_STRING);
    }

    @Override
    public CategoryGroup fromString(String s) {
        throw new UnsupportedOperationException();
    }
}
