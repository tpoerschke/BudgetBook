package timkodiert.budgetBook.i18n;

import java.util.Locale;
import java.util.stream.Stream;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LanguageManagerTest {

    static Stream<Arguments> testArgs(){
        return Stream.of(
                Arguments.of(GERMAN, "Willkommen", "Deutsch"),
                Arguments.of(ENGLISH, "Welcome", "English")
        );
    }

    @ParameterizedTest
    @MethodSource("testArgs")
    void should_return_the_string_for_specified_language_and_key(Locale locale, String expectedString){
        String greetingKey = "main.greeting";
        LanguageManager.getInstance().setLocale(locale); // Setting the locale explicitly because in test context, the loaded property will be null.
        assertEquals(expectedString, LanguageManager.getInstance().getLocString(greetingKey));
    }

    @Test
    void should_use_the_fallback_ResourceBundle_if_current_one_does_not_contain_key(){
        LanguageManager.getInstance().setLocale(GERMAN);
        assertEquals("Fallback", LanguageManager.getInstance().getLocString("testing.fallback"));
    }
}