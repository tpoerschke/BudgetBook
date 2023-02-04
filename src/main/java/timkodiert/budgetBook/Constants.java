package timkodiert.budgetBook;

import java.nio.file.Path;

public class Constants {
    public static final String INITIAL_AMOUNT_STRING = "0,00 €";

    public static final String DATA_DIR = ".budgetbook";
    public static final String PROPERTIES_PATH = Path.of(System.getProperty("user.home"), DATA_DIR, "application.properties").toString();
}
