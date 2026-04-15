package proyectowallet.exception;

/**
 * Excepción lanzada cuando una operación de retiro excede el saldo disponible.
 * Implementa el patrón de excepciones personalizadas para mejor manejo de errores.
 */
public class InsufficientBalanceException extends Exception {
    private final double requiredAmount;
    private final double availableBalance;

    public InsufficientBalanceException(double requiredAmount, double availableBalance) {
        super("Saldo insuficiente. Requerido: " + requiredAmount + ", Disponible: " + availableBalance);
        this.requiredAmount = requiredAmount;
        this.availableBalance = availableBalance;
    }

    public double getRequiredAmount() {
        return requiredAmount;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public double getShortfall() {
        return requiredAmount - availableBalance;
    }
}
