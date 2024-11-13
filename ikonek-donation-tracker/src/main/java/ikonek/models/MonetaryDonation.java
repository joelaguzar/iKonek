package ikonek.models;

import ikonek.exceptions.MonetaryDonationServiceException;

public interface MonetaryDonation extends Donation {
    double getDonationAmount();
    void setDonationAmount(double donationAmount);

    int getFundraisingId();
    void setFundraisingId(int fundraisingId);

    String getFundraisingCause() throws MonetaryDonationServiceException;
}
