package ikonek.dao;

import ikonek.models.FundraisingInitiative;
import ikonek.utils.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FundraisingInitiativeDao {
    private final String INSERT_FUNDRAISING = "INSERT INTO FundraisingInitiatives (user_id, cause, target_amount, amount_received, short_description, deadline) VALUES (?, ?, ?, ?, ?, ?)";
    private final String SELECT_ALL_FUNDRAISING = "SELECT * FROM FundraisingInitiatives";
    private final String SELECT_FUNDRAISING_BY_ID = "SELECT * FROM FundraisingInitiatives WHERE fundraising_id = ?";
    private final String UPDATE_FUNDRAISING = "UPDATE FundraisingInitiatives SET user_id = ?, cause = ?, target_amount = ?, amount_received = ?, short_description = ?, deadline = ? WHERE fundraising_id = ?";
    private final String DELETE_FUNDRAISING = "DELETE FROM FundraisingInitiatives WHERE fundraising_id = ?";


    public int createFundraisingInitiative(FundraisingInitiative fundraising) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_FUNDRAISING, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, fundraising.getUserId());
            pstmt.setString(2, fundraising.getCause());
            pstmt.setDouble(3, fundraising.getTargetAmount());
            pstmt.setDouble(4, fundraising.getAmountReceived());
            pstmt.setString(5, fundraising.getShortDescription());
            pstmt.setDate(6, Date.valueOf(fundraising.getDeadline()));

            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error creating fundraising initiative: " + e.getMessage());
        }
        return -1; // Return -1 on failure
    }

    public List<FundraisingInitiative> getAllFundraisingInitiatives() {
        List<FundraisingInitiative> fundraisingInitiatives = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_FUNDRAISING);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                fundraisingInitiatives.add(mapFundraisingFromResultSet(rs));
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error retrieving all fundraising initiatives: " + e.getMessage());
        }
        return fundraisingInitiatives;
    }

    public FundraisingInitiative getFundraisingInitiativeById(int fundraisingId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_FUNDRAISING_BY_ID)) {

            pstmt.setInt(1, fundraisingId); // Set the fundraisingId parameter HERE
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapFundraisingFromResultSet(rs);
                }
            }

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error getting fundraising initiative by ID: " + e.getMessage()); // Or use a logger
        }
        return null;
    }

    public boolean updateFundraisingInitiative(FundraisingInitiative fundraising) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_FUNDRAISING)) {
            // Set parameters
            pstmt.setInt(1, fundraising.getUserId());
            pstmt.setString(2, fundraising.getCause());
            pstmt.setDouble(3, fundraising.getTargetAmount());
            pstmt.setDouble(4, fundraising.getAmountReceived());
            pstmt.setString(5, fundraising.getShortDescription());
            pstmt.setDate(6, Date.valueOf(fundraising.getDeadline()));
            pstmt.setInt(7, fundraising.getFundraisingId());  // Where clause

            return pstmt.executeUpdate() > 0;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            System.err.println("Error updating fundraising initiative: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFundraisingInitiative(int fundraisingId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_FUNDRAISING)) {

            pstmt.setInt(1, fundraisingId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting fundraising initiative: " + e.getMessage());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

//    private FundraisingInitiative mapFundraisingFromResultSet(ResultSet rs) throws SQLException {
//        FundraisingInitiative fundraising = new FundraisingInitiative(
//                rs.getInt("user_id"),
//                rs.getString("cause"),
//                rs.getDouble("target_amount"),
//                rs.getDouble("amount_received"),
//                rs.getString("short_description"),
//                rs.getDate("deadline").toLocalDate()
//        );
//        fundraising.setFundraisingId(rs.getInt("fundraising_id")); //Set the id to the model
//        return fundraising;
//    }

    private FundraisingInitiative mapFundraisingFromResultSet(ResultSet rs) throws SQLException {
        double amountReceived = rs.getDouble("amount_received");
        FundraisingInitiative fundraising = new FundraisingInitiative(
                rs.getInt("user_id"),
                rs.getString("cause"),
                rs.getDouble("target_amount"),
                amountReceived,
                rs.getString("short_description"),
                rs.getDate("deadline").toLocalDate()
        );
        fundraising.setFundraisingId(rs.getInt("fundraising_id")); //Set the id to the model
        return fundraising;
    }
}