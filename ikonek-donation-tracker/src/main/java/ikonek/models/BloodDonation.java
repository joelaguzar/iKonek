package ikonek.models;

import java.time.LocalDateTime;

public class BloodDonation {
    private int donationId;
    private int userId;
    private int hospitalId;
    private LocalDateTime donationDate;
    private String status;
    private String failureReason; // Add field for failure reason

    // Constructor
    public BloodDonation(int userId, int hospitalId, LocalDateTime donationDate, String status) {
        this.userId = userId;
        this.hospitalId = hospitalId;
        this.donationDate = donationDate;  // Pass donationDate directly
        this.status = status;

    }

    // Getters and setters for all fields
    public int getDonationId() {
        return donationId;
    }

    public void setDonationId(int donationId) {
        this.donationId = donationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public LocalDateTime getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }


    @Override
    public String toString() {
        return "BloodDonation{" +
                "donationId=" + donationId +
                ", userId=" + userId +
                ", hospitalId=" + hospitalId +
                ", donationDate=" + donationDate +
                ", status='" + status + '\'' +
                ", failureReason='" + failureReason + '\'' +  // Include failureReason
                '}';
    }
}
