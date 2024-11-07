package com.ikonek.services;

import com.ikonek.data.AdminData;
import com.ikonek.data.BloodDonationData;
import com.ikonek.data.HospitalData;
import com.ikonek.data.UserData;
import com.ikonek.entities.Admin;
import com.ikonek.entities.BloodDonation;
import com.ikonek.entities.Hospital;
import com.ikonek.utils.InputValidation;
import com.ikonek.utils.PasswordHashing;
import com.ikonek.data.AdminData.AdminNotFoundException;
import com.ikonek.data.HospitalData.DuplicateHospitalException;
import com.ikonek.data.HospitalData.HospitalNotFoundException;
import com.ikonek.data.UserData.UserNotFoundException;
import com.ikonek.utils.Database;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminServices {
    private final AdminData adminData;
    private final HospitalData hospitalData;
    private final UserData userData;
    private final InputValidation inputValidation;
    private final PasswordHashing passwordHashing;
    private final Database database;
    private final BloodDonationData bloodDonationData;

    public AdminServices (AdminData adminData, HospitalData hospitalData, UserData userData, InputValidation inputValidation, PasswordHashing passwordHashing, Database database, BloodDonationData bloodDonationData) {
        this.adminData = adminData;
        this.hospitalData = hospitalData;
        this.userData = userData;
        this.inputValidation = inputValidation;
        this.passwordHashing = passwordHashing;
        this.database = database;
        this.bloodDonationData = bloodDonationData;
    }

    public Admin loginAdmin(String username, String password) throws SQLException {
        if (!inputValidation.isValidEmail(username) || !inputValidation.isValidPassword(password)) {
            System.out.println("Invalid Email or Password");
            return null;
        }

        try {
            Admin admin = adminData.getAdminByUsername(username);
            if (admin != null && passwordHashing.verifyPassword(password, admin.getPassword())) {
                return admin;
            } else {
                System.out.println("Invalid Email or Password");
                return null;
            }
        } catch (AdminNotFoundException e) {
            System.err.println("Login Failed: " + e.getMessage());
            return null;
        }
    }

    public void addHospital(String name, String address, String city, String province, String region, String contactNumber) throws SQLException, DuplicateHospitalException {
        if (!inputValidation.isValidHospitalName(name) || !inputValidation.isValidContactNumber(contactNumber) ) {
            System.out.println("Invalid Input");
            return;
        }

        Hospital hospital = new Hospital(name, address, city, province, region, contactNumber);
        try {
            hospitalData.insertHospital(hospital);
            System.out.println("Hospital added successfully. Hospital ID: " + hospital.getHospitalId());
        } catch (DuplicateHospitalException | SQLException e) {
            System.err.println("Error adding hospital: " + e.getMessage());
            throw e;
        }
    }

    public void updateHospital(int hospitalId, String name, String address, String city, String province, String region, String contactNumber) throws SQLException, HospitalNotFoundException {
        if (!inputValidation.isValidHospitalName(name) || !inputValidation.isValidContactNumber(contactNumber)) {
            System.out.println("Invalid Input");
            return;
        }
        try {
            Hospital hospital = hospitalData.getHospitalById(hospitalId);
            if (hospital != null) {
                hospital.setHospitalName(name);
                hospital.setHospitalAddLine1(address);
                hospital.setHospitalCity(city);
                hospital.setHospitalProvince(province);
                hospital.setHospitalRegion(region);
                hospital.setHospitalContact(contactNumber);
                hospitalData.updateHospital(hospital);
                System.out.println("Hospital updated successfully.");
            } else {
                System.out.println("Hospital not found.");
            }
        } catch (HospitalNotFoundException | SQLException e) {
            System.err.println("Error updating hospital: " + e.getMessage());
            throw e;
        }
    }

    public void deleteHospital(int hospitalId) throws SQLException, HospitalNotFoundException {
        try {
            hospitalData.deleteHospital(hospitalId);
            System.out.println("Hospital deleted successfully.");
        } catch (HospitalNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            throw e; // Re-throw the exception for handling at a higher level
        }
    }

    public List<Hospital> getAllHospitals() throws SQLException {
        return hospitalData.getAllHospitals();
    }

    public void verifyDonation(int donationId, String status) throws SQLException, BloodDonationData.BloodDonationNotFoundException, HospitalNotFoundException, UserNotFoundException {
        try {
            BloodDonation bloodDonation = bloodDonationData.getBloodDonationById(donationId);
            if (bloodDonation != null) {
                bloodDonation.setStatus(status);
                bloodDonationData.updateBloodDonation(bloodDonation);
                System.out.println("Donation verification updated successfully.");
            } else {
                System.out.println("Donation not found.");
            }
        } catch (BloodDonationData.BloodDonationNotFoundException e) {
            System.err.println("Error verifying donation: " + e.getMessage());
            throw e;
        }

    }

    public void makeUserAdmin(int userId, String adminType) throws SQLException, UserNotFoundException {
        try {
            Admin admin = new Admin(adminType);
            admin.setUserId(userId);
            adminData.insertAdmin(admin);
            System.out.println("User " + userId + " is now an admin.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    public List<Admin> getAllAdmins() throws SQLException {
        return adminData.getAllAdmins();
    }

    public void viewDonationRequests() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        try {
            List<BloodDonation> bloodDonations = bloodDonationData.getAllBloodDonations();

            if (bloodDonations.size() == 0) {
                System.out.println("No Donation found");
                return;
            }
            System.out.println("List of Donation Request");
            int i = 1;
            for (BloodDonation bloodDonation : bloodDonations) {
                System.out.println(i + ". " + bloodDonation);
                i++;
            }

            System.out.print("Enter Donation ID to verify: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter Status (Successful/Failed): ");
            String status = scanner.nextLine();

            verifyDonation(choice, status);

        } catch (SQLException | BloodDonationData.BloodDonationNotFoundException | HospitalData.HospitalNotFoundException | UserData.UserNotFoundException e) {
            System.err.println("Error retrieving list of blood donation request" + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}