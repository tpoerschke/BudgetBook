package timkodiert.budgetBook.util;

import java.util.function.Function;

public class ObjectUtils {

    private ObjectUtils() {
    }

    public static <T, R> R nvl(T obj, Function<T, R> nullSafeOperation) {
        return obj == null ? null : nullSafeOperation.apply(obj);
    }

}
