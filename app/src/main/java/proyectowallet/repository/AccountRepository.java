package proyectowallet.repository;

import proyectowallet.model.Account;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz del repositorio para gestionar persistencia de cuentas.
 * Implementa el patrón Repository para abstracción de datos.
 * Permite cambiar fácilmente entre diferentes implementaciones de almacenamiento.
 */
public interface AccountRepository {
    /**
     * Guarda una cuenta.
     * @param account cuenta a guardar
     * @return cuenta guardada
     */
    Account save(Account account);

    /**
     * Obtiene una cuenta por ID.
     * @param id identificador de la cuenta
     * @return Optional con la cuenta si existe
     */
    Optional<Account> findById(String id);

    /**
     * Obtiene todas las cuentas.
     * @return lista de todas las cuentas
     */
    List<Account> findAll();

    /**
     * Obtiene todas las cuentas de un usuario.
     * @param userId identificador del usuario
     * @return lista de cuentas del usuario
     */
    List<Account> findByUserId(String userId);

    /**
     * Actualiza una cuenta.
     * @param account cuenta actualizada
     * @return cuenta actualizada
     */
    Account update(Account account);

    /**
     * Elimina una cuenta.
     * @param id identificador de la cuenta
     * @return true si se eliminó, false si no existe
     */
    boolean delete(String id);

    /**
     * Obtiene el total de cuentas.
     * @return cantidad de cuentas
     */
    long count();
}
