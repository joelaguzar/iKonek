package ikonek.views;

import ikonek.exceptions.UserServiceException;
import ikonek.services.*;
import ikonek.models.User;
import ikonek.utils.InputValidator;

import java.time.LocalDate;
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
        String ikonekArt =
                "   II   K   K    OOO    N   N   EEEEE    K   K \n" +
                        "        K  K    O   O   NN  N   E        K  K  \n" +
                        "   II   KKK     O   O   N K N   EEEE     KKK   \n" +
                        "   II   K  K    O   O   N  KK   E        K  K  \n" +
                        "   II   K   K    OOO    N   K   EEEEE    K   K \n";

        System.out.println("\n" + ikonekArt);
        System.out.println("===============================================");
        System.out.println("✨ Welcome to iKonek: Every Drop Counts ✨");
        System.out.println("Your contribution matters in saving lives!");
        System.out.println("===============================================");

        System.out.println("\n1. 🔐 User Login");
        System.out.println("2. 👨‍💼 Admin Login");
        System.out.println("3. 📝 Register for a New Account");
        System.out.println("4. ❌ Exit");

        System.out.print("\nPlease enter a number between 1 and 4: ");

        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("⚠️ Invalid input! Please enter a number between 1 and 4.");
        }
        scanner.nextLine();

        return choice;
    }

    public void handleMenuChoice(int choice, UserService userService, AdminService adminService, HospitalService hospitalService, FundraiserService fundraiserService, MonetaryDonationService monetaryDonationService, BloodDonationService bloodDonationService) {
        switch (choice) {
            case 1:
                UserMenu.loginUser(hospitalService, userService, bloodDonationService, fundraiserService, monetaryDonationService);
                break;
            case 2:
                AdminMenu.loginAdmin(adminService, hospitalService, fundraiserService, monetaryDonationService, bloodDonationService, userService);
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

        System.out.println("\n--- Welcome to the Registration Portal ---");

        while (!registrationSuccessful) {
            try {
                // Email
                while (email == null) {
                    System.out.print("\n💌 Please enter your email address (or type 'exit' to quit): ");
                    email = scanner.nextLine();

                    // exit the registration process
                    if (email.equalsIgnoreCase("exit")) {
                        System.out.println("\n❌ You have exited the registration process. Goodbye!");
                        return;
                    }

                    // email format is valid
                    if (!InputValidator.isValidEmail(email)) {
                        System.out.print("");
                        System.err.println("❌ Invalid email format. Please try again.");
                        email = null; // Reset email for retry
                    }

                    // email is unique
                    if (!userService.isUniqueEmail(email)) {
                        System.out.print("");
                        System.err.println("❌ Email already exists. Please try again with a different email.");
                        email = null; // Reset email for retry
                    }
                }

        // Password
                while (password == null) {
                    System.out.print("\n🔐 Create a secure password (at least 8 characters, or type 'exit' to quit): ");
                    password = scanner.nextLine();
                    if (password.equalsIgnoreCase("exit")) {
                        System.out.println("\n❌ You have exited the registration process. Goodbye!");
                        return;
                    }
                    if (password.isEmpty() || password.isBlank() || password.length() < 8) {
                        System.out.print("");
                        System.err.println("❌ Password must be at least 8 characters long. Please try again.");
                        password = null; // Reset password for retry
                    }
                }

                // First Name
                while (firstName == null) {
                    System.out.print("👤 What is your first name? (or type 'exit' to quit): ");
                    firstName = scanner.nextLine();
                    if (firstName.equalsIgnoreCase("exit")) {
                        System.out.println("❌ You have exited the registration process. Goodbye!");
                        return;
                    }
                    firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
                    if (!InputValidator.isValidName(firstName)) {
                        System.err.println("❌ First name cannot be blank or invalid. Please try again.");
                        firstName = null; // Reset first name for retry
                    }
                }

                // Middle Name (Optional)
                while (middleName == null) {
                    System.out.print("🖋 Enter your middle name (Optional, or type 'exit' to quit): ");
                    middleName = scanner.nextLine();
                    if (middleName.equalsIgnoreCase("exit")) {
                        System.out.println("❌ You have exited the registration process. Goodbye!");
                        return;
                    }
                }

                // Last Name
                while (lastName == null) {
                    System.out.print("👤 What is your last name? (or type 'exit' to quit): ");
                    lastName = scanner.nextLine();
                    if (lastName.equalsIgnoreCase("exit")) {
                        System.out.println("❌ You have exited the registration process. Goodbye!");
                        return;
                    }
                    lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
                    if (!InputValidator.isValidName(lastName)) {
                        System.err.println("❌ Last name cannot be blank or invalid. Please try again.");
                        lastName = null; // Reset last name for retry
                    }
                }

                // Gender
                while (gender == null) {
                    System.out.print("🌈 Select your gender (Male, Female, Other, or type 'exit' to quit): ");
                    gender = scanner.nextLine();
                    if (gender.equalsIgnoreCase("exit")) {
                        System.out.println("❌ You have exited the registration process. Goodbye!");
                        return;
                    }
                    gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
                    if (!InputValidator.isValidGender(gender)) {
                        System.err.println("❌ Invalid gender selection. Please try again.");
                        gender = null; // Reset gender for retry
                    }
                }

                // Birth Date
                while (birthDate == null) {
                    System.out.print("🎂 Please enter your birth date (yyyy-MM-dd, or type 'exit' to quit): ");
                    String birthDateString = scanner.nextLine();
                    if (birthDateString.equalsIgnoreCase("exit")) {
                        System.out.println("❌ You have exited the registration process. Goodbye!");
                        return;
                    }
                    try {
                        birthDate = LocalDate.parse(birthDateString);
                        if (!InputValidator.isValidBirthDate(birthDate)) {
                            System.err.println("❌ Invalid birth date. Please try again.");
                            birthDate = null; // Reset birth date for retry
                        }
                    } catch (DateTimeParseException e) {
                        System.err.println("❌ Invalid date format. Use yyyy-MM-dd. Please try again.");
                    }
                }

                // Blood Type
                while (bloodType == null) {
                    System.out.print("🩸 Enter your blood type (A+, A-, B+, B-, AB+, AB-, O+, O-): ");
                    bloodType = scanner.nextLine().toUpperCase();
                    if (bloodType.equalsIgnoreCase("exit")) {
                        System.out.println("❌ You have exited the registration process. Goodbye!");
                        return;
                    }
                    if (!InputValidator.isValidBloodType(bloodType)) {
                        System.err.println("❌ Invalid blood type. Please try again.");
                        bloodType = null; // Reset blood type for retry
                    }
                }

                // Weight
                while (weight == 0) {
                    System.out.print("⚖️ Please enter your weight (kg, or type 'exit' to quit): ");
                    String weightString = scanner.nextLine();
                    if (weightString.equalsIgnoreCase("exit")) {
                        System.out.println("❌ You have exited the registration process. Goodbye!");
                        return;
                    }
                    try {
                        weight = Double.parseDouble(weightString);
                        if (!InputValidator.isValidWeight(weight)) {
                            System.err.println("❌ Invalid weight. Please try again.");
                            weight = 0; // Reset weight for retry
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("❌ Invalid weight format. Please try again.");
                    }
                }

                // Contact Number
                while (contactNumber == null) {
                    System.out.print("📱 Enter your contact number (or type 'exit' to quit): ");
                    contactNumber = scanner.nextLine();
                    if (contactNumber.equalsIgnoreCase("exit")) {
                        System.out.println("❌ You have exited the registration process. Goodbye!");
                        return;
                    }
                    if (!InputValidator.isValidContactNumber(contactNumber)) {
                        System.err.println("❌ Invalid contact number. Please try again.");
                        contactNumber = null; // Reset contact number for retry
                    }
                }

                // Register User
                newUser = userService.registerUser(firstName, middleName, lastName, gender, birthDate, email, password, bloodType, weight, contactNumber);
                if (newUser != null) {
                    registrationSuccessful = true;
                }
            } catch (UserServiceException | DateTimeParseException | NumberFormatException e) {
                System.err.println(e.getMessage());
            }
        }

        if (newUser != null) {
            System.out.println("\n🎉 Registration successful!");
            System.out.println("🔑 Remember yor email and password. Welcome to our community!");
        }
    }
}