package ikonek.models;

import java.time.LocalDate;

public interface BloodDonation extends Donation {
    int getHospitalId();
    void setHospitalId(int hospitalId);

    void setDonationDate(LocalDate donationDate); // Add this method

    String getStatus();
    void setStatus(String status);

    String getFailureReason();
    void setFailureReason(String failureReason);
}