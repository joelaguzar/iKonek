package ikonek.services;

import ikonek.dao.BloodDonationDao;
import ikonek.dao.HospitalDao;
import ikonek.dao.UserDao;
import ikonek.exceptions.BloodDonationServiceException;
import ikonek.models.BloodDonation;
import ikonek.models.BloodDonationImpl;
import ikonek.models.Hospital;
import ikonek.models.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

public class BloodDonationService {
    private final BloodDonationDao bloodDonationDao;
    private final UserDao userDao;
    private final HospitalDao hospitalDao;

    public BloodDonationService(BloodDonationDao bloodDonationDao, UserDao userDao, HospitalDao hospitalDao) {
        this.bloodDonationDao = bloodDonationDao;
        this.userDao = userDao;
        this.hospitalDao = hospitalDao;
    }

    public int createBloodDonation(int userId, int hospitalId, LocalDate donationDate) throws BloodDonationServiceException {
        User user = userDao.getUserById(userId);
        Hospital hospital = hospitalDao.getHospitalById(hospitalId);


        if (user == null) {
            throw new BloodDonationServiceException("User with ID " + userId + " does not exist.");
        }
        if (hospital == null) {
            throw new BloodDonationServiceException("Hospital with ID " + hospitalId + " does not exist.");
        }

        BloodDonation donation = new BloodDonationImpl(userId, hospitalId, donationDate);
        int donationId = bloodDonationDao.createBloodDonation(donation);
        if (donationId == -1) { // Check for errors during creation
            throw new BloodDonationServiceException("Failed to create blood donation.");
        }
        return donationId;
    }

    public BloodDonation getBloodDonationById(int donationId) {
        return bloodDonationDao.getBloodDonationById(donationId);}

    public List<BloodDonation> getBloodDonationsByUserId(int userId) {
        return bloodDonationDao.getBloodDonationsByUserId(userId);
    }

    public List<BloodDonation> getPendingBloodDonationsByUserId(int userId) {
        return bloodDonationDao.getPendingBloodDonationsByUserId(userId);
    }

    public List<BloodDonation> getAllBloodDonations() {
        return bloodDonationDao.getAllBloodDonations();}

    public boolean updateBloodDonation(BloodDonation bloodDonation) {
        try {
            // Input validation (you might want to add more checks here)
            if(bloodDonation.getDonationId() <= 0){
                throw new BloodDonationServiceException("Invalid donationId");
            }
            if(bloodDonationDao.getBloodDonationById(bloodDonation.getDonationId()) == null){
                throw new BloodDonationServiceException("Blood donation does not exist.");
            }
            return bloodDonationDao.updateBloodDonation(bloodDonation);
        } catch (BloodDonationServiceException e) {
            // Handle the exception, e.g., log the error and return a default value
            System.err.println("Error updating blood donation: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBloodDonation(int donationId) {
        return bloodDonationDao.deleteBloodDonation(donationId);}

    public boolean isEligibleForBloodDonation(int userId) throws BloodDonationServiceException {
        User user = userDao.getUserById(userId);  // Retrieve User object

        if (user == null) {
            throw new BloodDonationServiceException("User not found."); // Or handle appropriately
        }
        int age = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
        return age >= 18 && age <= 65 && user.getWeight() >= 50;
    }

    public void processBloodDonation(BloodDonation donation) {
        try {
            // 1. Retrieve User and Hospital information (for more detailed checks/updates)
            User user = userDao.getUserById(donation.getDonorId());
            if (user == null) {
                throw new BloodDonationServiceException("User not found for donation.");
            }
            Hospital hospital = hospitalDao.getHospitalById(donation.getHospitalId());
            if (hospital == null) {
                throw new BloodDonationServiceException("Hospital not found for donation.");
            }

            // 2. Eligibility Check (using the helper method)
            if (!isEligibleForBloodDonation(user.getUserId())) { // Pass userId
                donation.setStatus("Failed");
                donation.setFailureReason("Donor not eligible (age, weight, or other criteria)."); // Set specific reason
                bloodDonationDao.updateBloodDonation(donation); // Update status in the database
                return; // Exit early if not eligible
            }

            // 4. (Optional) Generate Donation Ticket/Certificate (using DonationTicketView):

        } catch (BloodDonationServiceException e) {
            System.err.println("Error processing blood donation: " + e.getMessage()); // Replace with proper logging
        }
    }

    public void displayDonationsByBloodType() {
        try {
            Map<String, Integer> bloodTypeCounts = bloodDonationDao.getDonationCountsByBloodType();

            if (bloodTypeCounts.isEmpty()) {
                System.out.println("\nNo blood donations found.");
                return;
            }


            System.out.println("\n--- Blood Donation Report by Blood Type ---");
            for (Map.Entry<String, Integer> entry : bloodTypeCounts.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage()); // Or use a logger
        }
    }

    public boolean cancelBloodDonation(int donationId) {
        return bloodDonationDao.cancelBloodDonation(donationId);
    }
}

