package proyectowallet.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Account.
 * Valida la creación y operaciones de cuentas.
 */
@DisplayName("Pruebas de Account")
class AccountTest {
    private User testUser;

    @BeforeEach
    void setup() {
        testUser = new User("Juan", "Pérez", "juan@example.com");
    }

    @Test
    @DisplayName("Debe crear cuenta correctamente")
    void testCreateAccount() {
        Account account = new Account(testUser, Currency.USD, 1000);

        assertNotNull(account.getId());
        assertEquals(testUser.getId(), account.getUser().getId());
        assertEquals(Currency.USD, account.getCurrency());
        assertEquals(1000, account.getBalance());
        assertTrue(account.isActive());
    }

    @Test
    @DisplayName("Debe activar y desactivar cuenta")
    void testActivateDeactivateAccount() {
        Account account = new Account(testUser, Currency.EUR, 500);

        assertTrue(account.isActive());
        account.deactivate();
        assertFalse(account.isActive());
    }

    @Test
    @DisplayName("Debe retornar moneda correcta")
    void testGetCurrency() {
        Account account = new Account(testUser, Currency.CLP, 10000);

        assertEquals(Currency.CLP, account.getCurrency());
    }

    @Test
    @DisplayName("Debe generar ID único para cada cuenta")
    void testAccountUniqueId() {
        Account account1 = new Account(testUser, Currency.USD, 1000);
        Account account2 = new Account(testUser, Currency.EUR, 500);

        assertNotEquals(account1.getId(), account2.getId());
    }

    @Test
    @DisplayName("Debe retornar saldo correcto")
    void testGetBalance() {
        Account account = new Account(testUser, Currency.USD, 2500.50);

        assertEquals(2500.50, account.getBalance());
    }
}
