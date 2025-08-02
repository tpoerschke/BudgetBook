package timkodiert.budgetbook.converter;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

import javafx.util.StringConverter;

import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.model.BudgetType;
import timkodiert.budgetbook.domain.model.PaymentType;
import timkodiert.budgetbook.domain.model.TurnoverDirection;
import timkodiert.budgetbook.i18n.LanguageManager;

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
        CONVERTER_MAP.put(CategoryGroupDTO.class, new CategoryGroupStringConverter());
        CONVERTER_MAP.put(BudgetType.class, new EnumStringConverter<BudgetType>(languageManager));
        CONVERTER_MAP.put(Reference.class, new ReferenceStringConverter());
    }

    public static <T> StringConverter<T> get(Class<T> type) {
        return (StringConverter<T>) CONVERTER_MAP.get(type);
    }
}
