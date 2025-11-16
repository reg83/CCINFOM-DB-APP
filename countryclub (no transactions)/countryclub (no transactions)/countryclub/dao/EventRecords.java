package com.countryclub.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.countryclub.db.DatabaseConnector;
import com.countryclub.model.Event; // <-- This is the correct line
import com.countryclub.model.Inventory;

public class EventRecords {
    private Connection conn;

    public EventRecords() throws SQLException {
        // --- 2. THIS IS THE ONLY CHANGE ---
        this.conn = DatabaseConnector.getConnection();
        
        // --- DELETE THE OLD CODE ---
        // String url = "jdbc:mysql://localhost:3306/country_club";
        // String user = "root";
        // String password = "your_password";
        // conn = DriverManager.getConnection(url, user, password);
    }

    // CREATE
    public void addEvent(Event e) throws SQLException {
        // ... (This code is correct, no changes needed)
        String sql = "INSERT INTO events (event_name, from_date, to_date, from_time, to_time, item_id, member_id, staff_incharge_id, venue_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, e.getEventName());
        pstmt.setDate(2, Date.valueOf(e.getFromDate()));
        pstmt.setDate(3, Date.valueOf(e.getToDate()));
        pstmt.setTime(4, Time.valueOf(e.getFromTime()));
        pstmt.setTime(5, Time.valueOf(e.getToTime()));
        pstmt.setInt(6, e.getItemId());
        pstmt.setInt(7, e.getMemberId());
        pstmt.setInt(8, e.getStaffInchargeId());
        pstmt.setInt(9, e.getVenueId());
        pstmt.executeUpdate();
    }

    // READ
    public Event getEvent(int id) throws SQLException {
        // ... (This code is correct, no changes needed)
        String sql = "SELECT * FROM events WHERE event_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Event(
                rs.getInt("event_id"),
                rs.getString("event_name"),
                rs.getDate("from_date").toLocalDate(),
                rs.getDate("to_date").toLocalDate(),
                rs.getTime("from_time").toLocalTime(),
                rs.getTime("to_time").toLocalTime(),
                rs.getInt("item_id"),
                rs.getInt("member_id"),
                rs.getInt("staff_incharge_id"),
                rs.getInt("venue_id")
            );
        }
        return null;
    }

    // UPDATE
    public void updateEventTime(int id, String newFrom, String newTo) throws SQLException {
        // ... (This code is correct, no changes needed)
        String sql = "UPDATE events SET from_time = ?, to_time = ? WHERE event_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setTime(1, Time.valueOf(newFrom));
        pstmt.setTime(2, Time.valueOf(newTo));
        pstmt.setInt(3, id);
        pstmt.executeUpdate();
    }

    // DELETE
    public void deleteEvent(int id) throws SQLException {
        // ... (This code is correct, no changes needed)
        String sql = "DELETE FROM events WHERE event_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    // LIST ALL
    public List<Event> getAllEvents() throws SQLException {
        // ... (This code is correct, no changes needed)
        List<Event> events = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM events");
        while (rs.next()) {
            events.add(new Event(
                rs.getInt("event_id"),
                rs.getString("event_name"),
                rs.getDate("from_date").toLocalDate(),
                rs.getDate("to_date").toLocalDate(),
                rs.getTime("from_time").toLocalTime(),
                rs.getTime("to_time").toLocalTime(),
                rs.getInt("item_id"),
                rs.getInt("member_id"),
                rs.getInt("staff_incharge_id"),
                rs.getInt("venue_id")
            ));
        }
        return events;
    }
}