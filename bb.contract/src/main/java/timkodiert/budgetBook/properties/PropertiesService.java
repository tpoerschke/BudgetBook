package timkodiert.budgetBook.properties;

import java.io.IOException;
import java.util.Properties;

import javafx.stage.Stage;

public interface PropertiesService {
    void load() throws IOException;
    String getDbPath();
    String getLanguage();
    Properties getProperties();
    Stage buildWindow();
}
