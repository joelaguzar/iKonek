package ikonek.services;

import ikonek.dao.MonetaryDonationDao;
import ikonek.dao.UserDao;
import ikonek.models.FundraisingInitiative;
import ikonek.dao.FundraisingInitiativeDao;
import ikonek.models.MonetaryDonation;
import ikonek.models.MonetaryDonationImpl;
import ikonek.exceptions.MonetaryDonationServiceException; // Renamed exception
import ikonek.models.User;

import java.util.List;

public class MonetaryDonationService {

    private final MonetaryDonationDao monetaryDonationDao;
    private final FundraiserService fundraiserService;
    private final UserDao userDao;
    private final FundraisingInitiativeDao fundraisingInitiativeDao;

    public MonetaryDonationService(MonetaryDonationDao monetaryDonationDao, FundraiserService fundraiserService, UserDao userDao, FundraisingInitiativeDao fundraisingInitiativeDao) {
        this.monetaryDonationDao = monetaryDonationDao;
        this.fundraiserService = fundraiserService;
        this.userDao = userDao;
        this.fundraisingInitiativeDao = fundraisingInitiativeDao;
    }

    public int createMonetaryDonation(int donorId, int fundraisingId, double donationAmount) throws MonetaryDonationServiceException {
        if (donationAmount <= 0) {
            throw new MonetaryDonationServiceException("Donation amount must be positive.");
        }
        // check if donor and fundraising initiative exist
        User donor = userDao.getUserById(donorId);
        FundraisingInitiative initiative = fundraisingInitiativeDao.getFundraisingInitiativeById(fundraisingId);

        if (donor == null) {
            throw new MonetaryDonationServiceException("Donor with ID " + donorId + " does not exist.");
        }
        if (initiative == null) {
            throw new MonetaryDonationServiceException("Fundraising initiative with ID " + fundraisingId + " does not exist.");
        }

        MonetaryDonationImpl donation = new MonetaryDonationImpl(donorId, fundraisingId, donationAmount);
        donation.setFundraiserService(fundraiserService); // Inject FundraiserService here

        int donationId = monetaryDonationDao.createDonation(donation);
        if (donationId == -1) {
            throw new MonetaryDonationServiceException("Failed to create monetary donation.");
        }

        donation.setDonationId(donationId);
        processDonation(donation); // Process the donation here
        return donationId;
    }

    public MonetaryDonation getMonetaryDonationsByUserId(int donationId) {
        return monetaryDonationDao.getDonationById(donationId);
    }

    public List<MonetaryDonation> getMonetaryDonationByUserId(int donorId) {
        return monetaryDonationDao.getMonetaryDonationsByUserId(donorId);
    }

    public List<MonetaryDonation> getMonetaryDonationsByFundraisingId(int fundraisingId) {
        return monetaryDonationDao.getMonetaryDonationsByFundraisingId(fundraisingId);
    }

    public boolean updateMonetaryDonation(MonetaryDonation donation) {
        try {
            if (donation.getDonationAmount() <= 0){
                throw new MonetaryDonationServiceException("Donation amount must be positive");
            }
            if(donation.getDonorId() <= 0 || donation.getFundraisingId() <= 0){
                throw new MonetaryDonationServiceException("Invalid donorId or fundraisingId");
            }
            return monetaryDonationDao.updateDonation(donation);
        } catch (MonetaryDonationServiceException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    public void displayTotalFundsRaised() {
        try {
            double totalFundsRaised = monetaryDonationDao.getTotalFundsRaised();
            System.out.println("\n--- Total Funds Raised Report ---");
            System.out.println("Total Funds Raised: PHP" + totalFundsRaised);

        } catch (Exception e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }

    public boolean deleteMonetaryDonation(int donationId) {
        return monetaryDonationDao.deleteDonation(donationId);
    }

    public void processDonation(MonetaryDonation monetaryDonation) {
        try {
            // retrieve the FundraisingInitiative
            FundraisingInitiative fundraisingInitiative = fundraiserService.getFundraisingInitiativeById(monetaryDonation.getFundraisingId());
            if (fundraisingInitiative == null) {
                throw new MonetaryDonationServiceException("Fundraising initiative not found.");
            }

            //update the fundraising initiative's amount received
            double newAmountReceived = fundraisingInitiative.getAmountReceived() + (monetaryDonation.getDonationAmount()/2);
            fundraisingInitiative.setAmountReceived(newAmountReceived);

            //update the FundraisingInitiative in the database
            fundraiserService.updateFundraisingInitiative(fundraisingInitiative);

        } catch (MonetaryDonationServiceException e) {
            System.err.println("Error processing monetary donation: " + e.getMessage());
        }
    }
}
