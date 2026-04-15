package proyectowallet;

import proyectowallet.controller.WalletController;
import proyectowallet.service.*;
import proyectowallet.util.*;
import proyectowallet.presentation.WalletMenu;
import java.util.Scanner;

/**
 * Punto de entrada principal de la aplicación Alke Wallet.
 * Configura dependencias, arranca el menú y cierra recursos.
 */
public class App {
    private final WalletController controller;
    private final Scanner scanner;

    public App() {
        // Inyección de dependencias - Principio SOLID: Dependency Injection
        AccountService accountService = new AccountServiceImpl();
        TransactionService transactionService = new TransactionServiceImpl();
        CurrencyConverterService currencyConverter = new CurrencyConverterUtil();

        this.controller = new WalletController(accountService, transactionService, currencyConverter);
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    /**
     * Inicia la aplicación: mensaje de bienvenida, menú y cierre.
     */
    public void start() {
        UIFormatter.printHeader("Bienvenido a Alke Wallet");
        System.out.println("Tu solución segura para gestionar tus activos financieros digitales\n");

        new WalletMenu(controller, scanner).run();

        scanner.close();
        UIFormatter.printSuccess("Gracias por usar Alke Wallet. ¡Hasta pronto!");
    }
}
