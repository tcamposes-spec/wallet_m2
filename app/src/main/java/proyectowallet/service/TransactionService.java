package proyectowallet.service;

import proyectowallet.model.Transaction;
import proyectowallet.model.Account;
import java.util.List;

/**
 * Interfaz para servicios de gestión de transacciones.
 * Implementa principio SOLID: Dependency Inversion.
 */
public interface TransactionService {
    /**
     * Registra una transacción.
     * @param transaction transacción a registrar
     * @return true si se registró exitosamente
     */
    boolean recordTransaction(Transaction transaction);

    /**
     * Obtiene el historial de transacciones de una cuenta.
     * @param account cuenta de la cual obtener el historial
     * @return lista de transacciones
     */
    List<Transaction> getTransactionHistory(Account account);

    /**
     * Obtiene el total de depósitos para una cuenta.
     * @param account cuenta a analizar
     * @return total de depósitos
     */
    double getTotalDeposits(Account account);

    /**
     * Obtiene el total de retiros para una cuenta.
     * @param account cuenta a analizar
     * @return total de retiros
     */
    double getTotalWithdrawals(Account account);
}
