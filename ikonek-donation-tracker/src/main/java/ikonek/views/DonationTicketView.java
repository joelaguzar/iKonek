package ikonek.views;

import ikonek.exceptions.MonetaryDonationServiceException;
import ikonek.models.*;
import ikonek.services.HospitalService;
import ikonek.services.UserService;

import java.time.format.DateTimeFormatter;

public class DonationTicketView {

    public static void displayDonationTicket(Donation donation, HospitalService hospitalService, UserService userService) throws MonetaryDonationServiceException {
        User user = userService.getUserById(donation.getDonorId());

        System.out.println("\n========================== Donation Ticket ============================");
        System.out.println("       📅 iKonek: Every Drop Counts, Every Click Matters 📅");
        System.out.println("=======================================================================");

        System.out.printf("🎫 Donation ID       : %d%n", donation.getDonationId());
        System.out.printf("🙋 Donor Name        : %s %s %s%n", user.getFirstName(), user.getMiddleName(), user.getLastName());
        System.out.printf("📆 Donation Date     : %s%n", donation.getDonationDate().format(DateTimeFormatter.ISO_DATE));

        if (donation instanceof MonetaryDonation) {
            MonetaryDonation monetaryDonation = (MonetaryDonation) donation;
            System.out.println("\n--------------- Donation Details ---------------");
            System.out.printf("💸 Donation Type     : Monetary%n");
            System.out.printf("💵 Donation Amount   : PHP %.2f%n", monetaryDonation.getDonationAmount());
            System.out.printf("📂 Donation Cause    : %s%n", monetaryDonation.getFundraisingCause());
        } else if (donation instanceof BloodDonation) {
            BloodDonation bloodDonation = (BloodDonation) donation;
            System.out.println("\n--------------- Donation Details ---------------");
            System.out.printf("🩸 Donation Type     : Blood%n");
            System.out.printf("🩸 Blood Type        : %s%n", user.getBloodType());
            Hospital hospital = hospitalService.getHospitalById(bloodDonation.getHospitalId());
            System.out.printf("🏥 Hospital          : %s, %s %s%n", hospital.getName(), hospital.getCity(), hospital.getProvince());
            System.out.printf("📊 Status            : %s%n", bloodDonation.getStatus());
            if (bloodDonation.getFailureReason() != null) {
                System.out.printf("❌ Failure Reason    : %s%n", bloodDonation.getFailureReason());
            }
        }
        System.out.println("\n========================================================================");
        System.out.println("Thank you for your contribution! Your donation truly makes a difference.");
        System.out.println("========================================================================");
    }
}
