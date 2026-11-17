package com.vcet.courseenrollment.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages database connection to SQLite.
 * Ensures foreign keys are enabled and handles connection lifecycle.
 * 
 * @author Dhanush .K (Reg No: 732925ITR017)
 */
public class DatabaseConnection {
    private static final String DEFAULT_DB_PATH = "database/course_enrollment.db";
    private static String dbPath = DEFAULT_DB_PATH;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Warning: org.sqlite.JDBC driver not found on classpath: " + e.getMessage());
        }
    }

    public static void setDbPath(String path) {
        dbPath = path;
    }

    public static String getDbPath() {
        return dbPath;
    }

    public static Connection getConnection() throws SQLException {
        File dbFile = new File(dbPath);
        File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        String url = "jdbc:sqlite:" + dbPath;
        Connection conn = DriverManager.getConnection(url);
        
        // Enforce Foreign Key constraints in SQLite
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        
        return conn;
    }
}
