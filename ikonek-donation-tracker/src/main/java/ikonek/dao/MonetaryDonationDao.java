package ikonek.dao;

import ikonek.models.MonetaryDonation;
import ikonek.models.MonetaryDonationImpl;
import ikonek.utils.DatabaseConnection;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonetaryDonationDao {
    private final String INSERT_DONATION = "INSERT INTO Donations (donor_id, fundraising_id, donation_amount, donation_date) VALUES (?, ?, ?, ?)";
    private final String SELECT_DONATION_BY_ID = "SELECT * FROM Donations WHERE donation_id = ?";
    private final String UPDATE_DONATION = "UPDATE Donations SET donor_id = ?, fundraising_id = ?, donation_amount = ?, donation_date = ? WHERE donation_id = ?";
    private final String DELETE_DONATION = "DELETE FROM Donations WHERE donation_id = ?";
    private final String SELECT_DONATIONS_BY_USER_ID = "SELECT * FROM Donations WHERE donor_id = ?";
    private final String SELECT_DONATIONS_BY_FUNDRAISING_ID = "SELECT * FROM Donations WHERE fundraising_id = ?";

    public int createDonation(MonetaryDonation donation) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_DONATION, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, donation.getDonorId());
            pstmt.setInt(2, donation.getFundraisingId());
            pstmt.setDouble(3, donation.getDonationAmount());
            pstmt.setTimestamp(4, Timestamp.valueOf(donation.getDonationDate()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error adding donation: " + e.getMessage());
        }
        return -1;
    }

    public MonetaryDonation getDonationById(int donationId) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_DONATION_BY_ID);
             ResultSet rs = pstmt.executeQuery()) {

            pstmt.setInt(1, donationId);

            if (rs.next()) {
                return mapToDonation(rs);
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error retrieving donation by id: " + e.getMessage());
        }
        return null;
    }

    public boolean updateDonation(MonetaryDonation donation) {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_DONATION)) {

            pstmt.setInt(1, donation.getDonorId());
            pstmt.setInt(2, donation.getFundraisingId());
            pstmt.setDouble(3, donation.getDonationAmount());
            pstmt.setTimestamp(4, Timestamp.valueOf(donation.getDonationDate()));
            pstmt.setInt(5, donation.getDonationId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error updating donation: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteDonation(int donationId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_DONATION)) {

            pstmt.setInt(1, donationId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error deleting donation: " + e.getMessage());
            return false;
        }
    }

    public double getTotalFundsRaised() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SUM(donation_amount) AS total_funds_raised FROM Donations")) {

            if (rs.next()) {
                return rs.getDouble("total_funds_raised");
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error getting total funds raised: " + e.getMessage());
        }
        return 0;
    }

    public List<MonetaryDonation> getMonetaryDonationsByUserId(int userId) {
        List<MonetaryDonation> donations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_DONATIONS_BY_USER_ID)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    donations.add(mapToDonation(rs));
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error retrieving monetary donations by user ID: " + e.getMessage());
        }
        return donations;
    }

    public List<MonetaryDonation> getMonetaryDonationsByFundraisingId(int fundraisingId) {
        List<MonetaryDonation> donations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_DONATIONS_BY_FUNDRAISING_ID)) {

            pstmt.setInt(1, fundraisingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    donations.add(mapMonetaryDonationFromResultSet(rs));
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error retrieving monetary donations by fundraising ID: " + e.getMessage());
        }
        return donations;
    }

    private MonetaryDonation mapToDonation(ResultSet rs) throws SQLException {
        MonetaryDonationImpl monetaryDonation = new MonetaryDonationImpl(
                rs.getInt("donor_id"),
                rs.getInt("fundraising_id"),
                rs.getDouble("donation_amount")
        );
        monetaryDonation.setDonationId(rs.getInt("donation_id"));
        return monetaryDonation;
    }

    private MonetaryDonation mapMonetaryDonationFromResultSet(ResultSet rs) throws SQLException{
        MonetaryDonationImpl donation = new MonetaryDonationImpl(
                rs.getInt("donor_id"),
                rs.getInt("fundraising_id"),
                rs.getDouble("donation_amount")
        );
        donation.setDonationId(rs.getInt("donation_id"));
        donation.getDonationDate();
        return donation;

    }
}