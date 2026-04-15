package proyectowallet.util;

import proyectowallet.model.Currency;
import proyectowallet.service.CurrencyConverterService;

/**
 * Implementación de servicio de conversión de monedas.
 * Implementa principios SOLID: 
 * - Single Responsibility: solo maneja conversiones
 * - Open/Closed: fácil de extender con nuevas monedas
 * - Liskov Substitution: implementa correctamente la interfaz
 */
public class CurrencyConverterUtil implements CurrencyConverterService {

    @Override
    public double convert(double amount, Currency from, Currency to) {
        if (from == null || to == null || amount < 0) {
            throw new IllegalArgumentException("Parámetros inválidos para conversión");
        }

        if (from == to) {
            return amount;
        }

        // Convertir a USD primero, luego a la moneda destino
        double amountInUSD = amount * from.getExchangeRateToUSD();
        return amountInUSD / to.getExchangeRateToUSD();
    }

    @Override
    public double getExchangeRate(Currency from, Currency to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Monedas no pueden ser nulas");
        }

        if (from == to) {
            return 1.0;
        }

        return (from.getExchangeRateToUSD() / to.getExchangeRateToUSD());
    }
}
