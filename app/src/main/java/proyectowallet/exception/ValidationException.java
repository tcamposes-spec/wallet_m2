package proyectowallet.exception;

/**
 * Excepción lanzada cuando la validación de datos falla.
 */
public class ValidationException extends Exception {
    private final String field;
    private final String value;

    public ValidationException(String field, String value, String message) {
        super("Error de validación en campo '" + field + "': " + message);
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
