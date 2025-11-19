package com.countryclub.dao;

import com.countryclub.db.DatabaseConnector;
import com.countryclub.model.EventReportItem;
import com.countryclub.model.Member;
import com.countryclub.model.SalesReportItem;
import com.countryclub.model.StaffReportItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private Connection conn;

    public ReportDAO() throws SQLException {
        this.conn = DatabaseConnector.getConnection();
    }

    public List<EventReportItem> getEventReport(int month, int year) throws SQLException {
        List<EventReportItem> items = new ArrayList<>();
        String sql = "SELECT e.event_id, e.event_name, " +
                     "(SELECT COALESCE(SUM(i.item_unit_price * ei.quantity_used), 0) " +
                     " FROM event_items ei JOIN inventory i ON ei.item_id = i.item_id " +
                     " WHERE ei.event_id = e.event_id) AS TotalCost, " +
                     "(SELECT COALESCE(SUM(t.amount), 0) " +
                     " FROM transactions t " +
                     " WHERE t.event_id = e.event_id) AS TotalIncome " +
                     "FROM events e " +
                     "WHERE MONTH(e.from_date) = ? AND YEAR(e.from_date) = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(new EventReportItem(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getDouble("TotalCost"),
                        rs.getDouble("TotalIncome")
                ));
            }
        }
        return items;
    }

    public List<Member> getMembershipReport(int month, int year) throws SQLException {
        List<Member> members = new ArrayList<>();
        
        String dateStr = year + "-" + month + "-01";

        String sql = "SELECT * FROM members_list " +
                     "WHERE join_date <= LAST_DAY(?) " +
                     "AND expiry_date >= ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dateStr); 
            pstmt.setString(2, dateStr); 
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("member_id"),
                        rs.getInt("membership_type_id"),
                        rs.getString("contact_no"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        rs.getDate("join_date").toLocalDate(),
                        rs.getDate("expiry_date").toLocalDate(),
                        rs.getString("email"),
                        rs.getDouble("balance")
                ));
            }
        }
        return members;
    }

    public List<StaffReportItem> getStaffReport(int month, int year) throws SQLException {
        List<StaffReportItem> items = new ArrayList<>();
        String sql = "SELECT s.staff_id, s.staff_name, e.event_name, e.from_date, e.to_date, e.from_time, e.to_time " +
                     "FROM staff_member s " +
                     "JOIN events e ON s.staff_id = e.staff_incharge_id " +
                     "WHERE MONTH(e.from_date) = ? AND YEAR(e.from_date) = ? " +
                     "ORDER BY e.from_date, e.from_time";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(new StaffReportItem(
                        rs.getInt("staff_id"),
                        rs.getString("staff_name"),
                        rs.getString("event_name"),
                        rs.getDate("from_date"),
                        rs.getDate("to_date"),
                        rs.getTime("from_time"),
                        rs.getTime("to_time")
                ));
            }
        }
        return items;
    }

    public List<SalesReportItem> getSalesReport(int month, int year) throws SQLException {
        List<SalesReportItem> items = new ArrayList<>();
        String sql = "SELECT transaction_date, SUM(amount) as TotalSales, AVG(amount) as AvgSales " +
                     "FROM transactions " +
                     "WHERE MONTH(transaction_date) = ? AND YEAR(transaction_date) = ? " +
                     "GROUP BY transaction_date " +
                     "ORDER BY transaction_date DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                items.add(new SalesReportItem(
                        rs.getDate("transaction_date"),
                        rs.getDouble("TotalSales"),
                        rs.getDouble("AvgSales")
                ));
            }
        }
        return items;
    }
    
    public int getMonthlyMembershipCount(int month, int year) throws SQLException {
        String sql = "SELECT COUNT(*) FROM members_list WHERE MONTH(join_date) = ? AND YEAR(join_date) = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}