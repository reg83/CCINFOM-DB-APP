package com.countryclub.dao;

import com.countryclub.db.DatabaseConnector;
import com.countryclub.model.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MemberRecords {
    private Connection conn;

    public MemberRecords() throws SQLException {
        this.conn = DatabaseConnector.getConnection();
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members_list";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
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

    public boolean addMember(Member member) throws SQLException {
        String sql = "INSERT INTO members_list (membership_type_id, contact_no, first_name, last_name, birth_date, join_date, expiry_date, email, balance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getMembershipTypeId());
            pstmt.setString(2, member.getContactNo());
            pstmt.setString(3, member.getFirstName());
            pstmt.setString(4, member.getLastName());
            pstmt.setDate(5, Date.valueOf(member.getBirthDate()));
            pstmt.setDate(6, Date.valueOf(member.getJoinDate()));
            pstmt.setDate(7, Date.valueOf(member.getExpiryDate()));
            pstmt.setString(8, member.getEmail());
            pstmt.setDouble(9, member.getBalance());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateMember(Member member) throws SQLException {
        String sql = "UPDATE members_list SET membership_type_id = ?, contact_no = ?, first_name = ?, last_name = ?, birth_date = ?, join_date = ?, expiry_date = ?, email = ? WHERE member_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, member.getMembershipTypeId());
            pstmt.setString(2, member.getContactNo());
            pstmt.setString(3, member.getFirstName());
            pstmt.setString(4, member.getLastName());
            pstmt.setDate(5, Date.valueOf(member.getBirthDate()));
            pstmt.setDate(6, Date.valueOf(member.getJoinDate()));
            pstmt.setDate(7, Date.valueOf(member.getExpiryDate()));
            pstmt.setString(8, member.getEmail());
            pstmt.setInt(9, member.getMemberId());
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean updateBalance(int memberId, double amount) throws SQLException {
        String sql = "UPDATE members_list SET balance = balance + ? WHERE member_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, memberId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean renewMembership(int memberId, int newMembershipTypeId) throws SQLException {
        String sql = "UPDATE members_list SET membership_type_id = ?, expiry_date = DATE_ADD(expiry_date, INTERVAL 1 YEAR) WHERE member_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newMembershipTypeId);
            pstmt.setInt(2, memberId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean deleteMember(int memberId) throws SQLException {
        String sql = "DELETE FROM members_list WHERE member_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            return pstmt.executeUpdate() > 0;
        }
    }
}