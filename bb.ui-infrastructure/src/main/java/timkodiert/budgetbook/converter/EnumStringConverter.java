package timkodiert.budgetbook.converter;

import javax.inject.Inject;

import javafx.util.StringConverter;

import timkodiert.budgetbook.i18n.LanguageManager;

public class EnumStringConverter<T extends Enum<T>> extends StringConverter<T> {

    private final LanguageManager languageManager;

    @Inject
    public EnumStringConverter(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    @Override
    public String toString(T t) {
        if(t == null) {
            return "---";
        }
        return languageManager.get(t.getClass().getSimpleName() + "." + t.name());
    }

    @Override
    public T fromString(String s) {
        throw new UnsupportedOperationException();
    }
}
