package timkodiert.budgetBook.i18n;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        LanguageManager languageManager = new LanguageManager();
        languageManager.setLocale(locale);
        assertEquals(expectedString, languageManager.getLocString(greetingKey));
    }
}