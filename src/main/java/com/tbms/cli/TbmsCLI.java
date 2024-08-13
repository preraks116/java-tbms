package com.tbms.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import com.tbms.service.BankService;
import com.tbms.service.BestConversion;
import com.tbms.service.ExchangeService;
import com.tbms.model.Bank;

import java.util.*;

@Command(name = "tbms", mixinStandardHelpOptions = true, version = "1.0",
        description = "Manages currency exchanges and finds the best conversion rates.")
public class TbmsCLI implements Runnable {

    private CommandLine commandLine;
    private BankService bankService = new BankService();
    private BestConversion bestConversion;

    public TbmsCLI() {
        System.out.println("Welcome to the TBMS CLI!");
        this.commandLine = new CommandLine(this);
        this.bestConversion = new BestConversion();
    }

    @Command(name = "help", description = "Display help information")
    public void help(@Parameters(paramLabel = "COMMAND", description = "Command to get help for", defaultValue = "") String command) {
        if (command.isEmpty()) {
            commandLine.usage(System.out);
        } else {
            CommandLine subcommand = commandLine.getSubcommands().get(command);
            if (subcommand != null) {
                subcommand.usage(System.out);
            } else {
                System.out.println("Unknown command: " + command);
                System.out.println("Use 'help' without arguments to see all available commands.");
            }
        }
    }

    @Command(name = "add-bank", description = "Add a new bank")
    public void addBank(@Parameters(paramLabel = "BANK_NAME", description = "The name of the bank") String bankName) {
        try {
            bankService.addBank(bankName);
            System.out.println("Added bank: " + bankName);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Command(name = "delete-bank", description = "Delete a bank")
    public void deleteBank(@Parameters(paramLabel = "BANK_NAME", description = "The name of the bank to delete") String bankName) {
        try {
            bankService.deleteBank(bankName);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Command(name = "add-curr", description = "Add a new currency to a bank")
    public void addCurrency(
            @Parameters(paramLabel = "BANK_NAME", description = "The name of the bank") String bankName,
            @Parameters(paramLabel = "CURRENCY", description = "The currency to add") String currency) {
        try {
            Bank bank = bankService.getBank(bankName);
            bank.getExchangeService().addCurrency(currency);

            bestConversion.addCurrency(currency);

            System.out.println("Added currency to " + bankName + ": " + currency);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Command(name = "delete-curr", description = "Delete a currency from a bank")
    public void deleteCurrency(
            @Parameters(paramLabel = "BANK_NAME", description = "The name of the bank") String bankName,
            @Parameters(paramLabel = "CURRENCY", description = "The currency to delete") String currency) {
        try {
            Bank bank = bankService.getBank(bankName);
            bank.getExchangeService().deleteCurrency(currency);

            bestConversion.deleteCurrency(currency);

            System.out.println("Deleted currency from " + bankName + ": " + currency);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Command(name = "add-rate", description = "Add a new exchange rate")
    public void addRate(
            @Parameters(paramLabel = "BANK_NAME", description = "The name of the bank") String bankName,
            @Parameters(paramLabel = "FROM_CURRENCY", description = "The currency to convert from") String fromCurrency,
            @Parameters(paramLabel = "TO_CURRENCY", description = "The currency to convert to") String toCurrency,
            @Parameters(paramLabel = "RATE", description = "The exchange rate") double rate) {
        try {
            Bank bank = bankService.getBank(bankName);
            bank.getExchangeService().addExchangeRate(fromCurrency, toCurrency, rate);

            bestConversion.addRate(fromCurrency, toCurrency, rate, bankName);

            System.out.println("Added exchange rate for " + bankName + ": " + fromCurrency + " to " + toCurrency + " at " + rate);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Command(name = "delete-rate", description = "Delete an exchange rate")
    public void deleteRate(
            @Parameters(paramLabel = "BANK_NAME", description = "The name of the bank") String bankName,
            @Parameters(paramLabel = "FROM_CURRENCY", description = "The currency to convert from") String fromCurrency,
            @Parameters(paramLabel = "TO_CURRENCY", description = "The currency to convert to") String toCurrency) {
        try {
            Bank bank = bankService.getBank(bankName);
            ExchangeService exchangeService = bank.getExchangeService();

            double rate = exchangeService.getExchangeRate(fromCurrency, toCurrency);
            exchangeService.deleteExchangeRate(fromCurrency, toCurrency);
            
            // worst
            bestConversion.deleteRate(rate, fromCurrency, toCurrency, bankService.getBanks());

            System.out.println("Deleted exchange rate for " + bankName + ": " + fromCurrency + " to " + toCurrency);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Command(name = "get-rate", description = "Get the exchange rate between two currencies")
    public void getRate(
            @Parameters(paramLabel = "BANK_NAME", description = "The name of the bank") String bankName,
            @Parameters(paramLabel = "FROM_CURRENCY", description = "The currency to convert from") String fromCurrency,
            @Parameters(paramLabel = "TO_CURRENCY", description = "The currency to convert to") String toCurrency) {
        try {
            Bank bank = bankService.getBank(bankName);
            double rate = bank.getExchangeService().getExchangeRate(fromCurrency, toCurrency);

            System.out.println("Exchange rate for " + bankName + ": " + fromCurrency + " to " + toCurrency + " is " + rate);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Command(name = "show-banks", description = "Show all banks in the system")
    public void showBanks() {
        Set<String> bankNames = bankService.getAllBankNames();
        if (bankNames.isEmpty()) {
            System.out.println("No banks found");
            return;
        }
        for (String bank : bankNames) {
            System.out.println("- " + bank);
        }
    }

    @Command(name = "show-curr", description = "Show currency table for a specified bank")
    public void showCurrencies(
            @Parameters(paramLabel = "BANK_NAME", description = "The name of the bank") String bankName) {
        try {
            Bank bank = bankService.getBank(bankName);
            String currencyTable = bank.getExchangeService().getCurrencyTable();
            System.out.println("Currency exchange rates for " + bankName + ":");
            System.out.println(currencyTable);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Command(name = "get-best-conversion", description = "Get the best conversion path between two currencies across all banks")
    public void getBestRate(
            @Parameters(paramLabel = "FROM_CURRENCY", description = "The currency to convert from") String fromCurrency,
            @Parameters(paramLabel = "TO_CURRENCY", description = "The currency to convert to") String toCurrency) {
        try {
            bestConversion.getBestConversion(fromCurrency, toCurrency);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Please enter a command. Use 'help' for usage information.");
    }

    public int executeCommand(String[] args) {
        return commandLine.execute(args);
    }
}