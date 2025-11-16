package com.countryclub.dao;

import java.sql.*;
import com.countryclub.db.DatabaseConnector;

/**
 * Handles all financial transactions (Sales, etc.) in the database.
 */
public class TransactionDAO {

    public TransactionDAO() {
        // No need to store connection in a field
    }

    /**
     * Records a new transaction in the database.
     */
    public void addTransaction(int memberId, double amount, String type, Date date) throws SQLException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive.");
        }

        String sql = "INSERT INTO transactions (member_id, amount, transaction_type, transaction_date) VALUES (?, ?, ?, ?)";
        
        // Get a fresh connection for this operation
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
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
        
        // Get a fresh connection for this operation
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
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
