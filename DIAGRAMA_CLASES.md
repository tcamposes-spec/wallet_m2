# Diagrama de Clases - Alke Wallet

Diagrama actualizado según la estructura actual del proyecto (arquitectura en capas, capa de presentación con WalletMenu).

---

## Vista por capas

```
╔════════════════════════════════════════════════════════════════════════════╗
║                        CAPA DE PRESENTACIÓN                                ║
╠════════════════════════════════════════════════════════════════════════════╣
║  App                           │  presentation.WalletMenu                  ║
║  - controller: WalletController│  - controller: WalletController           ║
║  - scanner: Scanner            │  - scanner: Scanner                       ║
║  + main(args)                  │  - running: boolean                       ║
║  + start()                     │  + run(): void                            ║
║    → new WalletMenu(...).run() │  - handleUserRegistration()               ║
║    → scanner.close()           │  - handleAccountCreation()                 ║
╚════════════════════════════════│  - handleViewBalance()                    ║
                                 │  - handleDeposit() / handleWithdrawal()   ║
                                 │  - handleCurrencyConversion()             ║
                                 │  - handleTransactionHistory()              ║
                                 │  - selectUserAndAccount()                 ║
                                 │  - pause(), parseMenuChoice(), parseDouble()
                                 ╚════════════════════════════════════════════╝
                                              │
                                              │ usa
                                              ▼
╔════════════════════════════════════════════════════════════════════════════╗
║                        CAPA DE APLICACIÓN                                  ║
╠════════════════════════════════════════════════════════════════════════════╣
║  WalletController                                                          ║
║  - accountService: AccountService                                         ║
║  - transactionService: TransactionService                                  ║
║  - currencyConverter: CurrencyConverterService                             ║
║  - users: Map<String, User>                                                ║
║  - currentAccount: Account                                                 ║
║  + registerUser(...)  + createAccountForUser(...)  + deposit(amount)      ║
║  + withdraw(amount)   + convertBalance(targetCurrency)  + getBalance()     ║
║  + getCurrentCurrency()  + getTransactionHistory()  + setCurrentAccount()   ║
║  + getAccountsForUser()  + getTotalDeposits()  + getTotalWithdrawals()     ║
║  + getExchangeRate()   + getCurrentAccount()  + getUser()  + getAllUsers() ║
╚════════════════════════════════════════════════════════════════════════════╝
         │                    │                          │
         │ depende de        │ depende de               │ depende de
         ▼                    ▼                          ▼
╔════════════════════════════════════════════════════════════════════════════╗
║                        SERVICIOS (interfaces + implementaciones)            ║
╠════════════════════════════════════════════════════════════════════════════╣
║  <<interface>> AccountService     <<interface>> TransactionService         ║
║  + createAccount(...)             + recordTransaction(Transaction)        ║
║  + getAccount(id)                 + getTransactionHistory(Account)        ║
║  + deposit(id, amount)            + getTotalDeposits(Account)              ║
║  + withdraw(id, amount)           + getTotalWithdrawals(Account)           ║
║  + getBalance(id)                                                          ║
║  + getAccountsByUser(userId)       ◄── TransactionServiceImpl              ║
║  ◄── AccountServiceImpl            (List<Transaction> en memoria)           ║
║  (Map<String,Account> en memoria)                                           ║
║                                                                             ║
║  <<interface>> CurrencyConverterService  ◄── CurrencyConverterUtil (util) ║
║  + convert(amount, from, to)       + getExchangeRate(from, to)              ║
╚════════════════════════════════════════════════════════════════════════════╝
```

---

## Modelo de dominio

```
┌────────────────────────┐         ┌────────────────────────────┐
│  User                  │         │  Account                    │
├────────────────────────┤         ├────────────────────────────┤
│ - id: String (UUID)    │◄─── 1   │ - id: String (UUID)        │
│ - firstName: String    │    *    │ - user: User               │
│ - lastName: String    │         │ - currency: Currency        │
│ - email: String        │         │ - balance: double          │
│ - createdAt: LocalDateTime       │ - createdAt, lastModifiedAt│
├────────────────────────┤         │ - active: boolean          │
│ + getFullName(): String│         ├────────────────────────────┤
└────────────────────────┘         │ + deposit(amount): boolean   │
                                  │ + withdraw(amount): boolean │
                                  │ + applyConversion(...): void│
                                  └─────────────┬────────────────┘
                                                │ 1
                                                │ *
                                                ▼
┌────────────────────────────┐     ┌────────────────────────────┐
│  Transaction              │     │  <<enum>> Currency          │
├────────────────────────────┤     ├────────────────────────────┤
│ - id: String              │     │ CLP, USD, EUR              │
│ - account: Account        │     │ - description: String       │
│ - type: TransactionType   │     │ - exchangeRateToUSD: double │
│ - amount: double          │     │ + getSymbol(): String       │
│ - currencyFrom/To: Currency     │ + getExchangeRateToUSD()    │
│ - amountInTargetCurrency  │     └────────────────────────────┘
│ - timestamp: LocalDateTime     │
│ - description: String     │     ┌────────────────────────────┐
├────────────────────────────┤     │  TransactionType (enum)   │
│ (getters)                  │     │ DEPOSIT, WITHDRAWAL,       │
└────────────────────────────┘     │ TRANSFER, CONVERSION       │
                                   └────────────────────────────┘
```

---

## Utilidades y repositorios

```
┌─────────────────────────────────┐   ┌─────────────────────────────────┐
│  ValidationUtil (util)          │   │  UIFormatter (util)             │
├─────────────────────────────────┤   ├─────────────────────────────────┤
│ + isValidEmail(email): boolean  │   │ + formatMoney(amount, currency) │
│ + isValidAmount(amount): boolean│   │ + formatAmount(amount)           │
│ + isValidName(name): boolean    │   │ + green/red/blue/yellow/bold()   │
│ + formatError(key): String      │   │ + printHeader/printSuccess/      │
└─────────────────────────────────┘   │   printError/printWarning()      │
                                      │ + printSeparator/printMenu()     │
┌─────────────────────────────────┐   └─────────────────────────────────┘
│  CurrencyConverterUtil (util)   │
│  implements CurrencyConverterService
├─────────────────────────────────┤   Repositorios (existen, no usados
│ + convert(amount, from, to)     │   aún por los servicios):
│ + getExchangeRate(from, to)     │   AccountRepository, UserRepository,
└─────────────────────────────────┘   TransactionRepository (+ Impl)
```

---

## Flujo de llamadas (resumido)

```
Usuario (consola)
    │
    ▼
WalletMenu.run() → opción 1..8
    │
    ▼
WalletController (registerUser | createAccountForUser | deposit | withdraw | …)
    │
    ├─→ ValidationUtil (validaciones)
    ├─→ AccountService / TransactionService / CurrencyConverterService
    │       │
    │       ▼
    │   AccountServiceImpl (Map) / TransactionServiceImpl (List) / CurrencyConverterUtil
    │       │
    │       ▼
    │   User, Account, Transaction, Currency (modelos)
    │
    └─→ UIFormatter (mensajes de éxito/error desde el controlador)
```

---

## Relación entre archivos principales

| Origen        | Usa / Depende de |
|---------------|-------------------|
| App           | WalletController, WalletMenu, UIFormatter, servicios (para construir el controlador) |
| WalletMenu    | WalletController, UIFormatter, Currency, Transaction, User |
| WalletController | AccountService, TransactionService, CurrencyConverterService, ValidationUtil, UIFormatter, User, Account, Transaction, Currency |
| AccountServiceImpl | Account, User, Currency (almacena Map&lt;String, Account&gt;) |
| TransactionServiceImpl | Transaction, Account (almacena List&lt;Transaction&gt;) |
| CurrencyConverterUtil | Currency |

Repositorios (AccountRepository, UserRepository, TransactionRepository): definidos e implementados; los servicios actuales **no** los invocan (persistencia futura).
