package timkodiert.budgetBook.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import lombok.Getter;
import timkodiert.budgetBook.Constants;

public class PropertiesService {

    private static PropertiesService INSTANCE;

    @Getter
    private Properties properties;

    private PropertiesService() {
        this.properties = new Properties();
    }

    public void load() throws IOException {
        File propsFile = Path.of(Constants.PROPERTIES_PATH).toFile();
        if(!propsFile.exists()) {
            Path.of(propsFile.getParent()).toFile().mkdirs();
            propsFile.createNewFile();
            this.properties.setProperty("db", "jdbc:sqlite:" + Path.of(System.getProperty("user.home"), Constants.DATA_DIR , "sqlite.db").toString());
            this.properties.store(new FileWriter(propsFile), "Store initial props");
        }
        else {
            this.properties.load(new FileInputStream(Constants.PROPERTIES_PATH));
        }
    }

    public static PropertiesService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PropertiesService();
        }
        return INSTANCE;
    }
}
