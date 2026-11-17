package com.vcet.courseenrollment.util;

import java.util.Scanner;

/**
 * Utility for reading and validating command-line inputs.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    public static Scanner getScanner() {
        return scanner;
    }

    public static String readLine() {
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "";
    }

    public static String readString(String prompt, boolean mandatory) {
        while (true) {
            System.out.print(prompt + ": ");
            if (!scanner.hasNextLine()) {
                return "";
            }
            String input = scanner.nextLine().trim();
            if (mandatory && input.isEmpty()) {
                System.out.println(">> Error: This field cannot be empty. Please re-enter.");
                continue;
            }
            return input;
        }
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + " (" + min + "-" + max + "): ");
            if (!scanner.hasNextLine()) {
                return min;
            }
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val < min || val > max) {
                    System.out.println(">> Error: Value must be between " + min + " and " + max + ".");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println(">> Error: Please enter a valid integer.");
            }
        }
    }

    public static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt + " (" + min + "-" + max + "): ");
            if (!scanner.hasNextLine()) {
                return min;
            }
            String input = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val < min || val > max) {
                    System.out.println(">> Error: Value must be between " + min + " and " + max + ".");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println(">> Error: Please enter a valid decimal number.");
            }
        }
    }

    public static String readChoice(String prompt, String[] options) {
        while (true) {
            System.out.println(prompt);
            for (int i = 0; i < options.length; i++) {
                System.out.println("  [" + (i + 1) + "] " + options[i]);
            }
            System.out.print("Select choice (1-" + options.length + "): ");
            if (!scanner.hasNextLine()) {
                return options[0];
            }
            String input = scanner.nextLine().trim();
            try {
                int idx = Integer.parseInt(input) - 1;
                if (idx >= 0 && idx < options.length) {
                    return options[idx];
                }
            } catch (NumberFormatException ignored) {}
            System.out.println(">> Invalid selection. Please choose a number between 1 and " + options.length + ".");
        }
    }
}
