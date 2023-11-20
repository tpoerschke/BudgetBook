package timkodiert.budgetBook.i18n;

import java.util.Locale;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeAll;
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
                Arguments.of(Locale.GERMAN, "Willkommen"),
                Arguments.of(Locale.ENGLISH, "Welcome")
        );
    }

    @ParameterizedTest
    @MethodSource("testArgs")
    void should_return_the_string_for_specified_language_and_key(Locale locale, String expectedString){
        String greetingKey = "main.greeting";
        LanguageManager languageManager = new LanguageManager(locale);
        languageManager.setLocale(locale);
        assertEquals(expectedString, languageManager.getLocString(greetingKey));
    }

    @Test
    void should_use_the_fallback_ResourceBundle_if_current_one_does_not_contain_key(){
        LanguageManager languageManager = new LanguageManager(Locale.GERMAN);
        assertEquals("Fallback", languageManager.getLocString("testing.fallback"));
    }
}