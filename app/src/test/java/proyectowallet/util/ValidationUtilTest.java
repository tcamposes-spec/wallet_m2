package proyectowallet.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para ValidationUtil.
 * Valida las funciones de validación.
 */
@DisplayName("Pruebas de ValidationUtil")
class ValidationUtilTest {

    @Test
    @DisplayName("Debe validar email correctamente")
    void testEmailValidation() {
        assertTrue(ValidationUtil.isValidEmail("usuario@example.com"));
        assertFalse(ValidationUtil.isValidEmail("email-invalido"));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    @DisplayName("Debe validar monto correctamente")
    void testAmountValidation() {
        assertTrue(ValidationUtil.isValidAmount(100));
        assertTrue(ValidationUtil.isValidAmount(0.01));
        assertFalse(ValidationUtil.isValidAmount(-100));
        assertFalse(ValidationUtil.isValidAmount(0));
    }

    @Test
    @DisplayName("Debe validar nombre correctamente")
    void testNameValidation() {
        assertTrue(ValidationUtil.isValidName("Juan"));
        assertFalse(ValidationUtil.isValidName("J"));
        assertFalse(ValidationUtil.isValidName(""));
        assertFalse(ValidationUtil.isValidName(null));
    }
}
