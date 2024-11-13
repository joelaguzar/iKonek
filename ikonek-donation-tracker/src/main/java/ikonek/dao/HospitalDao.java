package ikonek.dao;

import ikonek.models.Hospital;
import ikonek.utils.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalDao {

    private static final String INSERT_HOSPITAL = "INSERT INTO Hospitals (name, city, province, contact_number) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_HOSPITALS = "SELECT * FROM Hospitals";
    private static final String SELECT_HOSPITAL_BY_ID = "SELECT * FROM Hospitals WHERE hospital_id = ?";
    private static final String UPDATE_HOSPITAL = "UPDATE Hospitals SET name = ?, city = ?, province = ?, contact_number = ? WHERE hospital_id = ?";
    private static final String DELETE_HOSPITAL = "DELETE FROM Hospitals WHERE hospital_id = ?";

    public int createHospital(Hospital hospital) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_HOSPITAL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, hospital.getName());
            pstmt.setString(2, hospital.getCity());
            pstmt.setString(3, hospital.getProvince());
            pstmt.setString(4, hospital.getContactNumber());

            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated ID
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error creating hospital: " + e.getMessage()); // Or use a logger
        }
        return -1; // Return -1 on failure
    }


    public List<Hospital> getAllHospitals() {
        List<Hospital> hospitals = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_HOSPITALS);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                hospitals.add(mapHospitalFromResultSet(rs));
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error getting all hospitals: " + e.getMessage());
        }
        return hospitals;
    }

    public Hospital getHospitalById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_HOSPITAL_BY_ID)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapHospitalFromResultSet(rs);
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error retrieving hospital by id: " + e.getMessage());
        }
        return null;
    }

    public boolean updateHospital(Hospital hospital) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_HOSPITAL)) {

            pstmt.setString(1, hospital.getName());
            pstmt.setString(2, hospital.getCity());
            pstmt.setString(3, hospital.getProvince());
            pstmt.setString(4, hospital.getContactNumber());
            pstmt.setInt(5, hospital.getHospitalId()); //Where clause parameter

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating hospital: " + e.getMessage());
            return false;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteHospital(int hospitalId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_HOSPITAL)) {

            pstmt.setInt(1, hospitalId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error deleting hospital: " + e.getMessage());
            return false;
        }
    }

    private Hospital mapHospitalFromResultSet(ResultSet rs) throws SQLException {
        return new Hospital(
                rs.getString("name"),
                rs.getString("city"),
                rs.getString("province"),
                rs.getString("contact_number")
        );

    }
}