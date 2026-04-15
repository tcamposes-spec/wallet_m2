package proyectowallet.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import proyectowallet.model.Currency;
import proyectowallet.util.CurrencyConverterUtil;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CurrencyConverterService.
 * Valida la conversión entre monedas.
 */
@DisplayName("Pruebas de CurrencyConverterService")
class CurrencyConverterServiceTest {
    private CurrencyConverterService currencyConverter;

    @BeforeEach
    void setup() {
        currencyConverter = new CurrencyConverterUtil();
    }

    @Test
    @DisplayName("Debe convertir correctamente entre monedas")
    void testConvertCurrency() {
        double amount = 100;
        double converted = currencyConverter.convert(amount, Currency.USD, Currency.EUR);

        assertNotEquals(amount, converted);
        assertTrue(converted > 0);
    }

    @Test
    @DisplayName("Debe retornar la misma cantidad al convertir a la misma moneda")
    void testConvertToSameCurrency() {
        double amount = 100;
        double converted = currencyConverter.convert(amount, Currency.USD, Currency.USD);

        assertEquals(amount, converted);
    }

    @Test
    @DisplayName("Debe obtener tasa de cambio correcta")
    void testGetExchangeRate() {
        double rate = currencyConverter.getExchangeRate(Currency.USD, Currency.USD);
        assertEquals(1.0, rate);

        double rateToEUR = currencyConverter.getExchangeRate(Currency.USD, Currency.EUR);
        assertTrue(rateToEUR > 0);
    }

    @Test
    @DisplayName("No debe convertir con monedas nulas")
    void testConvertWithNullCurrencies() {
        assertThrows(IllegalArgumentException.class,
                () -> currencyConverter.convert(100, null, Currency.USD));

        assertThrows(IllegalArgumentException.class,
                () -> currencyConverter.convert(100, Currency.USD, null));
    }

    @Test
    @DisplayName("No debe convertir cantidad negativa")
    void testConvertNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> currencyConverter.convert(-100, Currency.USD, Currency.EUR));
    }
}
