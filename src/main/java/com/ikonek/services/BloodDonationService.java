package com.ikonek.services;

import com.ikonek.data.BloodDonationData;
import com.ikonek.data.HospitalData;
import com.ikonek.data.UserData;
import com.ikonek.entities.BloodDonation;
import com.ikonek.entities.Hospital;
import com.ikonek.entities.User;
import com.ikonek.utils.InputValidation;
import com.ikonek.data.BloodDonationData.BloodDonationNotFoundException;
import com.ikonek.data.HospitalData.HospitalNotFoundException;
import com.ikonek.data.UserData.UserNotFoundException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class BloodDonationServices {
    private final BloodDonationData bloodDonationData;
    private final UserData userData;
    private final HospitalData hospitalData;
    private final InputValidation inputValidation;

    public BloodDonationServices (BloodDonationData bloodDonationData, UserData userData, HospitalData hospitalData, InputValidation inputValidation) {
        this.bloodDonationData = bloodDonationData;
        this.userData = userData;
        this.hospitalData = hospitalData;
        this.inputValidation = inputValidation;
    }

    public void scheduleBloodDonation(int userId, int hospitalId, String bloodType, String donationDate, String timeSlot, String consentForm, String medicalHistory) throws SQLException, UserNotFoundException, HospitalNotFoundException{
        if (!inputValidation.isValidBloodType(bloodType) || !inputValidation.isValidDate(donationDate) || inputValidation.isValidTimeSlot(timeSlot) == null) { // Corrected condition
            System.out.println("Invalid input for Blood Type, Donation Date or Time Slot."); // More specific message
            return;
        }
        try{
            User user = userData.getUserById(userId);
            if(user == null){
                System.out.println("User Not Found");
                return; // Or throw an exception
            }
            if(!inputValidation.isUserEligible(user)){
                System.out.println("User is not Eligible for Blood Donation!");
                return;
            }
            Hospital hospital = hospitalData.getHospitalById(hospitalId);

            if (hospital == null) {
                System.out.println("Hospital not found.");
                return; // Or throw an exception
            }

            LocalDate donationDateObj = LocalDate.parse(donationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Ensure date format is correct
            LocalTime timeSlotObj = LocalTime.parse(timeSlot, DateTimeFormatter.ofPattern("HH:mm")); // Ensure time format is correct

            BloodDonation bloodDonation = new BloodDonation(userId, hospitalId, bloodType, donationDateObj, timeSlotObj, consentForm, medicalHistory, "Pending");
            bloodDonationData.insertBloodDonation(bloodDonation);
            System.out.println("Blood donation scheduled successfully. Donation ID: " + bloodDonation.getDonationId());

        }catch (SQLException | HospitalNotFoundException | UserNotFoundException e) {
            System.err.println("Error scheduling blood donation: " + e.getMessage());
            throw e; // Rethrow the exception after printing the error message
        }
    }

    public void updateBloodDonationStatus(int donationId, String status) throws SQLException, BloodDonationNotFoundException {
        try {
            BloodDonation bloodDonation = bloodDonationData.getBloodDonationById(donationId);

            if (bloodDonation != null) {
                bloodDonation.setStatus(status);
                bloodDonationData.updateBloodDonation(bloodDonation);
                System.out.println("Blood donation status updated successfully.");
            } else {
                System.out.println("Blood donation not found.");
            }
        } catch (BloodDonationNotFoundException | SQLException e) {
            System.err.println("Error updating blood donation status: " + e.getMessage());
            throw e;
        }
    }

    public void cancelBloodDonation(int donationId) throws SQLException, BloodDonationNotFoundException {
        try {
            bloodDonationData.deleteBloodDonation(donationId);
            System.out.println("Blood donation cancelled successfully.");
        } catch (BloodDonationNotFoundException e) {
            System.err.println("Error cancelling blood donation: " + e.getMessage());
            throw e; //Or handle the exception in another way.
        }
    }

    public List<BloodDonation> getAllBloodDonations() throws SQLException {
        return bloodDonationData.getAllBloodDonations();
    }

    public BloodDonation getBloodDonationById(int donationId) throws SQLException{
        try {
            return bloodDonationData.getBloodDonationById(donationId);
        } catch (BloodDonationNotFoundException e) {
            System.out.println(e.getMessage());
            return null; //Return null or handle the exception.
        }
    }

    public void viewDonationStatus(int userId) throws SQLException{
        List<BloodDonation> bloodDonations = bloodDonationData.getAllBloodDonations();

        if(bloodDonations.isEmpty()){ // Check if empty using isEmpty()
            System.out.println("No Donations found for User ID " + userId);
            return;
        }

        int i = 1;
        for (BloodDonation bloodDonation : bloodDonations) {
            if (bloodDonation.getUserId() == userId) {
                System.out.println(i + ". " + bloodDonation);
                i++;
            }
        }
    }

    public void viewAllDonationStatus() throws SQLException{
        List<BloodDonation> bloodDonations = bloodDonationData.getAllBloodDonations();

        if(bloodDonations.isEmpty()){
            System.out.println("No Donations found.");
            return;
        }

        int i = 1;
        for (BloodDonation bloodDonation : bloodDonations) {
            System.out.println(i + ". " + bloodDonation);
            i++;
        }
    }
}
