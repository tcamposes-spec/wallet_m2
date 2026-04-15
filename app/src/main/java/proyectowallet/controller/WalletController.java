package proyectowallet.controller;

import proyectowallet.model.*;
import proyectowallet.model.Currency;
import proyectowallet.service.*;
import proyectowallet.util.*;
import java.util.*;

/**
 * Orquestador principal de los casos de uso de Alke Wallet.
 * Capa de aplicación: coordina las operaciones de la billetera (registro,
 * cuentas, depósitos, retiros, conversión) y delega en los servicios de dominio.
 * No contiene lógica de presentación; es usado por la capa de presentación.
 * Implementa principios SOLID: Dependency Injection.
 */
public class WalletController {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final CurrencyConverterService currencyConverter;
    private final Map<String, User> users;
    private Account currentAccount;

    // Constructor de la clase WalletController
    public WalletController(AccountService accountService,
                          TransactionService transactionService,
                          CurrencyConverterService currencyConverter) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.currencyConverter = currencyConverter;
        this.users = new LinkedHashMap<>();
        this.currentAccount = null;
    }

    /**
     * Comprueba que haya una cuenta actual; si no, imprime error y devuelve false.
     */
    private boolean ensureCurrentAccount() {
        if (currentAccount == null) {
            UIFormatter.printError(ValidationUtil.formatError("ACCOUNT_NOT_FOUND"));
            return false;
        }
        return true;
    }

    /**
     * Valida el monto; si es inválido, imprime error y devuelve false.
     */
    private boolean validateAmount(double amount) {
        if (!ValidationUtil.isValidAmount(amount)) {
            UIFormatter.printError(ValidationUtil.formatError("INVALID_AMOUNT"));
            return false;
        }
        return true;
    }

    /**
     * Registra la transacción y muestra mensaje de éxito (depósito/retiro).
     */
    private void recordAndNotifyTransaction(Transaction.TransactionType type, double amount, String operationLabel) {
        Transaction transaction = new Transaction(
                currentAccount,
                type,
                amount,
                currentAccount.getCurrency(),
                currentAccount.getCurrency(),
                amount,
                operationLabel
        );
        transactionService.recordTransaction(transaction);
        UIFormatter.printSuccess(operationLabel + " de " + UIFormatter.formatMoney(amount, currentAccount.getCurrency()) + " realizado");
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    public boolean registerUser(String firstName, String lastName, String email) {
        if (!ValidationUtil.isValidName(firstName) || !ValidationUtil.isValidName(lastName)) {
            UIFormatter.printError(ValidationUtil.formatError("INVALID_NAME"));
            return false;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            UIFormatter.printError(ValidationUtil.formatError("INVALID_EMAIL"));
            return false;
        }

        User user = new User(firstName, lastName, email);
        users.put(user.getId(), user);
        UIFormatter.printSuccess("Usuario registrado: " + user.getFullName());
        return true;
    }

    /**
     * Crea una cuenta para un usuario.
     */
    public boolean createAccountForUser(String userId, Currency currency, double initialBalance) {
        if (!validateAmount(initialBalance)) {
            return false;
        }

        User user = users.get(userId);
        if (user == null) {
            UIFormatter.printError(ValidationUtil.formatError("ACCOUNT_NOT_FOUND"));
            return false;
        }

        // Validar límite de 5 cuentas por usuario
        List<Account> userAccounts = accountService.getAccountsByUser(userId);
        if (userAccounts.size() >= 5) {
            UIFormatter.printError("El usuario ha alcanzado el límite máximo de 5 cuentas.");
            return false;
        }

        try {
            currentAccount = accountService.createAccount(user, currency, initialBalance);
            UIFormatter.printSuccess("Cuenta creada exitosamente en " + currency.getDescription());
            return true;
        } catch (Exception e) {
            UIFormatter.printError(ValidationUtil.formatError("OPERATION_FAILED"));
            return false;
        }
    }

    /**
     * Obtiene las cuentas asociadas a un usuario.
     */
    public List<Account> getAccountsForUser(String userId) {
        return accountService.getAccountsByUser(userId);
    }

    /**
     * Realiza un depósito en la cuenta actual.
     */
    public boolean deposit(double amount) {
        if (!ensureCurrentAccount() || !validateAmount(amount)) {
            return false;
        }

        if (accountService.deposit(currentAccount.getId(), amount)) {
            recordAndNotifyTransaction(Transaction.TransactionType.DEPOSIT, amount, "Depósito");
            return true;
        }

        UIFormatter.printError(ValidationUtil.formatError("OPERATION_FAILED"));
        return false;
    }

    /**
     * Realiza un retiro de la cuenta actual.
     */
    public boolean withdraw(double amount) {
        if (!ensureCurrentAccount() || !validateAmount(amount)) {
            return false;
        }

        if (amount > currentAccount.getBalance()) {
            UIFormatter.printError(ValidationUtil.formatError("INSUFFICIENT_BALANCE"));
            return false;
        }

        if (accountService.withdraw(currentAccount.getId(), amount)) {
            recordAndNotifyTransaction(Transaction.TransactionType.WITHDRAWAL, amount, "Retiro");
            return true;
        }

        UIFormatter.printError(ValidationUtil.formatError("OPERATION_FAILED"));
        return false;
    }

    /**
     * Convierte el saldo de una moneda a otra.
     */
    public double convertBalance(Currency targetCurrency) {
        if (currentAccount == null) {
            UIFormatter.printError(ValidationUtil.formatError("ACCOUNT_NOT_FOUND"));
            return 0;
        }

        double convertedAmount = currencyConverter.convert(
                currentAccount.getBalance(),
                currentAccount.getCurrency(),
                targetCurrency
        );

        // Aplicar conversión sobre la cuenta: actualizar saldo y moneda
        double originalAmount = currentAccount.getBalance();
        Currency originalCurrency = currentAccount.getCurrency();
        currentAccount.applyConversion(targetCurrency, convertedAmount);

        Transaction transaction = new Transaction(
            currentAccount,
            Transaction.TransactionType.CONVERSION,
            originalAmount,
            originalCurrency,
            targetCurrency,
            convertedAmount,
            "Conversión de " + originalCurrency + " a " + targetCurrency
        );
        transactionService.recordTransaction(transaction);

        return convertedAmount;
    }

    /**
     * Obtiene el saldo actual de la cuenta.
     */
    public double getBalance() {
        if (currentAccount == null) {
            return 0;
        }
        return currentAccount.getBalance();
    }

    /**
     * Obtiene la moneda actual de la cuenta.
     */
    public Currency getCurrentCurrency() {
        if (currentAccount == null) {
            return null;
        }
        return currentAccount.getCurrency();
    }

    /**
     * Obtiene el historial de transacciones.
     */
    public List<Transaction> getTransactionHistory() {
        if (currentAccount == null) {
            return Collections.emptyList();
        }
        return transactionService.getTransactionHistory(currentAccount);
    }

    /**
     * Obtiene el total de depósitos de una cuenta.
     */
    public double getTotalDeposits(Account account) {
        return account == null ? 0 : transactionService.getTotalDeposits(account);
    }

    /**
     * Obtiene el total de retiros de una cuenta.
     */
    public double getTotalWithdrawals(Account account) {
        return account == null ? 0 : transactionService.getTotalWithdrawals(account);
    }

    /**
     * Obtiene la tasa de cambio entre dos monedas.
     */
    public double getExchangeRate(Currency from, Currency to) {
        return currencyConverter.getExchangeRate(from, to);
    }

    /**
     * Obtiene la cuenta actual.
     */
    public Account getCurrentAccount() {
        return currentAccount;
    }

    /**
     * Establece la cuenta actual.
     */
    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
    }

    /**
     * Obtiene un usuario por ID.
     */
    public User getUser(String userId) {
        return users.get(userId);
    }

    /**
     * Obtiene todos los usuarios.
     */
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
