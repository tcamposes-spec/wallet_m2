package proyectowallet.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el Enum Currency.
 * Valida símbolos, tasas de cambio y descripciones.
 */
@DisplayName("Pruebas de Currency")
class CurrencyTest {

    @Test
    @DisplayName("Debe obtener símbolo de moneda correctamente")
    void testCurrencySymbol() {
        assertEquals("USD$", Currency.USD.getSymbol());
        assertEquals("EUR$", Currency.EUR.getSymbol());
        assertEquals("CLP$", Currency.CLP.getSymbol());
    }

    @Test
    @DisplayName("Debe obtener tasa de cambio correctamente")
    void testExchangeRate() {
        assertEquals(1.0, Currency.USD.getExchangeRateToUSD());
        assertTrue(Currency.EUR.getExchangeRateToUSD() > 0);
        assertTrue(Currency.CLP.getExchangeRateToUSD() > 0);
    }

    @Test
    @DisplayName("Debe obtener descripción de moneda")
    void testCurrencyDescription() {
        assertNotNull(Currency.USD.getDescription());
        assertNotNull(Currency.EUR.getDescription());
        assertNotNull(Currency.CLP.getDescription());
        assertFalse(Currency.USD.getDescription().isEmpty());
    }

    @Test
    @DisplayName("Debe retornar todos los valores de Currency")
    void testCurrencyValues() {
        Currency[] currencies = Currency.values();

        assertEquals(3, currencies.length);
        assertTrue(contains(currencies, Currency.USD));
        assertTrue(contains(currencies, Currency.EUR));
        assertTrue(contains(currencies, Currency.CLP));
    }

    private boolean contains(Currency[] currencies, Currency target) {
        for (Currency currency : currencies) {
            if (currency == target) {
                return true;
            }
        }
        return false;
    }

    @Test
    @DisplayName("Debe convertir nombre a Currency")
    void testCurrencyValueOf() {
        Currency usd = Currency.valueOf("USD");
        Currency eur = Currency.valueOf("EUR");
        Currency clp = Currency.valueOf("CLP");

        assertEquals(Currency.USD, usd);
        assertEquals(Currency.EUR, eur);
        assertEquals(Currency.CLP, clp);
    }
}
