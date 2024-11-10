package ikonek.models;

import java.math.BigDecimal;

public interface MonetaryDonation extends Donation {
    double getDonationAmount();
    void setDonationAmount(double donationAmount);

    int getFundraisingId();
    void setFundraisingId(int fundraisingId);
}
