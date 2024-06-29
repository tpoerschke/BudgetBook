package timkodiert.budgetBook.view;

import java.io.IOException;
import java.net.URL;

public interface BbFxmlLoader {

    void setLocation(URL location);
    <T> T getController();
    <T> T load() throws IOException;

}
