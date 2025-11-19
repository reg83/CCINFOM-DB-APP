package com.countryclub.dao;

import com.countryclub.db.DatabaseConnector;
import com.countryclub.model.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class EventRecords {
    private Connection conn;

    public EventRecords() throws SQLException {
        this.conn = DatabaseConnector.getConnection();
    }

    public List<Event> getAllEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT e.*, v.venue_name, m.first_name, m.last_name, s.staff_name " +
                     "FROM events e " +
                     "LEFT JOIN venue v ON e.venue_id = v.venue_id " +
                     "LEFT JOIN members_list m ON e.member_id = m.member_id " +
                     "LEFT JOIN staff_member s ON e.staff_incharge_id = s.staff_id " +
                     "ORDER BY e.from_date DESC";
                     
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Event ev = new Event(
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
                
                ev.setVenueName(rs.getString("venue_name"));
                ev.setHostName(rs.getString("first_name") + " " + rs.getString("last_name"));
                ev.setStaffName(rs.getString("staff_name"));
                
                events.add(ev);
            }
        }
        return events;
    }

    public boolean addEvent(Event event) throws SQLException {
        String sqlEvent = "INSERT INTO events (event_name, from_date, to_date, from_time, to_time, item_id, member_id, staff_incharge_id, venue_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlEventItems = "INSERT INTO event_items (event_id, item_id, quantity_used) VALUES (?, ?, 1)";

        try {
            conn.setAutoCommit(false);

            int eventId = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEvent, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, event.getEventName());
                pstmt.setDate(2, Date.valueOf(event.getFromDate()));
                pstmt.setDate(3, Date.valueOf(event.getToDate()));
                pstmt.setTime(4, Time.valueOf(event.getFromTime()));
                pstmt.setTime(5, Time.valueOf(event.getToTime()));
                pstmt.setInt(6, event.getItemId());
                pstmt.setInt(7, event.getMemberId());
                pstmt.setInt(8, event.getStaffInchargeId());
                pstmt.setInt(9, event.getVenueId());
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) { conn.rollback(); return false; }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) { eventId = generatedKeys.getInt(1); } 
                    else { conn.rollback(); return false; }
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sqlEventItems)) {
                pstmt.setInt(1, eventId);
                pstmt.setInt(2, event.getItemId());
                pstmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public boolean updateEvent(Event event) throws SQLException {
        String sqlGetOldItem = "SELECT item_id FROM events WHERE event_id = ?";
        String sqlRemoveOldItem = "DELETE FROM event_items WHERE event_id = ? AND item_id = ?";
        String sqlInsertNewItem = "INSERT IGNORE INTO event_items (event_id, item_id, quantity_used) VALUES (?, ?, 1)";
        String sqlUpdate = "UPDATE events SET event_name=?, from_date=?, to_date=?, from_time=?, to_time=?, item_id=?, member_id=?, staff_incharge_id=?, venue_id=? WHERE event_id=?";

        try {
            conn.setAutoCommit(false);

            int oldItemId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlGetOldItem)) {
                pstmt.setInt(1, event.getEventId());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) oldItemId = rs.getInt("item_id");
            }

            if (oldItemId != -1 && oldItemId != event.getItemId()) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlRemoveOldItem)) {
                    pstmt.setInt(1, event.getEventId());
                    pstmt.setInt(2, oldItemId);
                    pstmt.executeUpdate();
                }
                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertNewItem)) {
                    pstmt.setInt(1, event.getEventId());
                    pstmt.setInt(2, event.getItemId());
                    pstmt.executeUpdate();
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                pstmt.setString(1, event.getEventName());
                pstmt.setDate(2, Date.valueOf(event.getFromDate()));
                pstmt.setDate(3, Date.valueOf(event.getToDate()));
                pstmt.setTime(4, Time.valueOf(event.getFromTime()));
                pstmt.setTime(5, Time.valueOf(event.getToTime()));
                pstmt.setInt(6, event.getItemId());
                pstmt.setInt(7, event.getMemberId());
                pstmt.setInt(8, event.getStaffInchargeId());
                pstmt.setInt(9, event.getVenueId());
                pstmt.setInt(10, event.getEventId());
                pstmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public boolean deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM events WHERE event_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, eventId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}