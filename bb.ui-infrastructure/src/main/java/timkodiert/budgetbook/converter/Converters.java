package timkodiert.budgetbook.converter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import javafx.util.StringConverter;

import timkodiert.budgetbook.analysis.AnalysisPeriod;
import timkodiert.budgetbook.budget.BudgetType;
import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.domain.PaymentType;
import timkodiert.budgetbook.domain.TurnoverDirection;
import timkodiert.budgetbook.i18n.LanguageManager;

@Singleton
public class Converters {

    private static final Map<Class<?>, StringConverter<?>> CONVERTER_MAP = new HashMap<>();

    private final LanguageManager languageManager;

    @Inject
    public Converters(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    public void register() {
        CONVERTER_MAP.put(LocalDate.class, new LocalDateStringConverter());
        CONVERTER_MAP.put(TurnoverDirection.class, new EnumStringConverter<>(languageManager));
        CONVERTER_MAP.put(PaymentType.class, new EnumStringConverter<>(languageManager));
        CONVERTER_MAP.put(CategoryGroupDTO.class, new CategoryGroupStringConverter());
        CONVERTER_MAP.put(BudgetType.class, new EnumStringConverter<>(languageManager));
        CONVERTER_MAP.put(AnalysisPeriod.class, new EnumStringConverter<>(languageManager));
    }

    public static <T> StringConverter<T> get(Class<T> type) {
        return (StringConverter<T>) CONVERTER_MAP.get(type);
    }
}
