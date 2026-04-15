package proyectowallet.exception;

/**
 * Excepción lanzada cuando no se encuentra una cuenta solicitada.
 */
public class AccountNotFoundException extends Exception {
    private final String accountId;

    public AccountNotFoundException(String accountId) {
        super("Cuenta no encontrada: " + accountId);
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
