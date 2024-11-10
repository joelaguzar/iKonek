package ikonek.models;

import java.time.LocalDateTime;

public class BloodDonationImpl implements BloodDonation {
    private int donationId;
    private int userId;
    private int hospitalId;
    private LocalDateTime donationDate;
    private String status;
    private String failureReason;

    // Constructor
    public BloodDonationImpl(int userId, int hospitalId, String bloodType) {
        this.userId = userId;
        this.hospitalId = hospitalId;
        this.donationDate = LocalDateTime.now(); // Set initial donation date and time
        this.status = "Pending"; // Set initial status
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
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId;}

    @Override
    public String getFailureReason() {return failureReason; }

    @Override
    public void setFailureReason(String failureReason) { this.failureReason = failureReason;}


    @Override
    public void processDonation() {
        // Implement the logic to process a blood donation here.
        // This might involve checking eligibility (age, weight, etc.),
        // updating hospital inventory, sending notifications, etc.
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