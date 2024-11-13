package ikonek.dao;

import ikonek.models.BloodDonation;
import ikonek.models.BloodDonationImpl; // Import the implementation class
import ikonek.utils.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class BloodDonationDao {
    private final String INSERT_BLOOD_DONATION = "INSERT INTO BloodDonations (user_id, hospital_id, donation_date, status, failure_reason) VALUES (?, ?, ?, ?, ?)";
    private final String SELECT_ALL_BLOOD_DONATIONS = "SELECT * FROM BloodDonations";
    private final String SELECT_BLOOD_DONATION_BY_ID = "SELECT * FROM BloodDonations WHERE donation_id = ?";
    private final String SELECT_BLOOD_DONATIONS_BY_USER_ID = "SELECT * FROM BloodDonations WHERE user_id = ?"; // New query
    private final String UPDATE_BLOOD_DONATION = "UPDATE BloodDonations SET user_id = ?, hospital_id = ?, donation_date = ?, status = ?, failure_reason = ? WHERE donation_id = ?";
    private final String DELETE_BLOOD_DONATION = "DELETE FROM BloodDonations WHERE donation_id = ?";
    private final String SELECT_PENDING_DONATIONS_BY_USER_ID = "SELECT * FROM BloodDonations WHERE user_id = ? AND status = 'Pending'";
    private final String UPDATE_BLOOD_DONATION_STATUS = "UPDATE BloodDonations SET status = ? WHERE donation_id = ?;";

    public int createBloodDonation(BloodDonation bloodDonation) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_BLOOD_DONATION, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters, including blood type
            pstmt.setInt(1, bloodDonation.getDonorId());
            pstmt.setInt(2, bloodDonation.getHospitalId());
            pstmt.setTimestamp(3, Timestamp.from(bloodDonation.getDonationDate().atZone(ZoneId.systemDefault()).toInstant()));
            pstmt.setString(4, bloodDonation.getStatus());
            pstmt.setString(5, bloodDonation.getFailureReason());

            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error creating blood donation: " + e.getMessage());
        }
        return -1; // Return -1 on failure
    }

    public List<BloodDonation> getAllBloodDonations() {
        List<BloodDonation> bloodDonations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_BLOOD_DONATIONS);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                bloodDonations.add(mapBloodDonationFromResultSet(rs));
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error getting all blood donations: " + e.getMessage());
        }
        return bloodDonations;
    }

    public BloodDonation getBloodDonationById(int donationId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BLOOD_DONATION_BY_ID)) {

            pstmt.setInt(1, donationId); // Set the donationId parameter HERE

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapBloodDonationFromResultSet(rs);
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error retrieving blood donation by ID: " + e.getMessage()); // Or use a logger
        }
        return null;
    }

    public boolean updateBloodDonation(BloodDonation bloodDonation) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_BLOOD_DONATION)) {

            pstmt.setInt(1, bloodDonation.getDonorId());
            pstmt.setInt(2, bloodDonation.getHospitalId());
            pstmt.setTimestamp(3, Timestamp.valueOf(String.valueOf(bloodDonation.getDonationDate().atZone(ZoneId.systemDefault()).toInstant())));
            pstmt.setString(4, bloodDonation.getStatus());
            pstmt.setString(5, bloodDonation.getFailureReason());
            pstmt.setInt(6, bloodDonation.getDonationId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error updating blood donation: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBloodDonation(int donationId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BLOOD_DONATION)) {

            pstmt.setInt(1, donationId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error deleting blood donation: " + e.getMessage());
            return false;
        }
    }

    private BloodDonation mapBloodDonationFromResultSet(ResultSet rs) throws SQLException {
        BloodDonationImpl bloodDonation = new BloodDonationImpl(
                rs.getInt("user_id"),
                rs.getInt("hospital_id"),
                rs.getDate("donation_date").toLocalDate()
        );
        bloodDonation.setDonationId(rs.getInt("donation_id"));
        bloodDonation.setStatus(rs.getString("status"));
        bloodDonation.setFailureReason(rs.getString("failure_reason"));

        return bloodDonation; // Return BloodDonation (interface)
    }

    public List<BloodDonation> getBloodDonationsByUserId(int userId) {
        List<BloodDonation> donations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BLOOD_DONATIONS_BY_USER_ID)) {

            pstmt.setInt(1, userId); //Set the parameter for user_id
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    donations.add(mapBloodDonationFromResultSet(rs));
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error retrieving blood donations by user ID: " + e.getMessage());
        }
        return donations;
    }

    public List<BloodDonation> getPendingBloodDonationsByUserId(int userId) {
        List<BloodDonation> donations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_PENDING_DONATIONS_BY_USER_ID)) {

            pstmt.setInt(1, userId); // Set userId parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    donations.add(mapBloodDonationFromResultSet(rs));
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error retrieving pending blood donations by user ID: " + e.getMessage()); // Or use a logger
        }
        return donations;
    }

    public boolean cancelBloodDonation(int donationId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_BLOOD_DONATION_STATUS)) {

            pstmt.setString(1, "Cancelled");
            pstmt.setInt(2, donationId);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error cancelling blood donation: " + e.getMessage()); // Or use a logger
            return false;
        }
    }
}
