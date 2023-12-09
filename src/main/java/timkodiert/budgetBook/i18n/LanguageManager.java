package timkodiert.budgetBook.i18n;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;

import lombok.Getter;

import timkodiert.budgetBook.util.PropertiesService;

@Getter
public class LanguageManager {

    public static final List<String> MONTH_NAMES = List.of("month.january",
                                                           "month.february",
                                                           "month.march",
                                                           "month.april",
                                                           "month.may",
                                                           "month.june",
                                                           "month.july",
                                                           "month.august",
                                                           "month.september",
                                                           "month.october",
                                                           "month.november",
                                                           "month.december");

    private static final String I_18_N_PACKAGE = "i18n.messages";

    private static LanguageManager INSTANCE;

    private ResourceBundle fallbackRB;

    private ResourceBundle resourceBundle;

    Locale locale;

    private void initialize(Locale locale) {
        this.resourceBundle = ResourceBundle.getBundle(I_18_N_PACKAGE, locale);
        this.fallbackRB = ResourceBundle.getBundle(I_18_N_PACKAGE, ENGLISH);
    }

    private LanguageManager(){
        // Map String to Locale
        this.initialize(mapToLocale(PropertiesService.getInstance().getProperties().getProperty("language")));
    }

    public static LanguageManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new LanguageManager();
        }
        return INSTANCE;
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

    // Intermediate solution until we have a dropdown menu for languages
    public static Locale mapToLocale(String language) {
        if(language == null){
            return ENGLISH;
        }
        if (language.equalsIgnoreCase("Deutsch")) {
            return GERMAN;
        } else if (language.equalsIgnoreCase("English")) {
            return ENGLISH;
        } else {
            // Default to Locale.ENGLISH if the language is not recognized
            return ENGLISH;
        }
    }

    private boolean resourceBundleContainsKey(String key) {
        return this.resourceBundle.containsKey(key);
    }
}
