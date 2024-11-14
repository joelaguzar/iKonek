package ikonek.dao;

import ikonek.models.User;
import ikonek.utils.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final String INSERT_USER = "INSERT INTO Users (first_name, middle_name, last_name, gender, birth_date, email, password_hash, blood_type, weight, contact_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SELECT_USER_BY_EMAIL = "SELECT * FROM Users WHERE email = ?";
    private final String UPDATE_USER = "UPDATE Users SET first_name = ?, middle_name = ?, last_name = ?, gender = ?, birth_date = ?, password_hash = ?, blood_type = ?, weight = ?, contact_number = ? WHERE user_id = ?";
    private final String SELECT_ALL_USERS = "SELECT * FROM Users";
    private final String SELECT_USER_BY_ID = "SELECT * FROM Users WHERE user_id = ?";
    private final String DELETE_USER = "DELETE FROM Users WHERE user_id = ?";


    public User createUser(User user) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

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

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        user.setUserId(userId);
                        return user; // Return the created User object with userId
                    }
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error creating user: " + e.getMessage()); // Or use a logger
            return null;
        }
        return null;
    }

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
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int getTotalRegisteredUsers() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Users")) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error getting total registered users: " + e.getMessage()); // Or use a logger

        }
        return -1; //Or throw an exception

    }

    public User updateUser(User user) {
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

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                return getUserById(user.getUserId()); // Retrieve the updated User object
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapToUser(rs));
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
        }
        return users;
    }

    public User getUserById(int userId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_ID)) {

            pstmt.setInt(1, userId); // Set the userId parameter
            try (ResultSet rs = pstmt.executeQuery()) { // ResultSet within try-with-resources
                if (rs.next()) {
                    return mapToUser(rs);
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage()); // Or use a logger
        }
        return null;
    }

    public boolean deleteUser(int userId) {
        String query = "DELETE FROM Users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("gender"),
                rs.getDate("birth_date").toLocalDate(),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("blood_type"),
                rs.getDouble("weight"),
                rs.getString("contact_number")
        );
        user.setUserId(rs.getInt("user_id"));
        user.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
        return user;
    }
}