package com.countryclub.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.countryclub.db.DatabaseConnector;
import com.countryclub.model.Staff; // <-- You will add this import

public class StaffRecords {
    private Connection conn;

    public StaffRecords() throws SQLException {
        // Get the shared connection
        this.conn = DatabaseConnector.getConnection();
    }

    // CREATE
    public void addStaff(Staff staff) throws SQLException {
        String sql = "INSERT INTO staff_member (contact_no, email, staff_name, birth_date, role_name, availability_status) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, staff.getContactNo());
        pstmt.setString(2, staff.getEmail());
        pstmt.setString(3, staff.getStaffName());
        pstmt.setDate(4, Date.valueOf(staff.getBirthDate()));
        pstmt.setString(5, staff.getRoleName());
        pstmt.setString(6, staff.getAvailabilityStatus());
        pstmt.executeUpdate();
    }

    // READ
    public Staff getStaff(int id) throws SQLException {
        String sql = "SELECT * FROM staff_member WHERE staff_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Staff(
                rs.getInt("staff_id"),
                rs.getString("contact_no"),
                rs.getString("email"),
                rs.getString("staff_name"),
                rs.getDate("birth_date").toLocalDate(),
                rs.getString("role_name"),
                rs.getString("availability_status")
            );
        }
        return null;
    }

    // UPDATE
    public void updateAvailability(int id, String status) throws SQLException {
        String sql = "UPDATE staff_member SET availability_status = ? WHERE staff_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, status);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }

    // DELETE
    public void deleteStaff(int id) throws SQLException {
        String sql = "DELETE FROM staff_member WHERE staff_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    // LIST ALL
    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM staff_member");
        while (rs.next()) {
            staffList.add(new Staff(
                rs.getInt("staff_id"),
                rs.getString("contact_no"),
                rs.getString("email"),
                rs.getString("staff_name"),
                rs.getDate("birth_date").toLocalDate(),
                rs.getString("role_name"),
                rs.getString("availability_status")
            ));
        }
        return staffList;
    }
}