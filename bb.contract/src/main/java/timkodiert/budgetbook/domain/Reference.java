package timkodiert.budgetbook.domain;

public record Reference<T>(Class<T> refClass, int id, String name) {
}
