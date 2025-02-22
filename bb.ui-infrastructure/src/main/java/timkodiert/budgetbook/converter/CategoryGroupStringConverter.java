package timkodiert.budgetbook.converter;

import javafx.util.StringConverter;

import timkodiert.budgetbook.domain.model.CategoryGroup;

import static timkodiert.budgetbook.converter.Converters.NULL_STRING;
import static timkodiert.budgetbook.util.ObjectUtils.nvl;

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
