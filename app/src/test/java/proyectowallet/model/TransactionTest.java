package proyectowallet.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Transaction.
 * Valida la creación y propiedades de transacciones.
 */
@DisplayName("Pruebas de Transaction")
class TransactionTest {
    private Account testAccount;

    @BeforeEach
    void setup() {
        User user = new User("Juan", "Pérez", "juan@example.com");
        testAccount = new Account(user, Currency.USD, 1000);
    }

    @Test
    @DisplayName("Debe crear transacción de depósito")
    void testCreateDepositTransaction() {
        Transaction tx = new Transaction(
                testAccount,
                Transaction.TransactionType.DEPOSIT,
                100,
                Currency.USD,
                Currency.USD,
                100,
                "Depósito"
        );

        assertNotNull(tx.getId());
        assertEquals(testAccount.getId(), tx.getAccount().getId());
        assertEquals(Transaction.TransactionType.DEPOSIT, tx.getType());
        assertEquals(100, tx.getAmount());
        assertEquals(Currency.USD, tx.getCurrencyFrom());
        assertEquals(Currency.USD, tx.getCurrencyTo());
        assertEquals(100, tx.getAmountInTargetCurrency());
        assertNotNull(tx.getTimestamp());
    }

    @Test
    @DisplayName("Debe crear transacción de conversión")
    void testCreateConversionTransaction() {
        Transaction tx = new Transaction(
                testAccount,
                Transaction.TransactionType.CONVERSION,
                100,
                Currency.USD,
                Currency.EUR,
                85,
                "Conversión USD a EUR"
        );

        assertEquals(Transaction.TransactionType.CONVERSION, tx.getType());
        assertEquals(Currency.USD, tx.getCurrencyFrom());
        assertEquals(Currency.EUR, tx.getCurrencyTo());
        assertEquals(85, tx.getAmountInTargetCurrency());
    }

    @Test
    @DisplayName("Debe generar ID único para cada transacción")
    void testTransactionUniqueId() {
        Transaction tx1 = new Transaction(testAccount, Transaction.TransactionType.DEPOSIT,
                50, Currency.USD, Currency.USD, 50, "Depósito 1");
        Transaction tx2 = new Transaction(testAccount, Transaction.TransactionType.WITHDRAWAL,
                30, Currency.USD, Currency.USD, 30, "Retiro 1");

        assertNotEquals(tx1.getId(), tx2.getId());
    }

    @Test
    @DisplayName("Debe retornar descripción correcta")
    void testGetDescription() {
        String description = "Depósito de nómina";
        Transaction tx = new Transaction(testAccount, Transaction.TransactionType.DEPOSIT,
                1500, Currency.USD, Currency.USD, 1500, description);

        assertEquals(description, tx.getDescription());
    }

    @Test
    @DisplayName("Debe registrar timestamp de creación")
    void testTransactionTimestamp() {
        Transaction tx = new Transaction(testAccount, Transaction.TransactionType.DEPOSIT,
                100, Currency.USD, Currency.USD, 100, "Prueba timestamp");

        assertNotNull(tx.getTimestamp());
        assertFalse(tx.getTimestamp().toString().isEmpty());
    }
}
