# Documentación - Alke Wallet

Aplicación de billetera digital desarrollada en Java, estructurada bajo una arquitectura en capas. Permite registrar usuarios, crear cuentas en varias monedas, depositar, retirar, convertir monedas y visualizar historial de transacciones.
Este proyecto fue desarrollado como parte del módulo 2, aplicando conceptos de programación orientada a objetos y buenas prácticas de diseño.

---

## Índice

1. [Descripción y características](#descripción-y-características)
2. [Arquitectura en capas](#arquitectura-en-capas)
3. [Estructura del proyecto](#estructura-del-proyecto)
4. [Componentes](#componentes)
5. [Instalación y ejecución](#instalación-y-ejecución)
6. [Uso de la aplicación](#uso-de-la-aplicación)
7. [Principios SOLID](#principios-solid)
8. [Persistencia y repositorios](#persistencia-y-repositorios)
9. [Pruebas y documentación relacionada](#pruebas-y-documentación-relacionada)
10. [Mejoras futuras y créditos](#mejoras-futuras-y-créditos)

---

## Descripción y características

- **Registro de usuarios**: nombre, apellido, email (validado).
- **Cuentas**: hasta 5 por usuario, en CLP, USD o EUR, con saldo inicial.
- **Operaciones**: depósito, retiro (con validación de saldo), conversión de moneda.
- **Historial**: transacciones por cuenta (depósito, retiro, conversión).
- **Interfaz**: CLI por consola con colores y mensajes claros (éxito, error, advertencia).
- **Validación**: montos, email, nombres; mensajes de error unificados.

Monedas soportadas: **CLP** (Peso Chileno), **USD** (Dólar), **EUR** (Euro).

---

## Arquitectura en capas

El sistema está organizado mediante una arquitectura en capas** (no solo MVC):

```
┌─────────────────────────────────────────────────────┐
│  PRESENTACIÓN (presentation/)                        │
│  WalletMenu: menú, entrada/salida por consola        │
└──────────────────────┬──────────────────────────────┘
                        │ usa
┌───────────────────────▼──────────────────────────────┐
│  APLICACIÓN (controller/)                             │
│  WalletController: orquesta casos de uso, valida,   │
│  delega en servicios; mantiene cuenta actual y users │
└──────────────────────┬──────────────────────────────┘
                        │ usa
┌───────────────────────▼──────────────────────────────┐
│  DOMINIO / NEGOCIO                                    │
│  model/, service/ (interfaces + impl), exception/    │
│  Lógica de cuentas, transacciones, conversión        │
└──────────────────────┬──────────────────────────────┘
                        │
┌───────────────────────▼──────────────────────────────┐
│  INFRAESTRUCTURA (repository/, util/)                 │
│  Repositorios (persistencia), utilidades, formateo   │
└─────────────────────────────────────────────────────┘
```

- **Presentación**: solo interacción con el usuario (Scanner, System.out); delega en el controlador.
- **Aplicación**: WalletController orquesta y valida; no pinta pantalla.
- **Dominio**: modelos (User, Account, Transaction, Currency), servicios (Account, Transaction, CurrencyConverter), excepciones de negocio.
- **Infraestructura**: repositorios (interfaces e implementaciones) y util (ValidationUtil, UIFormatter, CurrencyConverterUtil).

**Flujo**: `WalletMenu` → `WalletController` → Servicios → Modelos. Los servicios guardan datos en memoria (Map/List); los repositorios existen pero aún no están conectados a los servicios (reservados para futura persistencia en BD).

---

## Estructura del proyecto

```
ProyectoWallet/
├── app/src/main/java/proyectowallet/
│   ├── App.java                    # Punto de entrada; crea servicios, controlador y WalletMenu
│   ├── presentation/
│   │   ├── package-info.java
│   │   └── WalletMenu.java         # Menú e interacción por consola
│   ├── controller/
│   │   └── WalletController.java   # Casos de uso y coordinación
│   ├── model/
│   │   ├── User.java
│   │   ├── Account.java
│   │   ├── Transaction.java
│   │   └── Currency.java
│   ├── service/
│   │   ├── AccountService.java / AccountServiceImpl.java
│   │   ├── TransactionService.java / TransactionServiceImpl.java
│   │   └── CurrencyConverterService.java
│   ├── repository/                 # Interfaces e impl (no usados aún por servicios)
│   │   ├── AccountRepository.java / AccountRepositoryImpl.java
│   │   ├── UserRepository.java / UserRepositoryImpl.java
│   │   └── TransactionRepository.java / TransactionRepositoryImpl.java
│   ├── exception/
│   │   ├── AccountNotFoundException.java
│   │   ├── InsufficientBalanceException.java
│   │   ├── InvalidOperationException.java
│   │   └── ValidationException.java
│   └── util/
│       ├── UIFormatter.java        # Formateo y colores consola
│       ├── ValidationUtil.java
│       └── CurrencyConverterUtil.java  # Implementa CurrencyConverterService
├── app/src/test/java/proyectowallet/
│   ├── AppTest.java
│   ├── model/    (AccountTest, CurrencyTest, TransactionTest, UserTest)
│   ├── service/  (AccountServiceTest, CurrencyConverterServiceTest, TransactionServiceTest)
│   └── util/     (ValidationUtilTest)
├── DOCUMENTACION.md    # Este archivo
├── DIAGRAMA_CLASES.md  # Diagrama de clases actualizado
├── INFORME_TESTS.md    # Informe de pruebas
└── README.md           # Entrada rápida al proyecto
```

---

## Componentes

### Presentación (`presentation/`)

- **WalletMenu**: menú principal, opciones 1–8, métodos `handle*` para cada acción. Pide datos por consola, llama al `WalletController` y muestra resultados con `UIFormatter`. Helper `selectUserAndAccount()` para no repetir flujos.

### Aplicación (`controller/`)

- **WalletController**: inyección de `AccountService`, `TransactionService`, `CurrencyConverterService`. Mantiene `users` (Map) y `currentAccount`. Métodos públicos: `registerUser`, `createAccountForUser`, `getAccountsForUser`, `deposit`, `withdraw`, `convertBalance`, `getBalance`, `getCurrentCurrency`, `getTransactionHistory`, `getTotalDeposits`, `getTotalWithdrawals`, `getExchangeRate`, `setCurrentAccount`, `getCurrentAccount`, `getUser`, `getAllUsers`. Helpers privados: `ensureCurrentAccount`, `validateAmount`, `recordAndNotifyTransaction`.

### Modelo (`model/`)

- **User**: id (UUID), firstName, lastName, email, createdAt; getFullName().
- **Account**: id, user, currency, balance, fechas, active; deposit(), withdraw(), applyConversion().
- **Transaction**: id, account, type (DEPOSIT, WITHDRAWAL, TRANSFER, CONVERSION), amount, currencyFrom/To, amountInTargetCurrency, timestamp, description.
- **Currency** (enum): CLP, USD, EUR; description, exchangeRateToUSD, getSymbol().

### Servicios (`service/`)

- **AccountService**: createAccount, getAccount, deposit, withdraw, getBalance, getAccountsByUser. Implementación guarda cuentas en un `Map` en memoria.
- **TransactionService**: recordTransaction, getTransactionHistory, getTotalDeposits, getTotalWithdrawals. Implementación usa una `List` en memoria.
- **CurrencyConverterService**: convert(amount, from, to), getExchangeRate(from, to). Implementado por CurrencyConverterUtil (en `util/`).

### Repositorios (`repository/`)

Interfaces e implementaciones para Account, User y Transaction. Pensados para futura persistencia (p. ej. BD). Hoy los servicios **no** los usan; almacenan en memoria en sus propias estructuras.

### Excepciones (`exception/`)

- AccountNotFoundException, InsufficientBalanceException, InvalidOperationException, ValidationException. Excepciones de dominio para errores claros.

### Utilidades (`util/`)

- **ValidationUtil**: isValidEmail, isValidAmount, isValidName, formatError (mensajes por clave).
- **UIFormatter**: formatMoney, formatAmount; green/red/blue/yellow/bold (ANSI); printHeader, printSuccess, printError, printWarning, printSeparator, printMenu. Pensado para consola.
- **CurrencyConverterUtil**: implementa CurrencyConverterService (conversión y tasas).

---

## Instalación y ejecución

**Requisitos**: Java 21+, Gradle (wrapper incluido).

```bash
# Compilar
./gradlew build

# Ejecutar aplicación
./gradlew run

# Ejecutar pruebas por consola (opción alternativa)
./gradlew test
```

**Pruebas:** se recomienda ejecutarlas desde el IDE con el Run (▶) de JUnit sobre las clases de test o la carpeta de tests. Por consola: `./gradlew test`. Más detalle en [INFORME_TESTS.md](INFORME_TESTS.md).

En Windows: `gradlew.bat` en lugar de `./gradlew` si hace falta.

---

## Uso de la aplicación

1. **Registrar usuario**: opción 1 → nombre, apellido, email.
2. **Crear cuenta**: opción 2 → elegir usuario → moneda (CLP/USD/EUR) → saldo inicial (máx. 5 cuentas por usuario).
3. **Ver saldo**: opción 3 → usuario (o "A" para todas las cuentas) → ver saldo y totales depósitos/retiros.
4. **Depósito / Retiro**: opciones 4 y 5 → usuario → cuenta (si hay varias) → monto.
5. **Convertir moneda**: opción 6 → usuario → cuenta → moneda destino.
6. **Historial**: opción 7 → usuario → cuenta → listado de transacciones.
7. **Salir**: opción 8.

Los mensajes de error (cuenta no encontrada, monto inválido, saldo insuficiente, etc.) se muestran en rojo; las advertencias (opción no válida) en amarillo; los éxitos en verde; los encabezados en azul.

---

## Principios SOLID

- **S (Single Responsibility)**: Cada clase una responsabilidad (menú, controlador, servicio, modelo, util).
- **O (Open/Closed)**: Interfaces de servicios y repositorios permiten nuevas implementaciones sin cambiar consumidores.
- **L (Liskov Substitution)**: Las implementaciones (*ServiceImpl, CurrencyConverterUtil) sustituyen correctamente sus interfaces.
- **I (Interface Segregation)**: AccountService, TransactionService y CurrencyConverterService son interfaces acotadas.
- **D (Dependency Inversion)**: WalletController y App dependen de interfaces (servicios), no de clases concretas; inyección por constructor.

---

## Persistencia y repositorios

En la versión actual, **AccountServiceImpl** y **TransactionServiceImpl** guardan datos **en memoria** (Map y List). Los **repositorios** (AccountRepository, UserRepository, TransactionRepository) están definidos e implementados pero **no son usados** por los servicios. Están preparados para que, en el futuro, los servicios deleguen en ellos y la persistencia pase a base de datos u otro almacén sin cambiar la lógica de negocio.

---

## Pruebas y documentación relacionada

- **Pruebas**: ver [INFORME_TESTS.md](INFORME_TESTS.md) para resumen, clases de test y cómo ejecutarlas (recomendado: Run (▶) de JUnit en el IDE; alternativa: `./gradlew test`).
- **Diagrama de clases**: ver [DIAGRAMA_CLASES.md](DIAGRAMA_CLASES.md) para la vista actualizada de paquetes y relaciones.

---

## Mejoras futuras y créditos

**Posibles mejoras**: Conectar servicios con repositorios y persistir en BD; API REST; transferencias entre cuentas; autenticación; interfaz gráfica o web.

**Stack**: Java 21, Gradle, JUnit 5. Proyecto educativo (Alkemy Digital, Módulo 2 - Programación en Java).  
**Versión**: 1.0. Fecha: 2026.
