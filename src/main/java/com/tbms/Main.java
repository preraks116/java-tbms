package com.tbms;

import com.tbms.cli.TbmsCLI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TbmsCLI tbmsCLI = new TbmsCLI();

        if (args.length > 0) {
            // Command-line mode
            int exitCode = tbmsCLI.executeCommand(args);
            System.exit(exitCode);
        }
        // Interactive mode
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("tbms> ");
            String input = scanner.nextLine().trim();

            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
            try {
                tbmsCLI.executeCommand(input.split("\\s+"));
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Use 'help' for usage information.");
            }
        }
        scanner.close();
    }
}