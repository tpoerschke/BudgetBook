package timkodiert.budgetBook.properties;

public enum OperationMode {
    DEV, TEST, PRODUCTION;

    public String getPropertiesPostfix() {
        return switch (this) {
            case DEV -> "_dev";
            case TEST -> "_test";
            default -> "";
        };
    }
}
