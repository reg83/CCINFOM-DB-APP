package com.countryclub.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.countryclub.db.DatabaseConnector;

/**
 * Handles all financial transactions (Sales, etc.) in the database.
 * Replaces the in-memory logic of the "Transaction" files with real SQL.
 */
public class TransactionDAO {

    private Connection conn;

    public TransactionDAO() throws SQLException {
        this.conn = DatabaseConnector.getConnection();
    }

    /**
     * Records a new transaction in the database.
     * @param memberId The ID of the member making the purchase (can be 0 if guest)
     * @param amount The total amount of the transaction
     * @param type The type of transaction (e.g., "Food", "Beverage", "Merchandise")
     * @param date The date of the transaction (Date object)
     */
    public void addTransaction(int memberId, double amount, String type, Date date) throws SQLException {
        // validation
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }

        String sql = "INSERT INTO transactions (member_id, amount, transaction_type, transaction_date) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (memberId > 0) {
                pstmt.setInt(1, memberId);
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            
            pstmt.setDouble(2, amount);
            pstmt.setString(3, type);
            pstmt.setDate(4, date);
            
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves the total sales for a specific date.
     */
    public double getTotalSalesForDate(Date date) throws SQLException {
        String sql = "SELECT SUM(amount) FROM transactions WHERE DATE(transaction_date) = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, date);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }
}
