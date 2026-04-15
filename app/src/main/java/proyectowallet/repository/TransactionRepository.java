package proyectowallet.repository;

import proyectowallet.model.Transaction;
import proyectowallet.model.Account;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio para gestionar persistencia de transacciones.
 */
public interface TransactionRepository {
    /**
     * Guarda una transacción.
     * @param transaction transacción a guardar
     * @return transacción guardada
     */
    Transaction save(Transaction transaction);

    /**
     * Obtiene una transacción por ID.
     * @param id identificador de la transacción
     * @return Optional con la transacción si existe
     */
    Optional<Transaction> findById(String id);

    /**
     * Obtiene todas las transacciones.
     * @return lista de todas las transacciones
     */
    List<Transaction> findAll();

    /**
     * Obtiene todas las transacciones de una cuenta.
     * @param account cuenta
     * @return lista de transacciones de la cuenta
     */
    List<Transaction> findByAccount(Account account);

    /**
     * Obtiene el total de transacciones.
     * @return cantidad de transacciones
     */
    long count();

    /**
     * Obtiene el total de transacciones de una cuenta.
     * @param account cuenta
     * @return cantidad de transacciones de la cuenta
     */
    long countByAccount(Account account);
}
