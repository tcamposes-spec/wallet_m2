package proyectowallet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import proyectowallet.model.Account;
import proyectowallet.model.Currency;
import proyectowallet.model.User;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para AccountService.
 * Valida las operaciones de gestión de cuentas.
 */
@DisplayName("Pruebas de AccountService")
class AccountServiceTest {
    private AccountService accountService;
    private User testUser;

    @BeforeEach
    void setup() {
        accountService = new AccountServiceImpl();
        testUser = new User("Juan", "Pérez", "juan@example.com");
    }

    @Test
    @DisplayName("Debe crear una cuenta correctamente")
    void testCreateAccount() {
        Account account = accountService.createAccount(testUser, Currency.USD, 1000);

        assertNotNull(account);
        assertEquals(testUser.getId(), account.getUser().getId());
        assertEquals(Currency.USD, account.getCurrency());
        assertEquals(1000, account.getBalance());
        assertTrue(account.isActive());
    }

    @Test
    @DisplayName("No debe crear cuenta con parámetros nulos")
    void testCreateAccountWithNullParameters() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(null, Currency.USD, 1000));

        assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(testUser, null, 1000));
    }

    @Test
    @DisplayName("No debe crear cuenta con saldo negativo")
    void testCreateAccountWithNegativeBalance() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(testUser, Currency.USD, -100));
    }

    @Test
    @DisplayName("Debe obtener una cuenta por ID")
    void testGetAccount() {
        Account createdAccount = accountService.createAccount(testUser, Currency.EUR, 500);
        Account retrievedAccount = accountService.getAccount(createdAccount.getId());

        assertNotNull(retrievedAccount);
        assertEquals(createdAccount.getId(), retrievedAccount.getId());
    }

    @Test
    @DisplayName("Debe retornar null al obtener cuenta inexistente")
    void testGetNonExistentAccount() {
        Account account = accountService.getAccount("id-inexistente");
        assertNull(account);
    }

    @Test
    @DisplayName("Debe realizar depósito correctamente")
    void testDeposit() {
        Account account = accountService.createAccount(testUser, Currency.USD, 100);
        boolean result = accountService.deposit(account.getId(), 50);

        assertTrue(result);
        assertEquals(150, accountService.getBalance(account.getId()));
    }

    @Test
    @DisplayName("No debe permitir depósito de monto negativo")
    void testDepositNegativeAmount() {
        Account account = accountService.createAccount(testUser, Currency.USD, 100);
        boolean result = accountService.deposit(account.getId(), -50);

        assertFalse(result);
        assertEquals(100, accountService.getBalance(account.getId()));
    }

    @Test
    @DisplayName("No debe permitir depósito en cuenta inexistente")
    void testDepositToNonExistentAccount() {
        assertFalse(accountService.deposit("id-inexistente", 100));
    }

    @Test
    @DisplayName("Debe realizar retiro correctamente")
    void testWithdraw() {
        Account account = accountService.createAccount(testUser, Currency.USD, 500);
        boolean result = accountService.withdraw(account.getId(), 100);

        assertTrue(result);
        assertEquals(400, accountService.getBalance(account.getId()));
    }

    @Test
    @DisplayName("No debe permitir retiro superior al saldo")
    void testWithdrawMoreThanBalance() {
        Account account = accountService.createAccount(testUser, Currency.USD, 100);
        boolean result = accountService.withdraw(account.getId(), 200);

        assertFalse(result);
        assertEquals(100, accountService.getBalance(account.getId()));
    }

    @Test
    @DisplayName("No debe permitir retiro de monto negativo")
    void testWithdrawNegativeAmount() {
        Account account = accountService.createAccount(testUser, Currency.USD, 100);
        boolean result = accountService.withdraw(account.getId(), -50);

        assertFalse(result);
        assertEquals(100, accountService.getBalance(account.getId()));
    }

    @Test
    @DisplayName("No debe permitir retiro en cuenta inactiva")
    void testWithdrawFromInactiveAccount() {
        Account account = accountService.createAccount(testUser, Currency.USD, 100);
        account.deactivate();

        boolean result = accountService.withdraw(account.getId(), 50);
        assertFalse(result);
    }
}
