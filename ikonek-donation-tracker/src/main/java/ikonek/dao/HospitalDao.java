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
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error creating hospital: " + e.getMessage());
        }
        return -1;
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
            pstmt.setInt(5, hospital.getHospitalId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error updating hospital: " + e.getMessage());
            return false;
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
        Hospital hospital = new Hospital(
                rs.getString("name"),
                rs.getString("city"),
                rs.getString("province"),
                rs.getString("contact_number")
        );
        hospital.setHospitalId(rs.getInt("hospital_id")); // Set the hospitalId
        return hospital;
    }
}