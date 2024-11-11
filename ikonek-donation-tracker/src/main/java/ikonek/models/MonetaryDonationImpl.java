package ikonek.models;

import ikonek.exceptions.FundraiserServiceException;
import ikonek.exceptions.MonetaryDonationServiceException;
import ikonek.services.FundraiserService;

import java.time.LocalDateTime;

public class MonetaryDonationImpl implements MonetaryDonation {

    private int donationId;
    private int donorId;
    private int fundraisingId;
    private double donationAmount;
    private LocalDateTime donationDate;
    private FundraiserService fundraiserService;

    // Constructor
    public MonetaryDonationImpl(int donorId, int fundraisingId, double donationAmount) { // Update constructor
        this.donorId = donorId;
        this.fundraisingId = fundraisingId;
        this.donationAmount = donationAmount;
        this.donationDate = LocalDateTime.now();
    }

    public void setFundraiserService(FundraiserService fundraiserService) {
        this.fundraiserService = fundraiserService;
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
    public int getDonorId() {
        return donorId;
    }

    @Override
    public LocalDateTime getDonationDate() {
        return donationDate;
    }

    @Override
    public double getDonationAmount() {
        return donationAmount;
    }

    @Override
    public void setDonationAmount(double donationAmount) {
        this.donationAmount = donationAmount;
    }

    @Override
    public int getFundraisingId() {
        return fundraisingId;
    }

    @Override
    public void setFundraisingId(int fundraisingId) {
        this.fundraisingId = fundraisingId;
    }


    @Override
    public void processDonation() throws MonetaryDonationServiceException {
        if (fundraiserService == null) {
            throw new MonetaryDonationServiceException("FundraiserService not injected.");
        }

        try {
            // 1. Retrieve the FundraisingInitiative
            FundraisingInitiative fundraisingInitiative = fundraiserService.getFundraisingInitiativeById(fundraisingId);
            if (fundraisingInitiative == null) {
                throw new MonetaryDonationServiceException("Fundraising initiative not found.");
            }

            // 2. Update the fundraising initiative's amount received
            double newAmountReceived = fundraisingInitiative.getAmountReceived() + donationAmount;
            fundraisingInitiative.setAmountReceived(newAmountReceived);

            // 3. Update the FundraisingInitiative in the database
            fundraiserService.updateFundraisingInitiative(fundraisingInitiative);

            // 4. (Optional) Send a confirmation email or notification to the donor

        } catch (MonetaryDonationServiceException e) {
            System.err.println("Error processing monetary donation: " + e.getMessage()); // Or use a logger
        }
    }

    @Override
    public String toString() {
        return "MonetaryDonationImpl{" +
                "donationId=" + donationId +
                ", donorId=" + donorId +
                ", fundraisingId=" + fundraisingId +
                ", donationAmount=" + donationAmount +
                ", donationDate=" + donationDate +
                '}';
    }
}