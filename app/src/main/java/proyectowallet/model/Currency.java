package proyectowallet.model;

/**
 * Enumeración de monedas soportadas por Alke Wallet.
 * Implementa el patrón Strategy para facilitar la extensión de nuevas monedas.
 */
public enum Currency {
    CLP("Peso Chileno", 0.0012),
    USD("Dólar Estadounidense", 1.0),
    EUR("Euro", 0.92);

    private final String description;
    private final double exchangeRateToUSD;

    Currency(String description, double exchangeRateToUSD) {
        this.description = description;
        this.exchangeRateToUSD = exchangeRateToUSD;
    }

    public String getDescription() {
        return description;
    }

    public double getExchangeRateToUSD() {
        return exchangeRateToUSD;
    }

    /**
     * Obtiene el símbolo de la moneda.
     * @return símbolo de la moneda
     */
    public String getSymbol() {
        return switch (this) {
            case CLP -> "CLP$";
            case USD -> "USD$";
            case EUR -> "EUR$";
        };
    }
}
