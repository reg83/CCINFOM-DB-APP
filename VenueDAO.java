package com.countryclub.dao;

import com.countryclub.db.DatabaseConnector;
import com.countryclub.model.Venue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VenueDAO {
    private Connection conn;

    public VenueDAO() throws SQLException {
        this.conn = DatabaseConnector.getConnection();
    }

    public boolean addVenue(Venue venue) throws SQLException {
        String sql = "INSERT INTO venue (venue_name, venue_type, capacity, availability_status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, venue.getVenueName());
            pstmt.setString(2, venue.getVenueType());
            pstmt.setInt(3, venue.getCapacity());
            pstmt.setString(4, venue.getAvailabilityStatus());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateVenue(Venue venue) throws SQLException {
        String sql = "UPDATE venue SET venue_name = ?, venue_type = ?, capacity = ?, availability_status = ? WHERE venue_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, venue.getVenueName());
            pstmt.setString(2, venue.getVenueType());
            pstmt.setInt(3, venue.getCapacity());
            pstmt.setString(4, venue.getAvailabilityStatus());
            pstmt.setInt(5, venue.getVenueId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteVenue(int venueId) throws SQLException {
        String sql = "DELETE FROM venue WHERE venue_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, venueId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Venue> getAllVenues() throws SQLException {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT * FROM venue";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                venues.add(new Venue(
                        rs.getInt("venue_id"),
                        rs.getString("venue_name"),
                        rs.getString("venue_type"),
                        rs.getInt("capacity"),
                        rs.getString("availability_status")
                ));
            }
        }
        return venues;
    }
}