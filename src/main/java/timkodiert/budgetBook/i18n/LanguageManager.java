package timkodiert.budgetBook.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LanguageManager {

    private ResourceBundle resourceBundle;

    private Locale locale;

    public synchronized void setLocale(Locale locale){
        this.locale = locale;
        this.resourceBundle = ResourceBundle.getBundle("i18n.messages", this.locale);
    }

    public String getLocString(String key){
        return this.resourceBundle.getString(key);
    }
}
