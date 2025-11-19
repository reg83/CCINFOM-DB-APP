package com.countryclub.dao;

import com.countryclub.db.DatabaseConnector;
import com.countryclub.model.Staff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class StaffRecords {
    private Connection conn;

    public StaffRecords() throws SQLException {
        this.conn = DatabaseConnector.getConnection();
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff_member";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                staffList.add(new Staff(
                        rs.getInt("staff_id"),
                        rs.getString("contact_no"),
                        rs.getString("email"),
                        rs.getString("staff_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        rs.getString("role_name"),
                        rs.getString("availability_status"),
                        rs.getDouble("salary") 
                ));
            }
        }
        return staffList;
    }

    public boolean addStaff(Staff staff) throws SQLException {
        String sql = "INSERT INTO staff_member (contact_no, email, staff_name, birth_date, role_name, availability_status, salary) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getContactNo());
            pstmt.setString(2, staff.getEmail());
            pstmt.setString(3, staff.getStaffName());
            pstmt.setDate(4, Date.valueOf(staff.getBirthDate()));
            pstmt.setString(5, staff.getRoleName());
            pstmt.setString(6, staff.getAvailabilityStatus());
            pstmt.setDouble(7, staff.getSalary());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateStaff(Staff staff) throws SQLException {
        String sql = "UPDATE staff_member SET contact_no = ?, email = ?, staff_name = ?, birth_date = ?, role_name = ?, availability_status = ?, salary = ? WHERE staff_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getContactNo());
            pstmt.setString(2, staff.getEmail());
            pstmt.setString(3, staff.getStaffName());
            pstmt.setDate(4, Date.valueOf(staff.getBirthDate()));
            pstmt.setString(5, staff.getRoleName());
            pstmt.setString(6, staff.getAvailabilityStatus());
            pstmt.setDouble(7, staff.getSalary());
            pstmt.setInt(8, staff.getStaffId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteStaff(int staffId) throws SQLException {
        String sql = "DELETE FROM staff_member WHERE staff_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffId);
            return pstmt.executeUpdate() > 0;
        }
    }
}