package com.countryclub.dao;

import com.countryclub.db.DatabaseConnector;
import com.countryclub.model.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    private Connection conn;

    public TransactionDAO() throws SQLException {
        this.conn = DatabaseConnector.getConnection();
    }

    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (member_id, event_id, transaction_date, amount, description) VALUES (?, ?, ?, ?, ?)";
        
        String combinedDescription = transaction.getTransactionType() + " - " + transaction.getDescription();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (transaction.getMemberId() != null) {
                pstmt.setInt(1, transaction.getMemberId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }

            if (transaction.getEventId() != null) {
                pstmt.setInt(2, transaction.getEventId());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }

            pstmt.setDate(3, transaction.getTransactionDate());
            pstmt.setBigDecimal(4, transaction.getAmount());
            pstmt.setString(5, combinedDescription);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        String combinedDescription = rs.getString("description");
        String transactionType = "Uncategorized";
        String description = combinedDescription;

        if (combinedDescription != null && combinedDescription.contains(" - ")) {
            try {
                int separatorIndex = combinedDescription.indexOf(" - ");
                transactionType = combinedDescription.substring(0, separatorIndex);
                description = combinedDescription.substring(separatorIndex + 3);
            } catch (Exception e) {
                description = combinedDescription;
            }
        }
        
        Integer memberId = rs.getObject("member_id", Integer.class);
        Integer eventId = rs.getObject("event_id", Integer.class);

        Transaction tx = new Transaction(
                rs.getInt("transaction_id"),
                memberId,
                eventId,
                rs.getDate("transaction_date"),
                rs.getBigDecimal("amount"),
                description
        );
        tx.setTransactionType(transactionType);
        return tx;
    }
}