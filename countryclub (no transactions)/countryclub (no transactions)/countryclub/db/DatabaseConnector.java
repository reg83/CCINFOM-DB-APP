package com.countryclub.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages a single, shared database connection.
 * All DAO classes should use this to get their connection.
 */
public class DatabaseConnector {

    // --- IMPORTANT: Update these values ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/country_club";
    private static final String USER = "root";
    private static final String PASS = "INF3in23OM!!SQL";

    private static Connection connectionInstance;

    /**
     * Private constructor to prevent this class from being instantiated.
     */
    private DatabaseConnector() { }

    /**
     * Gets the single, shared database connection.
     * If the connection is null or closed, it creates a new one.
     *
     * @return A valid Connection object.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        if (connectionInstance == null || connectionInstance.isClosed()) {
            connectionInstance = DriverManager.getConnection(DB_URL, USER, PASS);
        }
        return connectionInstance;
    }

    /**
     * Closes the shared connection.
     * Call this when your application is shutting down.
     */
    public static void closeConnection() throws SQLException {
        if (connectionInstance != null && !connectionInstance.isClosed()) {
            connectionInstance.close();
        }
    }
}