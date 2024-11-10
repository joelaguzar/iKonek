package ikonek.dao;

import ikonek.models.User;
import ikonek.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Method to add a new user
    public boolean addUser(User user) {
        String query = "INSERT INTO Users (first_name, middle_name, last_name, gender, birth_date, email, password_hash, blood_type, weight, contact_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getMiddleName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getGender());
            pstmt.setDate(5, Date.valueOf(user.getBirthDate()));
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getPasswordHash());
            pstmt.setString(8, user.getBloodType());
            pstmt.setDouble(9, user.getWeight());
            pstmt.setString(10, user.getContactNumber());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        }
    }

    // Method to retrieve a user by their email
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM Users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by email: " + e.getMessage());
        }
        return null;
    }

    // Method to update a user's information
    public boolean updateUser(User user) {
        String query = "UPDATE Users SET first_name = ?, middle_name = ?, last_name = ?, gender = ?, birth_date = ?, password_hash = ?, blood_type = ?, weight = ?, contact_number = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getMiddleName());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getGender());
            pstmt.setDate(5, Date.valueOf(user.getBirthDate()));
            pstmt.setString(6, user.getPasswordHash());
            pstmt.setString(7, user.getBloodType());
            pstmt.setDouble(8, user.getWeight());
            pstmt.setString(9, user.getContactNumber());
            pstmt.setInt(10, user.getUserId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    // Method to delete a user by their ID
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM Users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    // Method to retrieve all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
        }
        return users;
    }

    // Helper method to map ResultSet to User object
    private User mapToUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("contact_number")
        );
        user.setUserId(rs.getInt("user_id"));
        user.setGender(rs.getString("gender"));
        user.setBirthDate(rs.getDate("birth_date").toLocalDate());
        user.setBloodType(rs.getString("blood_type"));
        user.setWeight(rs.getDouble("weight"));
        user.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
        return user;
    }
}
