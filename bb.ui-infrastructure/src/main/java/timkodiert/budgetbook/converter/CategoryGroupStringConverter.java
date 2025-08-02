package timkodiert.budgetbook.converter;

import javafx.util.StringConverter;

import timkodiert.budgetbook.domain.CategoryGroupDTO;

import static timkodiert.budgetbook.converter.Converters.NULL_STRING;
import static timkodiert.budgetbook.util.ObjectUtils.nvl;

public class CategoryGroupStringConverter extends StringConverter<CategoryGroupDTO> {

    @Override
    public String toString(CategoryGroupDTO categoryGroup) {
        return nvl(categoryGroup, CategoryGroupDTO::getName, NULL_STRING);
    }

    @Override
    public CategoryGroupDTO fromString(String s) {
        throw new UnsupportedOperationException();
    }
}
