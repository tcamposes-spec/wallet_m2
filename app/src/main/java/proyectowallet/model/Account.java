package proyectowallet.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa una cuenta bancaria/billetera en la plataforma.
 * Implementa principios SOLID: Single Responsibility (gestiona estado de la cuenta).
 */
public class Account {
    private final String id;
    private final User user;
    private Currency currency;
    private double balance;
    private final LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private boolean active;

    public Account(User user, Currency currency, double initialBalance) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.currency = currency;
        this.balance = initialBalance;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
        this.active = true;
    }

    /**
     * Aplica una conversión sobre la cuenta: actualiza moneda y saldo.
     * @param newCurrency moneda destino
     * @param newBalance monto convertido
     */
    public void applyConversion(Currency newCurrency, double newBalance) {
        if (newCurrency == null || newBalance < 0) {
            throw new IllegalArgumentException("Parámetros inválidos para conversión");
        }
        this.currency = newCurrency;
        this.balance = newBalance;
        this.lastModifiedAt = LocalDateTime.now();
    }

    // Getters
    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Deposita dinero a la cuenta.
     * @param amount monto a depositar (debe ser positivo)
     * @return true si la operación fue exitosa
     */
    public boolean deposit(double amount) {
        if (!active || amount <= 0) {
            return false;
        }
        this.balance += amount;
        this.lastModifiedAt = LocalDateTime.now();
        return true;
    }

    /**
     * Retira dinero de la cuenta.
     * @param amount monto a retirar (debe ser positivo y no exceder el saldo)
     * @return true si la operación fue exitosa
     */
    public boolean withdraw(double amount) {
        if (!active || amount <= 0 || amount > this.balance) {
            return false;
        }
        this.balance -= amount;
        this.lastModifiedAt = LocalDateTime.now();
        return true;
    }

    public void deactivate() {
        this.active = false;
        this.lastModifiedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", user=" + user.getFullName() +
                ", currency=" + currency +
                ", balance=" + balance +
                ", active=" + active +
                '}';
    }
}
