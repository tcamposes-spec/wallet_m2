package proyectowallet.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa una transacción en la billetera.
 * Implementa principios SOLID: Single Responsibility (gestiona registro de transacciones).
 */
public class Transaction {
    public enum TransactionType {
        DEPOSIT("Depósito"),
        WITHDRAWAL("Retiro"),
        TRANSFER("Transferencia"),
        CONVERSION("Conversión");

        private final String description;

        TransactionType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final String id;
    private final Account account;
    private final TransactionType type;
    private final double amount;
    private final Currency currencyFrom;
    private final Currency currencyTo;
    private final double amountInTargetCurrency;
    private final LocalDateTime timestamp;
    private final String description;

    public Transaction(Account account, TransactionType type, double amount,
                      Currency from, Currency to, double amountInTarget, String description) {
        this.id = UUID.randomUUID().toString();
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.currencyFrom = from;
        this.currencyTo = to;
        this.amountInTargetCurrency = amountInTarget;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public double getAmountInTargetCurrency() {
        return amountInTargetCurrency;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", type=" + type.getDescription() +
                ", amount=" + amount + " " + currencyFrom +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                '}';
    }
}
