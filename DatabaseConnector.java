package com.countryclub.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnector {

    // Replace USER and PASS with actualy username and password (MySQL)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/country_club";
    private static final String USER = "root"; 
    private static final String PASS = "itsASecret"; 

    private static Connection connectionInstance;

    private DatabaseConnector() {
    }

    public static Connection getConnection() throws SQLException {
        if (connectionInstance == null || connectionInstance.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connectionInstance = DriverManager.getConnection(DB_URL, USER, PASS);
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found!");
                throw new SQLException("JDBC Driver not found", e);
            }
        }
        return connectionInstance;
    }

    public static void closeConnection() {
        try {
            if (connectionInstance != null && !connectionInstance.isClosed()) {
                connectionInstance.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}