package com.countryclub.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.countryclub.db.DatabaseConnector; // <-- 1. IMPORT THE CONNECTOR
import com.countryclub.model.Inventory;

public class InventoryRecords {
    private Connection conn;

    public InventoryRecords() throws SQLException {
        // --- 2. THIS IS THE ONLY CHANGE ---
        this.conn = DatabaseConnector.getConnection();
        
        // --- DELETE THE OLD CODE ---
        // String url = "jdbc:mysql://localhost:3306/country_club";
        // String user = "root";
        // String password = "your_password";
        // conn = DriverManager.getConnection(url, user, password);
    }

    // CREATE
    public void addItem(Inventory item) throws SQLException {
        // ... (This code is correct, no changes needed)
        String sql = "INSERT INTO inventory (item_name, item_category, item_quantity, item_unit_price, supplier_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, item.getItemName());
        pstmt.setString(2, item.getItemCategory());
        pstmt.setInt(3, item.getItemQuantity());
        pstmt.setBigDecimal(4, item.getItemUnitPrice());
        pstmt.setInt(5, item.getSupplierId());
        pstmt.executeUpdate();
    }

    // READ
    public Inventory getItem(int id) throws SQLException {
        // ... (This code is correct, no changes needed)
        String sql = "SELECT * FROM inventory WHERE item_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Inventory(
                rs.getInt("item_id"),
                rs.getString("item_name"),
                rs.getString("item_category"),
                rs.getInt("item_quantity"),
                rs.getBigDecimal("item_unit_price"),
                rs.getInt("supplier_id")
            );
        }
        return null;
    }

    // UPDATE
    public void updateQuantity(int id, int newQty) throws SQLException {
        // ... (This code is correct, no changes needed)
        String sql = "UPDATE inventory SET item_quantity = ? WHERE item_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, newQty);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }

    // DELETE
    public void deleteItem(int id) throws SQLException {
        // ... (This code is correct, no changes needed)
        String sql = "DELETE FROM inventory WHERE item_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    // LIST ALL
    public List<Inventory> getAllItems() throws SQLException {
        // ... (This code is correct, no changes needed)
        List<Inventory> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM inventory");
        while (rs.next()) {
            items.add(new Inventory(
                rs.getInt("item_id"),
                rs.getString("item_name"),
                rs.getString("item_category"),
                rs.getInt("item_quantity"),
                rs.getBigDecimal("item_unit_price"),
                rs.getInt("supplier_id")
            ));
        }
        return items;
    }
}