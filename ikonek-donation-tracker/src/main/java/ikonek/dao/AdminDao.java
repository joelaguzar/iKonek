package ikonek.dao;

import ikonek.models.Admin;
import ikonek.utils.DatabaseConnection;

import java.io.IOException;
import java.sql.*;

public class AdminDao {

    private final String INSERT_ADMIN = "INSERT INTO Admins (username, password_hash, email, first_name, middle_name, last_name, contact_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String SELECT_ADMIN_BY_USERNAME = "SELECT * FROM Admins WHERE username = ?";
    private final String UPDATE_ADMIN = "UPDATE Admins SET password_hash = ?, email = ?, first_name = ?, middle_name = ?, last_name = ?, contact_number = ? WHERE admin_id = ?";
    private final String DELETE_ADMIN = "DELETE FROM Admins WHERE admin_id = ?";
    private final String SELECT_ADMIN_BY_ID = "SELECT * FROM Admins WHERE admin_id = ?";

    public int createAdmin(Admin admin) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_ADMIN, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, admin.getPasswordHash());
            pstmt.setString(3, admin.getEmail());
            pstmt.setString(5, admin.getMiddleName());
            pstmt.setString(6, admin.getLastName());
            pstmt.setString(7, admin.getContactNumber());

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();

            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error creating admin: " + e.getMessage());
        }
        return -1;
    }

    public Admin getAdminByUsername(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ADMIN_BY_USERNAME);
             ResultSet rs = pstmt.executeQuery()) {

            pstmt.setString(1, username);

            if (rs.next()) {
                return mapAdminFromResultSet(rs);
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error getting admin by username: " + e.getMessage());
        }
        return null;
    }

    public Admin getAdminById(int adminId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ADMIN_BY_ID);
             ResultSet rs = pstmt.executeQuery()) {

            pstmt.setInt(1, adminId);
            if (rs.next()) {
                return mapAdminFromResultSet(rs);
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error getting admin by ID: " + e.getMessage());
        }
        return null;
    }

    public boolean updateAdmin(Admin admin) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_ADMIN)) {

            pstmt.setString(1, admin.getUsername());
            pstmt.setString(2, admin.getPasswordHash());
            pstmt.setString(3, admin.getEmail());
            pstmt.setString(4, admin.getFirstName());
            pstmt.setString(5, admin.getMiddleName());
            pstmt.setString(6, admin.getLastName());
            pstmt.setString(7, admin.getContactNumber());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error updating admin: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAdmin(int adminId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_ADMIN)) {

            pstmt.setInt(1, adminId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error deleting admin: " + e.getMessage());
            return false;
        }
    }

    private Admin mapAdminFromResultSet(ResultSet rs) throws SQLException {
        Admin admin = new Admin(
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("contact_number"),
                rs.getString("username")
        );
        admin.setAdminId(rs.getInt("admin_id"));
        return admin;
    }
}