package proyectowallet.repository;

import proyectowallet.model.Account;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de cuentas.
 * Utiliza HashMap para almacenamiento rápido.
 * Fácilmente reemplazable por implementación con base de datos.
 */
public class AccountRepositoryImpl implements AccountRepository {
    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public Account save(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Cuenta no puede ser nula");
        }
        accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public Optional<Account> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public List<Account> findByUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            return Collections.emptyList();
        }
        return accounts.values().stream()
                .filter(account -> account.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Account update(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Cuenta no puede ser nula");
        }
        if (!accounts.containsKey(account.getId())) {
            throw new IllegalArgumentException("Cuenta no existe");
        }
        accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public boolean delete(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return accounts.remove(id) != null;
    }

    @Override
    public long count() {
        return accounts.size();
    }
}
