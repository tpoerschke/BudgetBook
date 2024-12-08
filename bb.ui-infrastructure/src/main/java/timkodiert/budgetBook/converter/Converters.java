package timkodiert.budgetBook.converter;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

import javafx.util.StringConverter;

import timkodiert.budgetBook.domain.model.BudgetType;
import timkodiert.budgetBook.domain.model.CategoryGroup;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.model.TurnoverDirection;
import timkodiert.budgetBook.i18n.LanguageManager;

@Singleton
public class Converters {

    private static final Map<Class<?>, StringConverter<?>> CONVERTER_MAP = new HashMap<>();
    static final String NULL_STRING = "---";

    private final LanguageManager languageManager;

    @Inject
    public Converters(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    public void register() {
        CONVERTER_MAP.put(TurnoverDirection.class, new EnumStringConverter<>(languageManager));
        CONVERTER_MAP.put(PaymentType.class, new EnumStringConverter<>(languageManager));
        CONVERTER_MAP.put(CategoryGroup.class, new CategoryGroupStringConverter());
        CONVERTER_MAP.put(BudgetType.class, new EnumStringConverter<BudgetType>(languageManager));
    }

    public static <T> StringConverter<T> get(Class<T> type) {
        return (StringConverter<T>) CONVERTER_MAP.get(type);
    }

}
