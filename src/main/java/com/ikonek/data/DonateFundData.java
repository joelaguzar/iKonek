package com.ikonek.data;

import com.ikonek.entities.DonateFund;
import com.ikonek.entities.Fundraising;
import com.ikonek.entities.User;
import com.ikonek.utils.Database;
import com.ikonek.data.FundraisingData.FundraisingNotFoundException;
import com.ikonek.data.UserData.UserNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DonateFundData {

    public static class DonationNotFoundException extends Exception {
        public DonationNotFoundException(String message) {
            super(message);
        }
    }


    private final Database database;
    private final FundraisingData fundraisingData;
    private final UserData userData;

    public DonateFundData(Database database, FundraisingData fundraisingData, UserData userData) {
        this.database = database;
        this.fundraisingData = fundraisingData;
        this.userData = userData;
    }

    public List<DonateFund> getAllDonations() throws SQLException {
        List<DonateFund> donations = new ArrayList<>();
        String sql = "SELECT * FROM Donations";
        try (Connection conn = database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                donations.add(createDonationFromResultSet(rs));
            }
        }
        return donations;
    }

    public DonateFund getDonationById(int donationId) throws SQLException, DonationNotFoundException {
        String sql = "SELECT * FROM Donations WHERE donation_id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, donationId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return createDonationFromResultSet(rs);
                } else {
                    throw new DonationNotFoundException("Donation not found with ID: " + donationId);
                }
            }
        }
    }


    public int insertDonation(DonateFund donation) throws SQLException, FundraisingNotFoundException, UserNotFoundException {
        String sql = "INSERT INTO Donations (fundraising_id, user_id, amount) VALUES (?, ?, ?)";
        try (Connection conn = database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Input validation and error handling
            Fundraising fundraising = fundraisingData.getFundraisingById(donation.getFundraisingId());
            if (fundraising == null) {
                throw new FundraisingNotFoundException("Fundraising not found with ID: " + donation.getFundraisingId());
            }
            User user = userData.getUserById(donation.getUserId());
            if (user == null) {
                throw new UserNotFoundException("User not found with ID: " + donation.getUserId());
            }

            statement.setInt(1, donation.getFundraisingId());
            statement.setInt(2, donation.getUserId());
            statement.setDouble(3, donation.getAmount());

            int rowsAffected = statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    donation.setDonationId(generatedKeys.getInt(1));
                }
            }
            return rowsAffected;
        }
    }

    public void updateDonation(DonateFund donation) throws SQLException, DonationNotFoundException {
        String sql = "UPDATE Donations SET fundraising_id = ?, user_id = ?, amount = ?, donation_date = ? WHERE donation_id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, donation.getFundraisingId());
            statement.setInt(2, donation.getUserId());
            statement.setDouble(3, donation.getAmount());
            statement.setTimestamp(4, Timestamp.valueOf(donation.getDonationDate()));
            statement.setInt(5, donation.getDonationId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new DonationNotFoundException("Donation not found with ID: " + donation.getDonationId());
            }
        }
    }


    public void deleteDonation(int donationId) throws SQLException, DonationNotFoundException {
        String sql = "DELETE FROM Donations WHERE donation_id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, donationId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new DonationNotFoundException("Donation not found with ID: " + donationId);
            }
        }
    }


    private DonateFund createDonationFromResultSet(ResultSet rs) throws SQLException {
        DonateFund donation = new DonateFund();
        donation.setDonationId(rs.getInt("donation_id"));
        donation.setFundraisingId(rs.getInt("fundraising_id"));
        donation.setUserId(rs.getInt("user_id"));
        donation.setAmount(rs.getDouble("amount"));
        donation.setDonationDate(rs.getTimestamp("donation_date").toLocalDateTime());
        return donation;
    }
}