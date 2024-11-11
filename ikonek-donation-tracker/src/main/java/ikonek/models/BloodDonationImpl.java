package ikonek.models;

import ikonek.exceptions.BloodDonationServiceException;
import ikonek.services.HospitalService;

import java.time.LocalDateTime;

public class BloodDonationImpl implements BloodDonation {
    private int donationId;
    private int userId;
    private int hospitalId;
    private LocalDateTime donationDate;
    private String status;
    private String failureReason;
    private HospitalService hospitalService;

    // Constructor
    public BloodDonationImpl(int userId, int hospitalId) {
        this.userId = userId;
        this.hospitalId = hospitalId;
        this.donationDate = LocalDateTime.now(); // Set initial donation date and time
        this.status = "Pending"; // Set initial status
    }

    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }
    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @Override
    public int getDonationId() {
        return donationId;
    }

    @Override
    public void setDonationId(int donationId) {
        this.donationId = donationId;
    }

    @Override
    public int getDonorId() {  // From Donation interface
        return userId;        // userId is the donorId for blood donations
    }

    @Override
    public LocalDateTime getDonationDate() {
        return donationDate;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int getHospitalId() {
        return hospitalId;
    }

    @Override
    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;}

    @Override
    public String getFailureReason() {
        return failureReason; }

    @Override
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;}

    @Override
    public void processDonation() {
        try {
            if (hospitalService == null) {
                throw new BloodDonationServiceException("HospitalService not injected.");
            }
            // 1. Retrieve Hospital information
            Hospital hospital = hospitalService.getHospitalById(hospitalId);
            if (hospital == null) {
                throw new BloodDonationServiceException("Hospital not found for donation.");
            }

            // 3. Update Donation Status (This will be done by Admin later)
            // 4. (Optional) Generate Donation Ticket/Certificate
        } catch (BloodDonationServiceException e) {
            // Handle the exception here
            System.err.println("Error processing blood donation: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "BloodDonationImpl{" +
                "donationId=" + donationId +
                ", userId=" + userId +
                ", hospitalId=" + hospitalId +
                ", donationDate=" + donationDate +
                ", status='" + status + '\'' +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}