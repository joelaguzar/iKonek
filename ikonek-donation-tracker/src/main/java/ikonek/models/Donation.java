package ikonek.models;

import ikonek.exceptions.MonetaryDonationServiceException;

import java.time.LocalDateTime;

public interface Donation {
    int getDonationId();
    void setDonationId(int donationId);
    int getDonorId();
    LocalDateTime getDonationDate();
    void processDonation() throws MonetaryDonationServiceException;
}