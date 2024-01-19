package timkodiert.budgetBook.util.string_converter;

import javafx.util.StringConverter;

import timkodiert.budgetBook.i18n.LanguageManager;

public class EnumStringConverter<T extends Enum<T>> extends StringConverter<T> {

    @Override
    public String toString(T t) {
        if(t == null) {
            return "---";
        }
        return LanguageManager.get(t.getClass().getSimpleName() + "." + t.name());
    }

    @Override
    public T fromString(String s) {
        throw new UnsupportedOperationException();
    }
}
