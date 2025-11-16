package com.countryclub.dao;

// Make sure to import your new model classes
import com.countryclub.model.EventReportItem;
import com.countryclub.model.SalesReportItem;
import com.countryclub.model.StaffReportItem;
// Import your new database connector
import com.countryclub.db.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * ReportDAO (Data Access Object)
 * Handles all database queries for generating reports.
 */
public class ReportDAO {

    /**
     * Fetches the Event Report for a given month and year.
     * @return A List of EventReportItem objects.
     */
    public List<EventReportItem> getEventReport(int month, int year) {
        List<EventReportItem> reportItems = new ArrayList<>();
        String sql = "SELECT e.event_id, e.event_name, SUM(i.item_unit_price * ei.quantity_used) AS TotalCost " +
                     "FROM events e " +
                     "JOIN event_items ei ON e.event_id = ei.event_id " +
                     "JOIN inventory i ON ei.item_id = i.item_id " +
                     "WHERE MONTH(e.from_date) = ? AND YEAR(e.from_date) = ? " +
                     "GROUP BY e.event_id, e.event_name;";

        try (Connection conn = DatabaseConnector.getConnection(); // Use the connector
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    EventReportItem item = new EventReportItem(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getDouble("TotalCost")
                    );
                    reportItems.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Event Report: " + e.getMessage());
            e.printStackTrace();
        }
        return reportItems;
    }

    /**
     * Fetches the Sales Report for a given month and year.
     * @return A List of SalesReportItem objects.
     */
    public List<SalesReportItem> getSalesReport(int month, int year) {
        List<SalesReportItem> reportItems = new ArrayList<>();
        String sql = "SELECT DATE(transaction_date) AS SaleDate, SUM(amount) AS TotalSales, AVG(amount) AS AverageSales " +
                     "FROM transactions " +
                     "WHERE MONTH(transaction_date) = ? AND YEAR(transaction_date) = ? " +
                     "GROUP BY DATE(transaction_date) " +
                     "ORDER BY DATE(transaction_date);";

        try (Connection conn = DatabaseConnector.getConnection(); // Use the connector
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SalesReportItem item = new SalesReportItem(
                        rs.getDate("SaleDate"),
                        rs.getDouble("TotalSales"),
                        rs.getDouble("AverageSales")
                    );
                    reportItems.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Sales Report: " + e.getMessage());
            e.printStackTrace();
        }
        return reportItems;
    }

    /**
     * Fetches the new member count for a specific month and year.
     * @return The count of new members, or -1 on error.
     */
    public int getMonthlyMembershipCount(int month, int year) {
        String sql = "SELECT COUNT(member_id) AS NewMemberCount " +
                     "FROM members_list " +
                     "WHERE MONTH(join_date) = ? AND YEAR(join_date) = ?;";
        
        try (Connection conn = DatabaseConnector.getConnection(); // Use the connector
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("NewMemberCount");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Monthly Membership Count: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // Indicate an error
    }
    
    /**
     * Fetches the new member count for a specific year.
     * @return The count of new members, or -1 on error.
     */
    public int getYearlyMembershipCount(int year) {
        String sql = "SELECT COUNT(member_id) AS NewMemberCount " +
                     "FROM members_list " +
                     "WHERE YEAR(join_date) = ?;";
        
        try (Connection conn = DatabaseConnector.getConnection(); // Use the connector
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, year);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("NewMemberCount");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Yearly Membership Count: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // Indicate an error
    }

    /**
     * Fetches the staff schedule report for a given month and year.
     * @return A List of StaffReportItem objects.
     */
    public List<StaffReportItem> getStaffReport(int month, int year) {
        List<StaffReportItem> reportItems = new ArrayList<>();
        String sql = "SELECT s.staff_id, s.staff_name, e.event_name, e.from_date, e.to_date, e.from_time, e.to_time " +
                     "FROM staff_member s " +
                     "LEFT JOIN events e ON s.staff_id = e.staff_incharge_id " +
                     "AND MONTH(e.from_date) = ? AND YEAR(e.from_date) = ? " +
                     "ORDER BY s.staff_id, e.from_date;";

        try (Connection conn = DatabaseConnector.getConnection(); // Use the connector
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StaffReportItem item = new StaffReportItem(
                        rs.getInt("staff_id"),
                        rs.getString("staff_name"),
                        rs.getString("event_name"),
                        rs.getDate("from_date"),
                        rs.getDate("to_date"),
                        rs.getTime("from_time"),
                        rs.getTime("to_time")
                    );
                    reportItems.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching Staff Report: " + e.getMessage());
            e.printStackTrace();
        }
        return reportItems;
    }
}