package timkodiert.budgetBook.ui.control;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTextFieldControllerTest {

    @DisplayName("Set Value: Null")
    @Test
    void testNullValue() {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setValue(null);

        assertNull(controller.getValue());
        assertNull(controller.stringValueProperty().get());
    }

    @DisplayName("Set Value: 0,00")
    @Test
    void test0Value() {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setValue(0.0);

        assertEquals(0.0, controller.getValue());
        assertEquals("0,00", controller.stringValueProperty().get());
    }

    @DisplayName("Set Value: With Decimals")
    @ParameterizedTest
    @CsvSource({"0.99,'0,99'", "1.49,'1,49'", "1.9,'1,90'", "10.99,'10,99'", "100,'100,00'", "1000,'1000,00'"})
    void testDecimalsValue(double value, String expectedStr) {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setValue(value);

        assertEquals(value, controller.getValue());
        assertEquals(expectedStr, controller.stringValueProperty().get());
    }

    @DisplayName("Format: Null-Value")
    @ParameterizedTest
    @NullAndEmptySource
    void testFormatNullValue(String value) {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.stringValueProperty().set(value);
        assertTrue(controller.isStringFormatValid());
    }

    @DisplayName("Format: Different Values")
    @ParameterizedTest
    @CsvSource({"'0,00',true", "'10,00',true", "'100,00',true", "'10',false", "'10,0',false","'10,000',false","'-10,00',false"})
    void testFormatDifferentValues(String value, boolean expected) {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.stringValueProperty().set(value);
        assertEquals(expected, controller.isStringFormatValid());
    }

    @DisplayName("Format: Not a number")
    @Test
    void testFormatDifferentValues() {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.stringValueProperty().set("10,ab");
        assertFalse(controller.isStringFormatValid());
    }
}