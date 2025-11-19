package com.countryclub.dao;

import com.countryclub.db.DatabaseConnector;
import com.countryclub.model.Inventory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class InventoryRecords {
    private Connection conn;

    public InventoryRecords() throws SQLException {
        this.conn = DatabaseConnector.getConnection();
    }

    public List<Inventory> getAllItems() throws SQLException {
        List<Inventory> items = new ArrayList<>();
        String sql = "SELECT * FROM inventory";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(new Inventory(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getString("item_category"),
                        rs.getInt("item_quantity"),
                        rs.getBigDecimal("item_unit_price"),
                        rs.getInt("supplier_id"),
                        rs.getTime("availability_start").toLocalTime(),
                        rs.getTime("availability_end").toLocalTime()    
                ));
            }
        }
        return items;
    }

    public boolean addItem(Inventory item) throws SQLException {
        String sql = "INSERT INTO inventory (item_name, item_category, item_quantity, item_unit_price, supplier_id, availability_start, availability_end) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getItemCategory());
            pstmt.setInt(3, item.getItemQuantity());
            pstmt.setBigDecimal(4, item.getItemUnitPrice());
            pstmt.setInt(5, item.getSupplierId());
            pstmt.setTime(6, Time.valueOf(item.getAvailabilityStart()));
            pstmt.setTime(7, Time.valueOf(item.getAvailabilityEnd()));
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateItem(Inventory item) throws SQLException {
        String sql = "UPDATE inventory SET item_name = ?, item_category = ?, item_quantity = ?, item_unit_price = ?, supplier_id = ?, availability_start = ?, availability_end = ? WHERE item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getItemCategory());
            pstmt.setInt(3, item.getItemQuantity());
            pstmt.setBigDecimal(4, item.getItemUnitPrice());
            pstmt.setInt(5, item.getSupplierId());
            pstmt.setTime(6, Time.valueOf(item.getAvailabilityStart()));
            pstmt.setTime(7, Time.valueOf(item.getAvailabilityEnd()));
            pstmt.setInt(8, item.getItemId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean reduceStock(int itemId, int quantityUsed) throws SQLException {
        String sql = "UPDATE inventory SET item_quantity = item_quantity - ? WHERE item_id = ? AND item_quantity >= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantityUsed);
            pstmt.setInt(2, itemId);
            pstmt.setInt(3, quantityUsed); 
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteItem(int itemId) throws SQLException {
        String sql = "DELETE FROM inventory WHERE item_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            return pstmt.executeUpdate() > 0;
        }
    }
}