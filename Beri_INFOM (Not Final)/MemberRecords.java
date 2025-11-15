import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRecords {
    private Connection conn;

    // Constructor: establish connection
    public MemberRecords() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/country_club";
        String user = "root"; // replace with your DB user
        String password = "your_password"; // replace with your DB password
        conn = DriverManager.getConnection(url, user, password);
    }

    // CREATE
    public void addMember(Member m) throws SQLException {
        String sql = "INSERT INTO members_list (membership_type_id, contact_no, first_name, last_name, birth_date, join_date, expiry_date, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, m.getMembershipTypeId());
        pstmt.setString(2, m.getContactNo());
        pstmt.setString(3, m.getFirstName());
        pstmt.setString(4, m.getLastName());
        pstmt.setDate(5, Date.valueOf(m.getBirthDate()));
        pstmt.setDate(6, Date.valueOf(m.getJoinDate()));
        pstmt.setDate(7, Date.valueOf(m.getExpiryDate()));
        pstmt.setString(8, m.getEmail());
        pstmt.executeUpdate();
    }

    // READ
    public Member getMember(int id) throws SQLException {
        String sql = "SELECT * FROM members_list WHERE member_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Member(
                rs.getInt("member_id"),
                rs.getInt("membership_type_id"),
                rs.getString("contact_no"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("birth_date").toLocalDate(),
                rs.getDate("join_date").toLocalDate(),
                rs.getDate("expiry_date").toLocalDate(),
                rs.getString("email")
            );
        }
        return null;
    }

    // UPDATE
    public void updateMemberEmail(int id, String newEmail) throws SQLException {
        String sql = "UPDATE members_list SET email = ? WHERE member_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newEmail);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }

    // DELETE
    public void deleteMember(int id) throws SQLException {
        String sql = "DELETE FROM members_list WHERE member_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    // LIST ALL
    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members_list";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
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
                rs.getString("email")
            ));
        }
        return members;
    }
}
