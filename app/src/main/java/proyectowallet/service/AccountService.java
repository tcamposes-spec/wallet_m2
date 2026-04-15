package proyectowallet.service;

import proyectowallet.model.Account;
import proyectowallet.model.Currency;
import proyectowallet.model.User;
import java.util.List;

/**
 * Interfaz para servicios de gestión de cuentas.
 * Implementa principio SOLID: Dependency Inversion.
 */
public interface AccountService {
    /**
     * Crea una nueva cuenta para un usuario.
     * @param user usuario propietario
     * @param currency moneda de la cuenta
     * @param initialBalance saldo inicial
     * @return cuenta creada
     */
    Account createAccount(User user, Currency currency, double initialBalance);

    /**
     * Obtiene una cuenta por su ID.
     * @param accountId identificador de la cuenta
     * @return cuenta encontrada o null
     */
    Account getAccount(String accountId);

    /**
     * Deposita dinero en una cuenta.
     * @param accountId identificador de la cuenta
     * @param amount monto a depositar
     * @return true si la operación fue exitosa
     */
    boolean deposit(String accountId, double amount);

    /**
     * Retira dinero de una cuenta.
     * @param accountId identificador de la cuenta
     * @param amount monto a retirar
     * @return true si la operación fue exitosa
     */
    boolean withdraw(String accountId, double amount);

    /**
     * Obtiene el saldo de una cuenta.
     * @param accountId identificador de la cuenta
     * @return saldo disponible
     */
    double getBalance(String accountId);

    /**
     * Obtiene las cuentas asociadas a un usuario.
     * @param userId id del usuario
     * @return lista de cuentas (vacía si no hay)
     */
    List<Account> getAccountsByUser(String userId);
}
