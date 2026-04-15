package proyectowallet.repository;

import proyectowallet.model.Transaction;
import proyectowallet.model.Account;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación en memoria del repositorio de transacciones.
 */
public class TransactionRepositoryImpl implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transacción no puede ser nula");
        }
        transactions.add(transaction);
        return transaction;
    }

    @Override
    public Optional<Transaction> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return transactions.stream()
                .filter(tx -> tx.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    @Override
    public List<Transaction> findByAccount(Account account) {
        if (account == null) {
            return Collections.emptyList();
        }
        return transactions.stream()
                .filter(tx -> tx.getAccount().getId().equals(account.getId()))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return transactions.size();
    }

    @Override
    public long countByAccount(Account account) {
        if (account == null) {
            return 0;
        }
        return transactions.stream()
                .filter(tx -> tx.getAccount().getId().equals(account.getId()))
                .count();
    }
}
