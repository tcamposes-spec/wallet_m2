package proyectowallet.service;

import proyectowallet.model.Account;
import proyectowallet.model.Currency;
import proyectowallet.model.User;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de cuentas.
 * Implementa principios SOLID:
 * - Single Responsibility: solo gestiona operaciones de cuentas
 * - Open/Closed: fácil de extender
 * - Liskov Substitution: sustituye correctamente la interfaz
 */
public class AccountServiceImpl implements AccountService {
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    @Override
    public Account createAccount(User user, Currency currency, double initialBalance) {
        if (user == null || currency == null || initialBalance < 0) {
            throw new IllegalArgumentException("Parámetros inválidos para crear cuenta");
        }

        Account account = new Account(user, currency, initialBalance);
        accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public Account getAccount(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("ID de cuenta inválido");
        }
        return accounts.get(accountId);
    }

    @Override
    public boolean deposit(String accountId, double amount) {
        Account account = getAccount(accountId);
        if (account == null) {
            return false;
        }
        return account.deposit(amount);
    }

    @Override
    public boolean withdraw(String accountId, double amount) {
        Account account = getAccount(accountId);
        if (account == null) {
            return false;
        }
        return account.withdraw(amount);
    }

    @Override
    public double getBalance(String accountId) {
        Account account = getAccount(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Cuenta no encontrada");
        }
        return account.getBalance();
    }

    @Override
    public List<Account> getAccountsByUser(String userId) {
        if (userId == null || userId.isBlank()) {
            return List.of();
        }
        return accounts.values().stream()
                .filter(a -> a.getUser() != null && userId.equals(a.getUser().getId()))
                .collect(Collectors.toList());
    }
}
