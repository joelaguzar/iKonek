package ikonek.models;

import java.time.LocalDateTime;

public interface Donation {
    int getDonationId();
    void setDonationId(int donationId);
    int getDonorId();
    LocalDateTime getDonationDate();
    void processDonation();
}