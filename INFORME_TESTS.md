# Informe de pruebas - Alke Wallet

Resumen de las pruebas unitarias del proyecto: estructura, clases de test y cómo ejecutarlas.

---

## Resumen ejecutivo

| Métrica            | Valor   |
|--------------------|---------|
| **Tests totales**  | 45      |
| **Clases de test** | 9       |
| **Framework**      | JUnit 5 (Jupiter) |

Las pruebas cubren modelos, servicios (cuentas, transacciones, conversión de moneda), utilidades de validación y el punto de entrada de la aplicación.

---

## Estructura de pruebas

```
app/src/test/java/proyectowallet/
├── AppTest.java                     (1 test)   → Inicialización de App
├── model/
│   ├── AccountTest.java             (5 tests)  → Account
│   ├── CurrencyTest.java            (5 tests)  → Currency
│   ├── TransactionTest.java         (5 tests)  → Transaction
│   └── UserTest.java                (4 tests)  → User
├── service/
│   ├── AccountServiceTest.java     (12 tests) → AccountService
│   ├── CurrencyConverterServiceTest.java (5)  → Conversión de monedas
│   └── TransactionServiceTest.java  (5 tests) → TransactionService
└── util/
    └── ValidationUtilTest.java      (3 tests) → ValidationUtil
```

---

## Desglose por clase

### AppTest (1 test)

- **appHasAGreeting**: comprueba que la instancia de `App` no sea nula (inicialización correcta).

### model/AccountTest (5 tests)

- Creación de cuenta con datos válidos.
- Rechazo de parámetros nulos o saldo negativo (IllegalArgumentException).
- Obtención de cuenta por ID.
- Comportamiento ante cuenta inexistente (null).
- Depósito y retiro (saldo actualizado correctamente).

### model/CurrencyTest (5 tests)

- Símbolos de monedas (USD, EUR, CLP).
- Tasas de cambio a USD (p. ej. USD = 1.0).
- Descripción de cada moneda.

### model/TransactionTest (5 tests)

- Creación de transacción (depósito, retiro, conversión).
- Campos: amount, currencyFrom/To, timestamp, type.
- Getters y consistencia de datos.

### model/UserTest (4 tests)

- Creación de usuario (id, nombre, apellido, email).
- getFullName().
- Actualización de datos (setters).

### service/AccountServiceTest (12 tests)

- Crear cuenta correctamente.
- Rechazar parámetros nulos o saldo negativo.
- getAccount por ID; cuenta inexistente (null).
- Depósito: éxito, monto negativo, cuenta inexistente.
- Retiro: éxito, saldo insuficiente, monto negativo, cuenta inactiva.
- getBalance; getAccountsByUser.

### service/CurrencyConverterServiceTest (5 tests)

- Conversión entre monedas (p. ej. USD → EUR).
- Conversión a la misma moneda (monto igual).
- getExchangeRate (USD→USD = 1.0; USD→EUR > 0).
- Rechazo de monedas nulas o cantidad negativa (IllegalArgumentException).

### service/TransactionServiceTest (5 tests)

- recordTransaction (éxito).
- recordTransaction(null) → IllegalArgumentException.
- getTransactionHistory (cuenta, orden).
- getTotalDeposits y getTotalWithdrawals por cuenta.

### util/ValidationUtilTest (3 tests)

- isValidEmail (válidos e inválidos, vacío, null).
- isValidAmount (válidos, negativos, cero).
- isValidName (válido, demasiado corto, vacío, null).

---

## Cómo ejecutar las pruebas

### Opción recomendada: desde el IDE (JUnit)

Usar el botón **Run** (▶) de JUnit que aparece junto a las clases o métodos de test:

1. **Todas las pruebas**: ejecutar desde la raíz de tests (`proyectowallet` o la carpeta `test`) con Run (▶), o usar *Run All Tests* / *Run Tests in Package* según tu IDE.
2. **Una clase**: abrir cualquier `*Test.java` y pulsar Run (▶) junto a la clase para ejecutar solo esa clase.
3. **Un método**: pulsar Run (▶) junto a un método `@Test` para ejecutar solo ese test.

### Opción alternativa: por consola

```bash
# Todas las pruebas
./gradlew test

# Solo una clase de test
./gradlew test --tests "proyectowallet.service.AccountServiceTest"

# Varias clases
./gradlew test --tests "proyectowallet.model.*" --tests "proyectowallet.util.*"

# Con más salida
./gradlew test --info
```

En Windows puede usarse `gradlew.bat` en lugar de `./gradlew`.

---

## Cobertura aproximada

| Capa / Paquete | Clases probadas | Estado |
|----------------|-----------------|--------|
| Punto de entrada | App | 1 test básico |
| model | User, Account, Transaction, Currency | 19 tests |
| service | AccountService, TransactionService, CurrencyConverterService | 22 tests |
| util | ValidationUtil | 3 tests |
| controller | WalletController | Sin tests unitarios dedicados |
| presentation | WalletMenu | Sin tests unitarios dedicados |

Los escenarios de negocio (depósito, retiro, conversión, validaciones, historial) se cubren a través de los servicios y modelos.

---

## Conclusión

Las **45 pruebas** distribuidas en **9 clases** validan modelos, servicios y utilidades de forma aislada. Para comprobar el estado actual:

- **En el IDE**: usar el Run (▶) de JUnit sobre la carpeta de tests o sobre una clase `*Test.java`.
- **Por consola**: `./gradlew test`. El reporte HTML se genera en `app/build/reports/tests/test/index.html`.

---

**Proyecto:** Alke Wallet  
**Informe:** Informe de pruebas unitarias  
**Fecha:** 2026
