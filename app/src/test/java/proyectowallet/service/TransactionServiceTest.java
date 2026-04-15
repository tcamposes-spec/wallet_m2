package proyectowallet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import proyectowallet.model.Account;
import proyectowallet.model.Currency;
import proyectowallet.model.Transaction;
import proyectowallet.model.User;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para TransactionService.
 * Valida el registro y consulta de transacciones.
 */
@DisplayName("Pruebas de TransactionService")
class TransactionServiceTest {
    private TransactionService transactionService;
    private AccountService accountService;
    private User testUser;
    private Account testAccount;

    @BeforeEach
    void setup() {
        transactionService = new TransactionServiceImpl();
        accountService = new AccountServiceImpl();
        testUser = new User("Juan", "Pérez", "juan@example.com");
        testAccount = accountService.createAccount(testUser, Currency.USD, 1000);
    }

    @Test
    @DisplayName("Debe registrar transacción correctamente")
    void testRecordTransaction() {
        Transaction transaction = new Transaction(
                testAccount,
                Transaction.TransactionType.DEPOSIT,
                100,
                Currency.USD,
                Currency.USD,
                100,
                "Depósito de prueba"
        );

        boolean result = transactionService.recordTransaction(transaction);
        assertTrue(result);
    }

    @Test
    @DisplayName("No debe registrar transacción nula")
    void testRecordNullTransaction() {
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.recordTransaction(null));
    }

    @Test
    @DisplayName("Debe obtener historial de transacciones")
    void testGetTransactionHistory() {
        // Crear varias transacciones
        for (int i = 0; i < 3; i++) {
            Transaction tx = new Transaction(
                    testAccount,
                    Transaction.TransactionType.DEPOSIT,
                    100,
                    Currency.USD,
                    Currency.USD,
                    100,
                    "Depósito " + (i + 1)
            );
            transactionService.recordTransaction(tx);
        }

        var history = transactionService.getTransactionHistory(testAccount);
        assertEquals(3, history.size());
    }

    @Test
    @DisplayName("Debe calcular total de depósitos")
    void testGetTotalDeposits() {
        Transaction tx1 = new Transaction(testAccount, Transaction.TransactionType.DEPOSIT, 100,
                Currency.USD, Currency.USD, 100, "Depósito 1");
        Transaction tx2 = new Transaction(testAccount, Transaction.TransactionType.DEPOSIT, 50,
                Currency.USD, Currency.USD, 50, "Depósito 2");
        Transaction tx3 = new Transaction(testAccount, Transaction.TransactionType.WITHDRAWAL, 30,
                Currency.USD, Currency.USD, 30, "Retiro");

        transactionService.recordTransaction(tx1);
        transactionService.recordTransaction(tx2);
        transactionService.recordTransaction(tx3);

        double totalDeposits = transactionService.getTotalDeposits(testAccount);
        assertEquals(150, totalDeposits);
    }

    @Test
    @DisplayName("Debe calcular total de retiros")
    void testGetTotalWithdrawals() {
        Transaction tx1 = new Transaction(testAccount, Transaction.TransactionType.WITHDRAWAL, 100,
                Currency.USD, Currency.USD, 100, "Retiro 1");
        Transaction tx2 = new Transaction(testAccount, Transaction.TransactionType.WITHDRAWAL, 50,
                Currency.USD, Currency.USD, 50, "Retiro 2");

        transactionService.recordTransaction(tx1);
        transactionService.recordTransaction(tx2);

        double totalWithdrawals = transactionService.getTotalWithdrawals(testAccount);
        assertEquals(150, totalWithdrawals);
    }
}
