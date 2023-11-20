package timkodiert.budgetBook.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import lombok.Getter;

@Getter
public class LanguageManager {

    private static final String I_18_N_PACKAGE = "i18n.messages";

    private final ResourceBundle fallbackRB;

    private ResourceBundle resourceBundle;

    private Locale locale;

    public LanguageManager(Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle(I_18_N_PACKAGE, locale);
        this.fallbackRB = ResourceBundle.getBundle(I_18_N_PACKAGE, Locale.ENGLISH);
    }

    public synchronized void setLocale(Locale locale) {
        this.locale = locale;
        this.resourceBundle = ResourceBundle.getBundle(I_18_N_PACKAGE, this.locale);
    }

    public String getLocString(String key) {
        if (resourceBundleContainsKey(key)) {
            return this.getResourceBundle().getString(key);
        }
        return this.getFallbackRB().getString(key);
    }

    private boolean resourceBundleContainsKey(String key) {
        return this.resourceBundle.containsKey(key);
    }
}
