package proyectowallet.service;

import proyectowallet.model.Currency;

/**
 * Interfaz para servicios de conversión de monedas.
 * Implementa principio SOLID: Dependency Inversion (depende de abstracción, no de implementación).
 */
public interface CurrencyConverterService {
    /**
     * Convierte una cantidad de una moneda a otra.
     * @param amount monto a convertir
     * @param from moneda origen
     * @param to moneda destino
     * @return cantidad convertida
     */
    double convert(double amount, Currency from, Currency to);

    /**
     * Obtiene la tasa de cambio entre dos monedas.
     * @param from moneda origen
     * @param to moneda destino
     * @return tasa de cambio
     */
    double getExchangeRate(Currency from, Currency to);
}
