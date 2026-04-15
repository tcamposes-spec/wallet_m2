package proyectowallet.util;

/**
 * Utilidad para validaciones comunes en la aplicación.
 * Implementa principio SOLID: Single Responsibility.
 */
public class ValidationUtil {
    private ValidationUtil() {
        // Clase de utilidad, no debe instanciarse
    }

    /**
     * Valida si un email es válido.
     * @param email email a validar
     * @return true si el email es válido
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Valida si un monto es válido para operaciones.
     * @param amount monto a validar
     * @return true si el monto es válido
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0 && !Double.isInfinite(amount) && !Double.isNaN(amount);
    }

    /**
     * Valida si un nombre no está vacío.
     * @param name nombre a validar
     * @return true si el nombre es válido
     */
    public static boolean isValidName(String name) {
        return name != null && !name.isBlank() && name.length() >= 2;
    }

    /**
     * Formatea un error para presentación al usuario.
     * @param errorKey clave del error
     * @return mensaje de error formateado
     */
    public static String formatError(String errorKey) {
        return switch (errorKey) {
            case "INVALID_EMAIL" -> "El email ingresado no es válido";
            case "INVALID_NAME" -> "El nombre debe tener al menos 2 caracteres";
            case "INVALID_AMOUNT" -> "El monto debe ser mayor a 0";
            case "INSUFFICIENT_BALANCE" -> "Saldo insuficiente para realizar la operación";
            case "ACCOUNT_INACTIVE" -> "La cuenta se encuentra inactiva";
            case "ACCOUNT_NOT_FOUND" -> "La cuenta no existe";
            case "OPERATION_FAILED" -> "La operación no se pudo completar";
            default -> "Error desconocido";
        };
    }
}
