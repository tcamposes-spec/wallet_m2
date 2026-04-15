package proyectowallet.exception;

/**
 * Excepción lanzada cuando una operación no es válida o está prohibida.
 */
public class InvalidOperationException extends Exception {
    private final String operation;
    private final String reason;

    public InvalidOperationException(String operation, String reason) {
        super("Operación inválida: " + operation + ". Razón: " + reason);
        this.operation = operation;
        this.reason = reason;
    }

    public String getOperation() {
        return operation;
    }

    public String getReason() {
        return reason;
    }
}
