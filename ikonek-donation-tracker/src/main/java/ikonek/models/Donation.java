package ikonek.models;

import java.time.LocalDateTime;

public class Donation {
    private int donationId;
    private int donorId;
    private int fundraisingId;
    private double donationAmount;  // Using double
    private LocalDateTime donationDate;

    // Constructor
    public Donation(int donorId, int fundraisingId, double donationAmount) {
        this.donorId = donorId;
        this.fundraisingId = fundraisingId;
        this.donationAmount = donationAmount;
        this.donationDate = LocalDateTime.now();
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

    public LocalDateTime getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDateTime donationDate) {
        this.donationDate = donationDate;
    }


    @Override
    public String toString() {
        return "Donation{" +
                "donationId=" + donationId +
                ", donorId=" + donorId +
                ", fundraisingId=" + fundraisingId +
                ", donationAmount=" + donationAmount +
                ", donationDate=" + donationDate +
                '}';
    }
}