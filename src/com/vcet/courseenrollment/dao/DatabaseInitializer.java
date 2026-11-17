package com.vcet.courseenrollment.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility to initialize the SQLite database schema and load seed data.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class DatabaseInitializer {

    public static boolean isDatabaseInitialized() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='students'")) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public static void initializeDatabase() throws SQLException, IOException {
        initializeFromSqlFiles("database/schema.sql", "database/seed_data.sql");
    }

    public static void resetDatabase() throws SQLException, IOException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = OFF;");
            stmt.execute("DROP TABLE IF EXISTS participation_records;");
            stmt.execute("DROP TABLE IF EXISTS enrollments;");
            stmt.execute("DROP TABLE IF EXISTS courses;");
            stmt.execute("DROP TABLE IF EXISTS students;");
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        initializeFromSqlFiles("database/schema.sql", "database/seed_data.sql");
    }

    private static void initializeFromSqlFiles(String schemaPath, String seedPath) throws SQLException, IOException {
        executeSqlScript(schemaPath);
        executeSqlScript(seedPath);
    }

    public static void executeSqlScript(String filePath) throws SQLException, IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("SQL script file not found: " + file.getAbsolutePath());
        }

        StringBuilder sql = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                    continue;
                }
                sql.append(line).append("\n");
            }
        }

        String[] statements = sql.toString().split(";");
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            for (String singleSql : statements) {
                String toExecute = singleSql.trim();
                if (!toExecute.isEmpty()) {
                    stmt.execute(toExecute);
                }
            }
        }
    }
}
