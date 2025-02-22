package timkodiert.budgetbook.util;

import java.util.function.Function;

public class ObjectUtils {

    private ObjectUtils() {
    }

    public static <T, R> R nvl(T obj, Function<T, R> nullSafeOperation) {
        return nvl(obj, nullSafeOperation, null);
    }

    public static <T, R> R nvl(T obj, Function<T, R> nullSafeOperation, R nullSubstitution) {
        return obj == null ? nullSubstitution : nullSafeOperation.apply(obj);
    }

}
