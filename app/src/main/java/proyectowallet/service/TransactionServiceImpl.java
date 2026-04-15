package proyectowallet.service;

import proyectowallet.model.Transaction;
import proyectowallet.model.Account;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de gestión de transacciones.
 * Implementa principios SOLID y usa Streams de Java 8 para operaciones funcionales.
 */
public class TransactionServiceImpl implements TransactionService {
    private final List<Transaction> transactionHistory = new ArrayList<>();

    @Override
    public boolean recordTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transacción no puede ser nula");
        }
        return transactionHistory.add(transaction);
    }

    @Override
    public List<Transaction> getTransactionHistory(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Cuenta no puede ser nula");
        }

        return transactionHistory.stream()
                .filter(t -> t.getAccount().getId().equals(account.getId()))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toUnmodifiableList());
    }

    private double getTotalByType(Account account, Transaction.TransactionType type) {
        if (account == null) {
            throw new IllegalArgumentException("Cuenta no puede ser nula");
        }
        return transactionHistory.stream()
                .filter(t -> t.getAccount().getId().equals(account.getId()))
                .filter(t -> t.getType() == type)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double getTotalDeposits(Account account) {
        return getTotalByType(account, Transaction.TransactionType.DEPOSIT);
    }

    @Override
    public double getTotalWithdrawals(Account account) {
        return getTotalByType(account, Transaction.TransactionType.WITHDRAWAL);
    }
}
