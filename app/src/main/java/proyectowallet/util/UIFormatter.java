package proyectowallet.util;

import proyectowallet.model.Currency;
import java.text.DecimalFormat;

/**
 * Utilidad para formateo de interfaz de usuario.
 * Implementa principio SOLID: Single Responsibility.
 * Mejora la experiencia del usuario (UX) con formatos claros.
 */
public class UIFormatter {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";

    private UIFormatter() {
        // Clase de utilidad
    }

    /**
     * Formatea un monto de dinero con símbolo de moneda.
     * @param amount monto
     * @param currency moneda
     * @return monto formateado
     */
    public static String formatMoney(double amount, Currency currency) {
        return currency.getSymbol() + " " + DECIMAL_FORMAT.format(amount);
    }

    /**
     * Formatea un monto sin símbolo.
     * @param amount monto
     * @return monto formateado
     */
    public static String formatAmount(double amount) {
        return DECIMAL_FORMAT.format(amount);
    }

    // Métodos para colores de consola (mejora UX)
    public static String green(String text) {
        return GREEN + text + RESET;
    }

    public static String red(String text) {
        return RED + text + RESET;
    }

    public static String blue(String text) {
        return BLUE + text + RESET;
    }

    public static String yellow(String text) {
        return YELLOW + text + RESET;
    }

    public static String bold(String text) {
        return BOLD + text + RESET;
    }

    /**
     * Imprime un encabezado formateado.
     * @param title título del encabezado
     */
    public static void printHeader(String title) {
        System.out.println("\n" + blue("═══════════════════════════════════════════"));
        System.out.println(bold(blue(title)));
        System.out.println(blue("═══════════════════════════════════════════\n"));
    }

    /**
     * Imprime un mensaje de éxito.
     * @param message mensaje
     */
    public static void printSuccess(String message) {
        System.out.println(green("✓ " + message));
    }

    /**
     * Imprime un mensaje de error.
     * @param message mensaje
     */
    public static void printError(String message) {
        System.out.println(red("✗ " + message));
    }

    /**
     * Imprime un mensaje de advertencia.
     * @param message mensaje
     */
    public static void printWarning(String message) {
        System.out.println(yellow("⚠ " + message));
    }

    /**
     * Imprime un separador.
     */
    public static void printSeparator() {
        System.out.println("─────────────────────────────────────────────\n");
    }

    /**
     * Imprime un menú de opciones.
     * @param options opciones del menú
     */
    public static void printMenu(String... options) {
        printSeparator();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print("\nSeleccione una opción: ");
    }
}
