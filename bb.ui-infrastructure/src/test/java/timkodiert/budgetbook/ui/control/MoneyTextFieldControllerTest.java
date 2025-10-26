package timkodiert.budgetbook.ui.control;

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

    @DisplayName("Set Value: Null (null values allowed)")
    @Test
    void testNullValueAllowed() {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setNullable(true);
        controller.setValue(null);

        assertNull(controller.getValue());
        assertNull(controller.stringValueProperty().get());
        assertNull(controller.integerValueProperty().get());
        assertTrue(controller.isStringFormatValid());
    }

    @DisplayName("Set Value: Null (null values not allowed)")
    @Test
    void testNullValueNotAllowed() {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setNullable(false);
        controller.setValue(null);

        assertEquals(0.0, controller.getValue());
        assertNull(controller.stringValueProperty().get());
        assertEquals(0, controller.integerValueProperty().get());
        assertFalse(controller.isStringFormatValid());
    }

    @DisplayName("Set Value via IntegerProperty: Null (null values allowed)")
    @Test
    void testNullValueAllowedViaIntegerProperty() {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setNullable(true);
        controller.integerValueProperty().set(null);

        assertNull(controller.getValue());
        assertNull(controller.stringValueProperty().get());
        assertNull(controller.integerValueProperty().get());
        assertTrue(controller.isStringFormatValid());
    }

    @DisplayName("Set Value via IntegerProperty: Null (null values not allowed)")
    @Test
    void testNullValueNotAllowedViaIntegerProperty() {
        // Deckt einen Fall ab, der nicht vorkommen sollte, da die über die IntegerProperty
        // keine Nullwerte gesetzt werden sollten, wenn null nicht zulässig ist, daher
        // wird hier ein tewas inkonsistentes Verhalten hingenommen...
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setNullable(false);
        controller.integerValueProperty().set(null);

        assertEquals(0.0, controller.getValue());
        assertEquals("0,00", controller.stringValueProperty().get());
        assertNull(controller.integerValueProperty().get());
        assertTrue(controller.isStringFormatValid());
    }

    @DisplayName("Set Value: 0,00")
    @Test
    void test0Value() {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setValue(0.0);

        assertEquals(0.0, controller.getValue());
        assertEquals("0,00", controller.stringValueProperty().get());
        assertEquals(0, controller.integerValueProperty().get());
        assertTrue(controller.isStringFormatValid());
    }

    @DisplayName("Set Value: With Decimals")
    @ParameterizedTest
    @CsvSource({"0.99,'0,99'", "1.49,'1,49'", "1.9,'1,90'", "10.99,'10,99'", "100,'100,00'", "1000,'1000,00'"})
    void testDecimalsValue(double value, String expectedStr) {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setValue(value);

        assertEquals(value, controller.getValue());
        assertEquals(expectedStr, controller.stringValueProperty().get());
        assertEquals((int) (value * 100), controller.integerValueProperty().get());
    }

    @DisplayName("Set Value: Via IntegerProperty")
    @ParameterizedTest
    @CsvSource({"0,'0,00'", "149,'1,49'", "190,'1,90'", "1099,'10,99'", "10000,'100,00'", "100000,'1000,00'"})
    void testDecimalsValueViaIntegerProperty(int value, String expectedStr) {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.integerValueProperty().set(value);

        assertEquals(value / 100.0, controller.getValue());
        assertEquals(expectedStr, controller.stringValueProperty().get());
    }

    @DisplayName("Format: Null-Value (null allowed)")
    @ParameterizedTest
    @NullAndEmptySource
    void testFormatNullValueNullAllowed(String value) {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setNullable(true);
        controller.stringValueProperty().set(value);
        assertTrue(controller.isStringFormatValid());
    }

    @DisplayName("Format: Null-Value (null not allowed)")
    @ParameterizedTest
    @NullAndEmptySource
    void testFormatNullValueNullNotAllowed(String value) {
        MoneyTextFieldController controller = new MoneyTextFieldController();
        controller.setNullable(false);
        controller.stringValueProperty().set(value);
        assertFalse(controller.isStringFormatValid());
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