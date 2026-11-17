package com.vcet.courseenrollment.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility to print professional ASCII grid tables to console.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class TablePrinter {

    public static void printTable(String title, String[] headers, List<String[]> rows) {
        if (headers == null || headers.length == 0) return;

        int cols = headers.length;
        int[] colWidths = new int[cols];

        for (int i = 0; i < cols; i++) {
            colWidths[i] = headers[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < cols && i < row.length; i++) {
                String val = row[i] == null ? "NULL" : row[i];
                if (val.length() > colWidths[i]) {
                    colWidths[i] = val.length();
                }
            }
        }

        // Padding
        for (int i = 0; i < cols; i++) {
            colWidths[i] += 2;
        }

        // Total width calculation
        int totalWidth = 1;
        for (int w : colWidths) {
            totalWidth += w + 1;
        }

        if (title != null && !title.isEmpty()) {
            System.out.println();
            printSeparator(totalWidth, '=', '=');
            System.out.println("  " + title.toUpperCase());
            printSeparator(totalWidth, '=', '=');
        } else {
            System.out.println();
            printRowSeparator(colWidths, '+', '-');
        }

        // Header Row
        printRow(headers, colWidths);
        printRowSeparator(colWidths, '+', '=');

        // Data Rows
        if (rows.isEmpty()) {
            System.out.println("| " + padRight("No records found.", totalWidth - 4) + " |");
        } else {
            for (String[] row : rows) {
                printRow(row, colWidths);
            }
        }
        printRowSeparator(colWidths, '+', '-');
        System.out.println("Total Rows: " + rows.size() + "\n");
    }

    private static void printRow(String[] values, int[] widths) {
        StringBuilder sb = new StringBuilder("|");
        for (int i = 0; i < widths.length; i++) {
            String val = (i < values.length && values[i] != null) ? values[i] : "";
            sb.append(" ").append(padRight(val, widths[i] - 1)).append("|");
        }
        System.out.println(sb.toString());
    }

    private static void printRowSeparator(int[] widths, char corner, char line) {
        StringBuilder sb = new StringBuilder();
        sb.append(corner);
        for (int w : widths) {
            for (int i = 0; i < w; i++) sb.append(line);
            sb.append(corner);
        }
        System.out.println(sb.toString());
    }

    private static void printSeparator(int width, char corner, char line) {
        StringBuilder sb = new StringBuilder();
        sb.append(corner);
        for (int i = 0; i < width - 2; i++) sb.append(line);
        sb.append(corner);
        System.out.println(sb.toString());
    }

    private static String padRight(String s, int n) {
        if (s.length() >= n) return s;
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < n) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
