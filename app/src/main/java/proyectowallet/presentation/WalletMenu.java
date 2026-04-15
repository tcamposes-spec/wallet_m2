package proyectowallet.presentation;

import proyectowallet.controller.WalletController;
import proyectowallet.model.Currency;
import proyectowallet.model.Transaction;
import proyectowallet.model.User;
import proyectowallet.util.UIFormatter;
import java.util.*;

/**
 * Menú principal de la aplicación Alke Wallet.
 * <p>
 * <b>Capa de presentación</b>: se encarga únicamente de la interacción con el usuario
 * (entrada por consola y salida por pantalla). No contiene lógica de negocio;
 * delega todas las operaciones en la capa de aplicación ({@link WalletController}).
 */
public class WalletMenu {
    private final WalletController controller;
    private final Scanner scanner;
    private boolean running;

    // Constructor de la clase WalletMenu
    public WalletMenu(WalletController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
        this.running = true;
    }

    /**
     * Ejecuta el bucle del menú hasta que el usuario elige Salir.
     */
    public void run() {
        while (running) {
            // Imprimir el menú de opciones
            UIFormatter.printMenu(
                    "Registrar nuevo usuario",
                    "Crear cuenta",
                    "Ver saldo",
                    "Realizar depósito",
                    "Realizar retiro",
                    "Convertir moneda",
                    "Ver historial de transacciones",
                    "Salir"
            );

            // Leer la opción seleccionada por el usuario
            String choice = scanner.nextLine().trim();
            UIFormatter.printSeparator();

            // Evaluar la opción seleccionada por el usuario
            switch (choice) {
                case "1" -> handleUserRegistration();
                case "2" -> handleAccountCreation();
                case "3" -> handleViewBalance();
                case "4" -> handleDeposit();
                case "5" -> handleWithdrawal();
                case "6" -> handleCurrencyConversion();
                case "7" -> handleTransactionHistory();
                case "8" -> running = false;
                default -> UIFormatter.printWarning("Opción no válida. Por favor, intente de nuevo.");
            }
        }
    }

    // Método para manejar el registro de un nuevo usuario
    private void handleUserRegistration() {
        UIFormatter.printHeader("Registrar Nuevo Usuario");

        System.out.print("Nombre: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Apellido: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        if (controller.registerUser(firstName, lastName, email)) {
            pause();
            return;
        }

        pause();
    }

    // Método para manejar la creación de una nueva cuenta
    private void handleAccountCreation() {
        UIFormatter.printHeader("Crear Cuenta");

        if (controller.getAllUsers().isEmpty()) {
            UIFormatter.printError("No hay usuarios registrados. Registre uno primero.");
            pause();
            return;
        }

        System.out.println("Usuarios disponibles:");
        List<String> userIds = new ArrayList<>();
        int idx = 1;
        for (var user : controller.getAllUsers()) {
            System.out.println(idx + ". " + user.getFullName() + " (" + user.getEmail() + ")");
            userIds.add(user.getId());
            idx++;
        }

        System.out.print("\nSeleccione el número del usuario: ");
        String userChoice = scanner.nextLine().trim();
        int userIdx = parseMenuChoice(userChoice, userIds.size());

        if (userIdx < 0) {
            UIFormatter.printError("Opción inválida");
            pause();
            return;
        }

        String userId = userIds.get(userIdx);

        var existingAccounts = controller.getAccountsForUser(userId);
        if (!existingAccounts.isEmpty()) {
            System.out.println("\nCuentas existentes para este usuario:");
            for (var a : existingAccounts) {
                System.out.println("- " + a.getId() + " - " + UIFormatter.formatMoney(a.getBalance(), a.getCurrency()) + " (" + a.getCurrency() + ")");
            }
            System.out.println("\nCreando una nueva cuenta adicional...\n");
        }

        Currency[] currencies = Currency.values();
        System.out.println("\nMonedas disponibles (Enter = CLP por defecto):");
        for (int i = 0; i < currencies.length; i++) {
            System.out.println((i + 1) + ". " + currencies[i].getDescription() + " (" + currencies[i] + ")");
        }

        System.out.print("\nSeleccione la moneda (número) [Enter=1]: ");
        String currencyChoice = scanner.nextLine().trim();
        int currencyIdx;
        if (currencyChoice.isEmpty()) {
            currencyIdx = 0;
        } else {
            currencyIdx = parseMenuChoice(currencyChoice, currencies.length);
        }

        if (currencyIdx < 0) {
            UIFormatter.printError("Moneda inválida");
            pause();
            return;
        }

        Currency selectedCurrency = currencies[currencyIdx];

        System.out.print("\nSaldo inicial (" + selectedCurrency.getSymbol() + "): ");
        double initialBalance = parseDouble(scanner.nextLine().trim());

        if (initialBalance < 0) {
            UIFormatter.printError("El saldo debe ser mayor o igual a 0");
            pause();
            return;
        }

        if (controller.createAccountForUser(userId, selectedCurrency, initialBalance)) {
            pause();
            return;
        }

        pause();
    }

    // Método para manejar la visualización del saldo
    private void handleViewBalance() {
        UIFormatter.printHeader("Ver Saldo");

        if (controller.getAllUsers().isEmpty()) {
            UIFormatter.printError("No hay usuarios registrados.");
            pause();
            return;
        }

        List<User> users = new ArrayList<>(controller.getAllUsers());
        System.out.println("Usuarios disponibles:");
        for (int i = 0; i < users.size(); i++) {
            var u = users.get(i);
            System.out.println((i + 1) + ". " + u.getFullName() + " (" + u.getEmail() + ")");
        }

        System.out.print("\nSeleccione usuario (número) o ingrese 'A' para ver todas las cuentas: ");
        String choice = scanner.nextLine().trim();

        if (choice.equalsIgnoreCase("A")) {
            System.out.println("\nTodas las cuentas:");
            for (var u : controller.getAllUsers()) {
                var accounts = controller.getAccountsForUser(u.getId());
                for (var a : accounts) {
                    System.out.println("- Usuario: " + u.getFullName() + " | Cuenta: " + a.getId() + " | " + a.getCurrency() + " " + UIFormatter.formatMoney(a.getBalance(), a.getCurrency()));
                }
            }
            pause();
            return;
        }

        int userIdx = parseMenuChoice(choice, users.size());
        if (userIdx < 0) {
            UIFormatter.printError("Selección inválida");
            pause();
            return;
        }

        User selectedUser = users.get(userIdx);
        String userId = selectedUser.getId();

        var existingAccounts = controller.getAccountsForUser(userId);
        if (existingAccounts.isEmpty()) {
            System.out.println("El usuario no tiene cuentas.");
            pause();
            return;
        }

        if (existingAccounts.size() == 1) {
            var a = existingAccounts.get(0);
            controller.setCurrentAccount(a);
            System.out.println("Usuario: " + a.getUser().getFullName());
            System.out.println("Número de cuenta: " + a.getId());
            System.out.println("Moneda: " + a.getCurrency().getDescription());
            System.out.println("\nSaldo: " + UIFormatter.formatMoney(a.getBalance(), a.getCurrency()));
            pause();
            return;
        }

        System.out.println("\nCuentas del usuario:");
        for (int i = 0; i < existingAccounts.size(); i++) {
            var a = existingAccounts.get(i);
            System.out.println((i + 1) + ". " + a.getId() + " - " + UIFormatter.formatMoney(a.getBalance(), a.getCurrency()) + " (" + a.getCurrency() + ")");
        }

        System.out.print("\nSeleccione la cuenta a ver (número): ");
        String accChoice = scanner.nextLine().trim();
        int accIdx = parseMenuChoice(accChoice, existingAccounts.size());
        if (accIdx < 0) {
            UIFormatter.printError("Selección inválida");
            pause();
            return;
        }

        var account = existingAccounts.get(accIdx);
        controller.setCurrentAccount(account);
        System.out.println("Usuario: " + account.getUser().getFullName());
        System.out.println("Número de cuenta: " + account.getId());
        System.out.println("Moneda: " + account.getCurrency().getDescription());
        System.out.println("\nSaldo: " + UIFormatter.formatMoney(account.getBalance(), account.getCurrency()));
        System.out.println("Total depósitos: " + UIFormatter.formatMoney(
                controller.getTotalDeposits(account), account.getCurrency()));
        System.out.println("Total retiros: " + UIFormatter.formatMoney(
                controller.getTotalWithdrawals(account), account.getCurrency()));

        pause();
    }

    // Método para manejar el depósito de dinero
    private void handleDeposit() {
        UIFormatter.printHeader("Realizar Depósito");

        if (controller.getAllUsers().isEmpty()) {
            UIFormatter.printError("No hay usuarios registrados.");
            pause();
            return;
        }

        if (!selectUserAndAccount()) {
            return;
        }

        System.out.print("Monto a depositar: ");
        double amount = parseDouble(scanner.nextLine().trim());

        if (amount <= 0) {
            UIFormatter.printError("El monto debe ser mayor a 0");
            pause();
            return;
        }

        if (controller.deposit(amount)) {
            UIFormatter.printSuccess("Saldo actual: " + UIFormatter.formatMoney(controller.getBalance(), controller.getCurrentCurrency()));
        }

        pause();
    }

    // Método para manejar el retiro de dinero
    private void handleWithdrawal() {
        UIFormatter.printHeader("Realizar Retiro");

        if (controller.getAllUsers().isEmpty()) {
            UIFormatter.printError("No hay usuarios registrados.");
            pause();
            return;
        }

        if (!selectUserAndAccount()) {
            return;
        }

        System.out.println("Saldo disponible: " + UIFormatter.formatMoney(controller.getBalance(), controller.getCurrentCurrency()));
        System.out.print("Monto a retirar: ");
        double amount = parseDouble(scanner.nextLine().trim());

        if (amount <= 0) {
            UIFormatter.printError("El monto debe ser mayor a 0");
            pause();
            return;
        }

        if (controller.withdraw(amount)) {
            UIFormatter.printSuccess("Saldo actual: " + UIFormatter.formatMoney(controller.getBalance(), controller.getCurrentCurrency()));
        }

        pause();
    }

    // Método para manejar la conversión de moneda
    private void handleCurrencyConversion() {
        UIFormatter.printHeader("Convertir Moneda");

        if (controller.getAllUsers().isEmpty()) {
            UIFormatter.printError("No hay usuarios registrados.");
            pause();
            return;
        }

        if (!selectUserAndAccount()) {
            return;
        }

        Currency currentCurrency = controller.getCurrentCurrency();
        double currentBalance = controller.getBalance();

        System.out.println("\nMoneda actual: " + currentCurrency + " (" + currentCurrency.getDescription() + ")");
        System.out.println("Saldo actual: " + UIFormatter.formatMoney(currentBalance, currentCurrency) + "\n");

        Currency[] currencies = Currency.values();
        System.out.println("Monedas disponibles para conversión:");
        for (int i = 0; i < currencies.length; i++) {
            if (currencies[i] != currentCurrency) {
                System.out.println((i + 1) + ". " + currencies[i].getDescription() + " (" + currencies[i] + ")");
            }
        }

        System.out.print("\nSeleccione la moneda destino (número): ");
        String choice = scanner.nextLine().trim();
        int idx = parseMenuChoice(choice, currencies.length);

        if (idx < 0 || currencies[idx] == currentCurrency) {
            UIFormatter.printError("Selección inválida");
            pause();
            return;
        }

        Currency targetCurrency = currencies[idx];
        double convertedAmount = controller.convertBalance(targetCurrency);

        System.out.println("\n✓ Conversión realizada:");
        System.out.println("  De: " + UIFormatter.formatMoney(currentBalance, currentCurrency));
        System.out.println("  A: " + UIFormatter.formatMoney(convertedAmount, targetCurrency));
        System.out.println("  Tasa de cambio: " + controller.getExchangeRate(currentCurrency, targetCurrency));

        pause();
    }

    // Método para manejar el historial de transacciones
    private void handleTransactionHistory() {
        UIFormatter.printHeader("Historial de Transacciones");

        if (controller.getAllUsers().isEmpty()) {
            UIFormatter.printError("No hay usuarios registrados.");
            pause();
            return;
        }

        if (!selectUserAndAccount()) {
            return;
        }

        List<Transaction> transactions = controller.getTransactionHistory();

        if (transactions.isEmpty()) {
            System.out.println("\nNo hay transacciones registradas para esta cuenta.");
        } else {
            System.out.println("\nTotal de transacciones: " + transactions.size() + "\n");
            for (int i = transactions.size() - 1; i >= 0; i--) {
                var transaction = transactions.get(i);
                System.out.println("├─ " + transaction.getType().getDescription());
                System.out.println("│  Fecha: " + transaction.getTimestamp());
                System.out.println("│  Monto: " + UIFormatter.formatMoney(transaction.getAmount(), transaction.getCurrencyFrom()));
                if (transaction.getType() == Transaction.TransactionType.CONVERSION) {
                    System.out.println("│  Convertido a: " + UIFormatter.formatMoney(transaction.getAmountInTargetCurrency(), transaction.getCurrencyTo()));
                }
                System.out.println();
            }
        }

        pause();
    }

    // Método para seleccionar un usuario y una cuenta
    /**
     * Pide selección de usuario y cuenta y establece la cuenta actual en el controlador.
     * @return true si se seleccionó correctamente, false si hubo error (ya se hizo pause).
     */
    private boolean selectUserAndAccount() {
        List<User> users = new ArrayList<>(controller.getAllUsers());
        System.out.println("Usuarios disponibles:");
        for (int i = 0; i < users.size(); i++) {
            var u = users.get(i);
            System.out.println((i + 1) + ". " + u.getFullName() + " (" + u.getEmail() + ")");
        }

        System.out.print("\nSeleccione usuario (número): ");
        String userChoice = scanner.nextLine().trim();
        int userIdx = parseMenuChoice(userChoice, users.size());
        if (userIdx < 0) {
            UIFormatter.printError("Selección inválida");
            pause();
            return false;
        }

        User selectedUser = users.get(userIdx);
        var existingAccounts = controller.getAccountsForUser(selectedUser.getId());
        if (existingAccounts.isEmpty()) {
            UIFormatter.printError("El usuario no tiene cuentas.");
            pause();
            return false;
        }

        if (existingAccounts.size() == 1) {
            controller.setCurrentAccount(existingAccounts.get(0));
            return true;
        }

        System.out.println("\nCuentas del usuario:");
        for (int i = 0; i < existingAccounts.size(); i++) {
            var a = existingAccounts.get(i);
            System.out.println((i + 1) + ". " + a.getId() + " - " + UIFormatter.formatMoney(a.getBalance(), a.getCurrency()) + " (" + a.getCurrency() + ")");
        }

        System.out.print("\nSeleccione la cuenta (número): ");
        String accChoice = scanner.nextLine().trim();
        int accIdx = parseMenuChoice(accChoice, existingAccounts.size());
        if (accIdx < 0) {
            UIFormatter.printError("Selección inválida");
            pause();
            return false;
        }
        controller.setCurrentAccount(existingAccounts.get(accIdx));
        return true;
    }

    // Método para pausar la ejecución del programa
    private void pause() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Método para analizar la selección del menú
    private int parseMenuChoice(String input, int maxOptions) {
        try {
            int choice = Integer.parseInt(input) - 1;
            return (choice >= 0 && choice < maxOptions) ? choice : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Método para analizar la selección del menú
    /**
     * Analiza la selección del menú y devuelve el número de la opción seleccionada.
     * @param input La selección del usuario.
     * @param maxOptions El número de opciones disponibles.
     * @return El número de la opción seleccionada o -1 si la selección es inválida.
     */
    private double parseDouble(String input) {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
