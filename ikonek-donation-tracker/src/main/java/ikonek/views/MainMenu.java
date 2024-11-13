package ikonek.views;

import ikonek.exceptions.UserServiceException;
import ikonek.services.*;
import ikonek.models.Admin;
import ikonek.models.User;
import ikonek.utils.InputValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static AdminService adminService;
    private static HospitalService hospitalService;
    private static FundraiserService fundraiserService;
    private static MonetaryDonationService monetaryDonationService;
    private static BloodDonationService bloodDonationService;

    public MainMenu(UserService userService, AdminService adminService, HospitalService hospitalService, FundraiserService fundraiserService, MonetaryDonationService monetaryDonationService, BloodDonationService bloodDonationService) {
        this.userService = userService;
        this.adminService = adminService;
        this.hospitalService = hospitalService;
        this.fundraiserService = fundraiserService;
        this.monetaryDonationService = monetaryDonationService;
        this.bloodDonationService = bloodDonationService;
    }

    public int displayMenu() {
        System.out.println("\nWelcome to the Blood Donation and Fundraising System");
        System.out.println("1. User Login");
        System.out.println("2. Admin Login");
        System.out.println("3. Register");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");

        int choice = -1;
        try{
            choice = scanner.nextInt();
        }catch(InputMismatchException e){
            System.err.println("Invalid input. Please enter a number between 1 and 4.");

        }
        scanner.nextLine(); // Consume newline

        return choice;

    }

    public void handleMenuChoice(int choice, UserService userService, AdminService adminService, HospitalService hospitalService, FundraiserService fundraiserService, MonetaryDonationService monetaryDonationService, BloodDonationService bloodDonationService) {
        switch (choice) {
            case 1:
                UserMenu.loginUser(hospitalService, userService, bloodDonationService, fundraiserService, monetaryDonationService);
                break;
            case 2:
                //AdminMenu.loginAdmin(adminService, hospitalService, fundraiserService, monetaryDonationService, bloodDonationService);
                break;
            case 3:
                registerUser(userService);
                break;
            case 4:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void registerUser(UserService userService) {
        Scanner scanner = new Scanner(System.in);
        User newUser = null;
        boolean registrationSuccessful = false;

        String email = null;
        String password = null;
        String firstName = null;
        String middleName = null;
        String lastName = null;
        String gender = null;
        LocalDate birthDate = null;
        String bloodType = null;
        double weight = 0;
        String contactNumber = null;

        System.out.println("\n--- Welcome to the Registration Portal ---\n");

        while (!registrationSuccessful) {
            try {
                // Email
                while (email == null) {
                    System.out.print("💌 Please enter your email address: ");
                    email = scanner.nextLine();
                    if (!InputValidator.isValidEmail(email)) {
                        throw new UserServiceException("❌ Invalid email format. Press enter to try again or type 'exit' to quit: ");
                    }
//                    if (!userService.isUniqueEmail(email)) {
//                        throw new UserServiceException("❌ Email already exists. Press enter to try again or type 'exit' to quit: ");
//                    }
                }

                // Password
                while (password == null) {
                    System.out.print("🔐 Create a secure password (at least 8 characters): ");
                    password = scanner.nextLine();
                    if (password == null || password.isEmpty() || password.isBlank()) {
                        throw new UserServiceException("❌ Password must be at least 8 characters long. Press enter to try again or type 'exit' to quit: ");
                    }
                }

                // First Name
                while (firstName == null) {
                    System.out.print("👤 What is your first name? ");
                    firstName = scanner.nextLine();
                    firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
                    if (!InputValidator.isValidName(firstName)) {
                        throw new UserServiceException("❌ First name cannot be blank. Press enter to try again or type 'exit' to quit: ");
                    }
                }

                // Middle Name
                while (middleName == null) {
                    System.out.print("🖋 Enter your middle name (Optional): ");
                    middleName = scanner.nextLine();
                }

                // Last Name
                while (lastName == null) {
                    System.out.print("👤 What is your last name? ");
                    lastName = scanner.nextLine();
                    lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
                    if (!InputValidator.isValidName(lastName)) {
                        throw new UserServiceException("❌ Last name cannot be blank. Press enter to try again or type 'exit' to quit: ");
                    }
                }

                // Gender
                while (gender == null) {
                    System.out.print("🌈 Select your gender (Male, Female, Other): ");
                    gender = scanner.nextLine();
                    gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
                    if (!InputValidator.isValidGender(gender)) {
                        throw new UserServiceException("❌ Invalid gender selection. Press enter to try again or type 'exit' to quit: ");
                    }
                }

                // Birth Date
                while (birthDate == null) {
                    System.out.print("🎂 Please enter your birth date (yyyy-MM-dd): ");
                    String birthDateString = scanner.nextLine();
                    try {
                        birthDate = LocalDate.parse(birthDateString);
                        if (!InputValidator.isValidBirthDate(birthDate)) {
                            throw new UserServiceException("❌ Invalid birth date. Press enter to try again or type 'exit' to quit: ");
                        }
                    } catch (DateTimeParseException e) {
                        throw new UserServiceException("❌ Invalid date format. Use yyyy-MM-dd. Press enter to try again or type 'exit' to quit: ");
                    }
                }

                // Blood Type
                while (bloodType == null) {
                    System.out.print("🩸 Enter your blood type (A+, A-, B+, B-, AB+, AB-, O+, O-): ");
                    bloodType = scanner.nextLine().toUpperCase();
                    if (!InputValidator.isValidBloodType(bloodType)) {
                        throw new UserServiceException("❌ Invalid blood type. Press enter to try again or type 'exit' to quit: ");
                    }
                }

                // Weight
                while (weight == 0) {
                    System.out.print("⚖️ Please enter your weight (kg): ");
                    String weightString = scanner.nextLine();
                    try {
                        weight = Double.parseDouble(weightString);
                        if (!InputValidator.isValidWeight(weight)) {
                            throw new UserServiceException("❌ Invalid weight. Press enter to try again or type 'exit' to quit: ");
                        }
                    } catch (NumberFormatException e) {
                        throw new UserServiceException("❌ Invalid weight format. Press enter to try again or type 'exit' to quit: ");
                    }
                }

                // Contact Number
                while (contactNumber == null) {
                    System.out.print("📱 Enter your contact number: ");
                    contactNumber = scanner.nextLine();
                    if (!InputValidator.isValidContactNumber(contactNumber)) {
                        throw new UserServiceException("❌ Invalid contact number. Press enter to try again or type 'exit' to quit: ");
                    }
                }

                // Register User
                newUser = userService.registerUser(firstName, middleName, lastName, gender, birthDate, email, password, bloodType, weight, contactNumber);
                if (newUser != null) {
                    registrationSuccessful = true;
                }
            } catch (UserServiceException | DateTimeParseException | NumberFormatException e) {
                System.err.print(e.getMessage());
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                // Reset fields for re-entry
                email = null;
                password = null;
                firstName = null;
                middleName = null;
                lastName = null;
                gender = null;
                birthDate = null;
                bloodType = null;
                weight = 0;
                contactNumber = null;
            }
        }

        if (newUser != null) {
            System.out.println("\n🎉 Registration successful! Your User ID is: " + newUser.getUserId());
            System.out.println("🔑 Keep it safe for future reference. Welcome to our community!");
        }
    }
}