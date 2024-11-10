package ikonek.models;

import java.time.LocalDateTime;

public class Donation {
    private int donationId;           // Unique identifier for each donation
    private int donorId;              // ID of the donor (foreign key from Users)
    private int fundraisingId;        // ID of the fundraising initiative (foreign key from FundraisingInitiatives)
    private double donationAmount; // Amount donated
    private LocalDateTime donationDate; // Date and time of the donation

    // Constructor
    public Donation(int donorId, int fundraisingId, double donationAmount) {
        this.donorId = donorId;
        this.fundraisingId = fundraisingId;
        this.donationAmount = donationAmount;
        this.donationDate = LocalDateTime.now(); // Default to current timestamp
    }

    // Getters and Setters
    public int getDonationId() {
        return donationId;
    }

    public void setDonationId(int donationId) {
        this.donationId = donationId;
    }

    public int getDonorId() {
        return donorId;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public int getFundraisingId() {
        return fundraisingId;
    }

    public void setFundraisingId(int fundraisingId) {
        this.fundraisingId = fundraisingId;
    }

    public double getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(double donationAmount) {
        this.donationAmount = donationAmount;
    }
    @Override
    public String toString() {
        return "Donation{" +
                "donationId=" + donationId +
                "donorId=" + donorId +
                "fundraisingId=" + fundraisingId +
                "donationAmount=" + donationAmount +
                "donationDate=" + donationDate +
                '}';
    }
}
