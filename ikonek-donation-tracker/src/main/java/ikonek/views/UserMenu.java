package ikonek.views;

import ikonek.dao.FundraisingInitiativeDao;
import ikonek.exceptions.*;
import ikonek.models.*;
import ikonek.services.*;
import ikonek.utils.InputValidator;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class UserMenu {

    public static void displayUserMenu(User user, UserService userService, BloodDonationService bloodDonationService, FundraiserService fundraiserService, MonetaryDonationService monetaryDonationService, HospitalService hospitalService) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Display header and user info
            System.out.println("\n=========================");
            System.out.println("iKonek: Every Drop Counts");
            System.out.println("Logged in as: " + user.getFirstName() + " " + user.getLastName());
            System.out.println("=========================");
            System.out.println("Please select an option from the menu below:");

            // Display user menu options in a more logical order
            System.out.println("\n1. View/Update My Profile");
            System.out.println("2. View My Donation History");
            System.out.println("3. Schedule a New Blood Donation");
            System.out.println("4. Cancel a Pending Blood Donation");
            System.out.println("5. Create a New Fundraising Initiative");
            System.out.println("6. View My Fundraising Initiatives History");
            System.out.println("7. Donate to a Fundraising Initiative");
            System.out.println("8. Logout\n");

            System.out.print("Please enter the number corresponding to your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    viewUpdateProfile(user, userService);
                    break;
                case 2:
                    viewDonationHistory(user, bloodDonationService, hospitalService, monetaryDonationService);
                    break;
                case 3:
                    scheduleBloodDonation(user, bloodDonationService, hospitalService, userService);
                    break;
                case 4:
                    cancelBloodDonation(user, bloodDonationService);
                    break;
                case 5:
                    createFundraisingInitiative(user, fundraiserService);
                    break;
                case 6:
                    viewFundraisingHistory(user, fundraiserService, monetaryDonationService, userService);
                    break;
                case 7:
                    donateToFundraisingInitiative(user, fundraiserService, monetaryDonationService, userService, hospitalService);
                    break;
                case 8:
                    System.out.println("\nYou have successfully logged out. Thank you for using iKonek!");
                    break;
                default:
                    System.out.println("\n⚠️ Invalid choice! Please enter a valid number from the menu.");
            }
        } while (choice != 8);
    }


    public static void loginUser(HospitalService hospitalService, UserService userService, BloodDonationService bloodDonationService, FundraiserService fundraiserService, MonetaryDonationService monetaryDonationService) {
        Scanner scanner = new Scanner(System.in);
        User user = null;
        while (user == null) {
            String email;
            String password;

            try {
                System.out.println("\nWelcome to iKonek: Every Drop Counts, Every Click Matters!");
                System.out.println("Please log in to continue supporting life-saving causes.");

                System.out.print("📧 Email Address: ");
                email = scanner.nextLine();

                System.out.print("🔒 Password: ");
                password = scanner.nextLine();

                user = userService.loginUser(email, password);
                if (user == null) {
                    throw new UserServiceException("Oops! The email or password you entered is incorrect.");
                }

            } catch (UserServiceException e) {
                System.err.print("❌ " + e.getMessage() + "\nPress Enter to try again or type 'exit' to return to the main menu: ");
                if (scanner.nextLine().equalsIgnoreCase("exit")) {
                    break;
                }
            }
        }
        if (user != null) {
            System.out.println("✅ Login successful! Welcome back, " + user.getFirstName() + ".");
            displayUserMenu(user, userService, bloodDonationService, fundraiserService, monetaryDonationService, hospitalService);
        } else {
            System.out.println("Returning to the main menu. We hope to see you again soon!");
        }
    }

    private static void viewUpdateProfile(User user, UserService userService) {
        Scanner scanner = new Scanner(System.in);
        boolean updateSuccessful = false;
        User updatedUser = null;
        String updatedPassword = null;

        // Display user profile information
        System.out.println("\n--- Your Profile ---");
        System.out.println("First Name        : " + user.getFirstName());
        System.out.println("Middle Name       : " + user.getMiddleName());
        System.out.println("Last Name         : " + user.getLastName());
        System.out.println("Gender            : " + user.getGender());
        System.out.println("Birth Date        : " + user.getBirthDate());
        System.out.println("Weight (kg)       : " + user.getWeight());
        System.out.println("Blood Type        : " + user.getBloodType());
        System.out.println("Email             : " + user.getEmail());
        System.out.println("Contact Number    : " + user.getContactNumber());

        // Prompt to update profile
        System.out.print("\nWould you like to update your profile details? (y/n): ");
        String updateChoice = scanner.nextLine();
        if (updateChoice.equalsIgnoreCase("y")) {
            while (!updateSuccessful) {
                try {
                    System.out.print("Enter your first name (leave blank if no change): ");
                    String firstName = scanner.nextLine();
                    if (!firstName.isEmpty()) {
                        if (!InputValidator.isValidName(firstName)) {
                            throw new UserServiceException("First name is invalid. Please enter letters only.");
                        }
                        firstName = Arrays.stream(firstName.split(" "))
                                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                                .collect(Collectors.joining(" "));
                        user.setFirstName(firstName);
                    }

                    System.out.print("Enter your middle name (leave blank if no change): ");
                    String middleName = scanner.nextLine();
                    if (!middleName.isEmpty()) {
                        user.setMiddleName(middleName);
                    }

                    System.out.print("Enter your last name (leave blank if no change): ");
                    String lastName = scanner.nextLine();
                    if (!lastName.isEmpty()) {
                        if (!InputValidator.isValidName(lastName)) {
                            throw new UserServiceException("Last name is invalid. Please enter letters only.");
                        }
                        lastName = Arrays.stream(lastName.split(" "))
                                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                                .collect(Collectors.joining(" "));
                        user.setLastName(lastName);
                    }

                    System.out.print("Enter your gender (e.g., Male, Female, Other) (leave blank if no change): ");
                    String gender = scanner.nextLine();
                    if (!gender.isEmpty()) {
                        if (!InputValidator.isValidGender(gender)) {
                            throw new UserServiceException("Gender is invalid. Please enter a valid gender.");
                        }
                        gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
                        user.setGender(gender);
                    }

                    System.out.print("Enter your birth date (YYYY-MM-DD) (leave blank if no change): ");
                    String birthDateString = scanner.nextLine();
                    if (!birthDateString.isEmpty()) {
                        LocalDate birthDate = LocalDate.parse(birthDateString);
                        if (!InputValidator.isValidBirthDate(birthDate)) {
                            throw new UserServiceException("Invalid birth date. Please enter a valid date.");
                        }
                        user.setBirthDate(birthDate);
                    }

                    System.out.print("Enter your weight in kg (leave blank if no change): ");
                    String weightString = scanner.nextLine();
                    if (!weightString.isEmpty()) {
                        double weight = Double.parseDouble(weightString);
                        if (!InputValidator.isValidWeight(weight)) {
                            throw new UserServiceException("Invalid weight. Please enter a positive number.");
                        }
                        user.setWeight(weight);
                    }

                    System.out.print("Enter your contact number (leave blank if no change): ");
                    String contactNumber = scanner.nextLine();
                    if (!contactNumber.isEmpty()) {
                        if (!InputValidator.isValidContactNumber(contactNumber)) {
                            throw new UserServiceException("Invalid contact number. Please enter a valid phone number.");
                        }
                        user.setContactNumber(contactNumber);
                    }

                    System.out.print("Enter a new password (optional, at least 8 characters) (leave blank if no change): ");
                    updatedPassword = scanner.nextLine();
                    if (!updatedPassword.isEmpty()) {
                        if (!InputValidator.isValidPassword(updatedPassword)) {
                            throw new UserServiceException("Invalid password. Password must be at least 8 characters long.");
                        }
                        updatedUser = userService.updateUser(user, updatedPassword);
                        if (updatedUser != null) {
                            updateSuccessful = true;
                        } else {
                            throw new UserServiceException("Failed to update user information. Please try again.");
                        }
                    } else {
                        updatedUser = userService.updateUser(user); // If password is not changed
                        if (updatedUser != null) {
                            updateSuccessful = true;
                        } else {
                            throw new UserServiceException("Failed to update user information. Please try again.");
                        }
                    }

                } catch (UserServiceException | DateTimeParseException | NumberFormatException e) {
                    System.err.print("❌ Error: " + e.getMessage() + "\nPress Enter to try again or type 'exit' to cancel: ");
                    if (scanner.nextLine().equalsIgnoreCase("exit")) {
                        break;
                    }
                }
            }

            if (updateSuccessful) {
                System.out.println("\n✅ Profile updated successfully! Your information has been saved.");
            } else {
                System.out.println("\n⚠️ Profile update was canceled. Returning to the main menu.");
            }
        } else {
            System.out.println("\n⚠️ No changes made. Returning to the main menu.");
        }
    }

    private static void scheduleBloodDonation(User user, BloodDonationService bloodDonationService, HospitalService hospitalService, UserService userService) {
        Scanner scanner = new Scanner(System.in);

        try {
            // 1. Eligibility Check
            System.out.println("🩸 Eligibility Notice: To donate blood, you must be between the ages of 16 and 65, and weigh at least 50 kg.");

            if (!bloodDonationService.isEligibleForBloodDonation(user.getUserId())) {
                System.out.println("Thank you for your interest in donating! However, based on your current details, you are not eligible to donate blood at this time. Please check our guidelines or contact support for further assistance.");
                return;
            }

            // 2. Display Hospitals
            List<Hospital> hospitals = hospitalService.getAllHospitals();
            if (hospitals.isEmpty()) {
                throw new BloodDonationServiceException("We couldn't find any registered hospitals at the moment. Please try again later or contact support.");
            }

            System.out.println("\n🩸 Welcome to iKonek: Every Drop Counts, Every Click Matters 🩸");
            System.out.println("Choose a hospital near you for your blood donation appointment:");

            // Display hospitals in a table format
            System.out.printf("+----+------------------------------+----------------+----------------+\n");
            System.out.printf("| %-2s | %-28s | %-14s | %-14s |\n", "No", "Hospital Name", "City", "Province");
            System.out.printf("+----+------------------------------+----------------+----------------+\n");

            for (int i = 0; i < hospitals.size(); i++) {
                Hospital hospital = hospitals.get(i);
                System.out.printf("| %-2d | %-28s | %-14s | %-14s |\n", i + 1, hospital.getName(), hospital.getCity(), hospital.getProvince());
            }

            System.out.printf("+----+------------------------------+----------------+----------------+\n");

            // 3. Select Hospital (Loop until valid input)
            int hospitalChoice;
            while (true) {
                System.out.print("\nEnter the number corresponding to your chosen hospital, or type '0' to cancel: ");
                String input = scanner.nextLine();

                if (input.equals("0") || input.equalsIgnoreCase("cancel")) {
                    System.out.println("Cancelling appointment process and returning to the main menu.");
                    return;
                }

                try {
                    hospitalChoice = Integer.parseInt(input);
                    if (hospitalChoice >= 1 && hospitalChoice <= hospitals.size()) {
                        break;
                    }
                    System.out.println("Invalid choice. Please enter a number between 1 and " + hospitals.size() + " to proceed.");
                } catch (NumberFormatException e) {
                    System.out.println("Input error: Please use numbers only.");
                }
            }

            Hospital selectedHospital = hospitals.get(hospitalChoice - 1);
            System.out.println("✅ You have selected: " + selectedHospital.getName() + " located in " + selectedHospital.getCity() + ", " + selectedHospital.getProvince());

            // 4. Enter Donation Date (Loop until valid date input)
            LocalDate donationDate;
            while (true) {
                System.out.print("\nPlease enter your preferred donation date (format: YYYY-MM-DD), or type 'cancel' to exit: ");
                String dateString = scanner.nextLine();

                if (dateString.equalsIgnoreCase("cancel")) {
                    System.out.println("Cancelling appointment process and returning to the main menu.");
                    return;
                }

                try {
                    donationDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
                    if (donationDate.isBefore(LocalDate.now())) {
                        System.out.println("Oops! That date is in the past. Please select a date in the future.");
                    } else {
                        break;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
                }
            }

            // 5. Create Blood Donation (Pending status)
            int donationId = bloodDonationService.createBloodDonation(user.getUserId(), selectedHospital.getHospitalId(), donationDate);
            if (donationId == -1) {
                throw new BloodDonationServiceException("We encountered an issue scheduling your blood donation. Please try again or reach out for support.");
            }

            // 6. Generate and Display Donation Ticket
            BloodDonation donation = bloodDonationService.getBloodDonationById(donationId);
            System.out.println("🎟️ Your blood donation has been successfully scheduled!");
            DonationTicketView.displayDonationTicket(donation, hospitalService, userService);

        } catch (BloodDonationServiceException | IndexOutOfBoundsException e) {
            System.err.println("Error: " + e.getMessage() + "\nIf the issue persists, please contact iKonek support.");
        } catch (MonetaryDonationServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private static void viewDonationHistory(User user, BloodDonationService bloodDonationService, HospitalService hospitalService, MonetaryDonationService monetaryDonationService) {
        try {
            // Display Blood Donation History
            List<BloodDonation> donations = bloodDonationService.getBloodDonationsByUserId(user.getUserId());
            if (!donations.isEmpty()) {
                System.out.println("\n💉 --- Your Blood Donation History --- 💉");
                System.out.print("+----+---------------------+-----------------------+--------+----------------------------------+\n");
                System.out.printf("| %-2s | %-19s | %-21s | %-6s | %-34s |\n", "No", "Donation Date", "Hospital", "Status", "Failure Reason (if applicable)");
                System.out.print("+----+---------------------+-----------------------+--------+----------------------------------+\n");

                int counter = 1;
                for (BloodDonation donation : donations) {
                    Hospital hospital = hospitalService.getHospitalById(donation.getHospitalId());
                    String hospitalName = (hospital != null) ? hospital.getName() : "Unknown Hospital";
                    System.out.printf("| %-2d | %-19s | %-21s | %-6s | %-34s |\n",
                            counter++,
                            donation.getDonationDate(),
                            hospitalName,
                            donation.getStatus(),
                            donation.getFailureReason() != null ? donation.getFailureReason() : "N/A");
                }
                System.out.print("+----+---------------------+-----------------------+--------+----------------------------------+\n");
            } else {
                System.out.println("\n💉 You have no blood donation history yet.");
            }

            // Display Monetary Donation History
            FundraisingInitiativeDao fundraiserService = new FundraisingInitiativeDao();
            List<MonetaryDonation> monetaryDonations = monetaryDonationService.getMonetaryDonationByUserId(user.getUserId());
            if (!monetaryDonations.isEmpty()) {
                System.out.println("\n💵 --- Your Monetary Donation History --- 💵");
                System.out.print("+----+-----------------------------+----------+-------------------------+\n");
                System.out.printf("| %-2s | %-27s | %-8s | %-23s |\n", "No", "Initiative", "Amount", "Date");
                System.out.print("+----+-----------------------------+----------+-------------------------+\n");

                int counter = 1;
                for (MonetaryDonation donation : monetaryDonations) {
                    FundraisingInitiative initiative = fundraiserService.getFundraisingInitiativeById(donation.getFundraisingId());
                    String initiativeName = (initiative != null) ? initiative.getCause() : "Unknown Initiative";
                    System.out.printf("| %-2d | %-27s | %-8.2f | %-23s |\n",
                            counter++,
                            initiativeName,
                            donation.getDonationAmount(),
                            donation.getDonationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
                System.out.print("+----+-----------------------------+----------+-------------------------+\n");
            } else {
                System.out.println("\n💵 You have no monetary donation history yet.");
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error viewing donation history: " + e.getMessage());
        }
    }

    private static void createFundraisingInitiative(User user, FundraiserService fundraiserService) {
        Scanner scanner = new Scanner(System.in);
        boolean initiativeCreated = false;

        while (!initiativeCreated) {
            try {
                // Cause Input
                String cause;
                while (true) {
                    System.out.print("\n🌟 Please enter the main *cause* for your fundraising initiative: ");
                    cause = scanner.nextLine();
                    if (!cause.trim().isEmpty()) break;
                    System.out.println("⚠️ Cause cannot be empty. Kindly provide a valid cause for this initiative.");
                }

                // Short Description Input
                String shortDescription;
                while (true) {
                    System.out.print("\n📝 Provide a *short description* to give more details about this initiative: ");
                    shortDescription = scanner.nextLine();
                    if (!shortDescription.trim().isEmpty()) break;
                    System.out.println("⚠️ Description cannot be empty. Please give a brief summary of the initiative.");
                }

                // Target Amount Input
                double targetAmount;
                while (true) {
                    System.out.print("\n💰 Enter your *target amount* (PHP): ");
                    try {
                        targetAmount = scanner.nextDouble();
                        if (targetAmount > 0) break;
                        System.out.println("⚠️ Target amount must be a positive value. Please re-enter the amount.");
                    } catch (InputMismatchException e) {
                        System.out.println("⚠️ Invalid amount. Please enter a valid number for the target amount.");
                        scanner.nextLine(); // Clear invalid input
                    }
                }

                // Deadline Input
                LocalDate deadline;
                while (true) {
                    System.out.print("\n📅 Set a *deadline* for your initiative (yyyy-MM-dd): ");
                    String deadlineString = scanner.next();
                    try {
                        deadline = LocalDate.parse(deadlineString, DateTimeFormatter.ISO_DATE);
                        if (!deadline.isBefore(LocalDate.now())) break;
                        System.out.println("⚠️ Deadline cannot be a past date. Please choose a future date.");
                    } catch (DateTimeParseException e) {
                        System.out.println("⚠️ Invalid date format. Please enter the date in the format yyyy-MM-dd.");
                    }
                }

                // Create Fundraising Initiative
                int initiativeId = fundraiserService.createFundraisingInitiative(
                        user.getUserId(), cause, targetAmount, shortDescription, deadline.toString());
                if (initiativeId != -1) {
                    initiativeCreated = true;
                    System.out.println("\n🎉 Congratulations! Your fundraising initiative has been created successfully!");
                    System.out.println("📢 Cause: " + cause + " | Description: " + shortDescription);
                    System.out.println("📅 Deadline: " + deadline + " | Target Amount: PHP " + targetAmount);
                }

            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage() + ". You may try again or type 'exit' to quit.");
                if (scanner.nextLine().equalsIgnoreCase("exit")) {
                    break;
                }
            } finally {
                scanner.nextLine(); // Consume remaining newline
            }
        }
    }

    private static void donateToFundraisingInitiative(User user, FundraiserService fundraiserService, MonetaryDonationService monetaryDonationService, UserService userService, HospitalService hospitalService) {
        Scanner scanner = new Scanner(System.in);
        List<FundraisingInitiative> initiatives = new ArrayList<>();
        int choice;
        double donationAmount = 0;

        try {
            initiatives = fundraiserService.getAllFundraisingInitiatives();

            // Remove initiatives that met the deadline and target amount
            initiatives.removeIf(i -> i.getDeadline().isBefore(LocalDate.now()) && i.getAmountReceived() >= i.getTargetAmount());

            if (initiatives.isEmpty()) {
                System.out.println("\n🚫 No active fundraising initiatives available at the moment. Thank you for your willingness to support.");
                return;
            }

            System.out.println("\n🌟 --- Active Fundraising Initiatives --- 🌟");
            for (int i = 0; i < initiatives.size(); i++) {
                FundraisingInitiative initiative = initiatives.get(i);
                System.out.println((i + 1) + ". Cause: " + initiative.getCause() + " | " + initiative.getShortDescription());
                System.out.println("   🎯 Target Amount: " + initiative.getTargetAmount());
                System.out.println("   💰 Amount Raised: " + initiative.getAmountReceived());
                System.out.println("   📅 Deadline:      " + initiative.getDeadline());
                System.out.println("------------------------------------");
            }

            while (true) {
                System.out.print("\nEnter the number of the initiative you'd like to support, or type 'cancel' to exit: ");
                String choiceString = scanner.nextLine();

                if (choiceString.equalsIgnoreCase("cancel")) {
                    System.out.println("\nReturning to the main menu. Thank you for visiting the donation section!");
                    return;
                }

                try {
                    choice = Integer.parseInt(choiceString);
                    if (choice < 1 || choice > initiatives.size()) {
                        System.out.println("⚠️ Invalid choice. Please select a valid initiative number.");
                        continue;
                    }
                    break; // Valid choice, exit loop
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid input. Please enter a valid number or type 'cancel' to exit.");
                }
            }

            while (donationAmount <= 0) {
                System.out.print("\nEnter your donation amount (greater than 0) or type '0' to cancel: ");
                try {
                    donationAmount = scanner.nextDouble();
                    if (donationAmount == 0) {
                        System.out.println("\nYou’ve chosen to cancel your donation. Returning to the main menu.");
                        return;
                    } else if (donationAmount < 0) {
                        System.out.println("⚠️ Donation amount must be positive. Please enter an amount greater than 0.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("⚠️ Invalid input. Please enter a valid numeric amount.");
                }
                scanner.nextLine();
            }

            // Simulate payment processing
            System.out.println("\n💳 Processing your donation...");
            System.out.println("✅ Payment successful! Thank you for making a difference.");

            // Record donation
            MonetaryDonationImpl donation = new MonetaryDonationImpl(user.getUserId(), initiatives.get(choice - 1).getFundraisingId(), donationAmount);
            int donationId = monetaryDonationService.createMonetaryDonation(donation.getDonorId(), donation.getFundraisingId(), donation.getDonationAmount());
            if (donationId == -1){
                throw new MonetaryDonationServiceException("Failed to record donation.");
            }
            donation.setDonationId(donationId);
            donation.setFundraiserService(fundraiserService);

            // Process donation and display donation ticket
            monetaryDonationService.processDonation(donation);
            DonationTicketView.displayDonationTicket(donation, hospitalService, userService);

            // Refresh the list of initiatives after donation
            initiatives = fundraiserService.getAllFundraisingInitiatives();

            // Display updated initiatives
            System.out.println("\n🌟 --- Updated Fundraising Initiatives --- 🌟");
            for (int i = 0; i < initiatives.size(); i++) {
                FundraisingInitiative initiative = initiatives.get(i);
                System.out.println((i + 1) + ". Cause: " + initiative.getCause() + " | " + initiative.getShortDescription());
                System.out.println("   🎯 Target Amount: " + initiative.getTargetAmount());
                System.out.println("   💰 Amount Raised: " + initiative.getAmountReceived());
                System.out.println("------------------------------------");
            }
        } catch (MonetaryDonationServiceException | InputMismatchException | NumberFormatException e) {
            System.err.println("⚠️ Error: " + e.getMessage());
        }
    }

    private static void viewFundraisingHistory(User user, FundraiserService fundraiserService, MonetaryDonationService monetaryDonationService, UserService userService) {
        Scanner scanner = new Scanner(System.in);
        try {
            List<FundraisingInitiative> initiatives = fundraiserService.getAllFundraisingInitiatives();

            // Filter initiatives by userId
            initiatives.removeIf(i -> i.getUserId() != user.getUserId());

            if (initiatives.isEmpty()) {
                System.out.println("\n🔍 No Fundraising Initiatives Found");
                System.out.println("You haven't created any fundraising initiatives yet.");
                return;
            }

            System.out.println("\n📈 Fundraising Initiatives Overview");
            System.out.println("Below are the fundraising initiatives you've created. Select an initiative to view donation history.");

            // Display fundraising initiatives and prompt user for input
            for (int i = 0; i < initiatives.size(); i++) {
                FundraisingInitiative initiative = initiatives.get(i);
                System.out.println("\n-----------------------------------");
                System.out.println("Initiative #" + (i + 1));
                System.out.println("Cause: " + initiative.getCause());
                System.out.println("Description: " + initiative.getShortDescription());
                System.out.println("Target Amount: PHP " + initiative.getTargetAmount());
                System.out.println("Deadline: " + initiative.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                System.out.println("Amount Raised: PHP " + initiative.getAmountReceived());
                System.out.println("-----------------------------------");
            }

            int choice;
            while (true) {
                System.out.print("\nEnter the initiative number to view its donation history (or type '0' to go back): ");
                try {
                    choice = scanner.nextInt();
                    if (choice == 0) {
                        System.out.println("Returning to the main menu.");
                        return;
                    }
                    if (choice >= 1 && choice <= initiatives.size()) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please select a number from 1 to " + initiatives.size() + ".");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            FundraisingInitiative selectedInitiative = initiatives.get(choice - 1);
            System.out.println("\n--- Donation History for \"" + selectedInitiative.getCause() + "\" ---");

            // Retrieve and display monetary donation history for the selected initiative
            List<MonetaryDonation> donations = monetaryDonationService.getMonetaryDonationsByFundraisingId(selectedInitiative.getFundraisingId());
            if (donations.isEmpty()) {
                System.out.println("No donations have been made for this initiative yet.");
            } else {
                for (MonetaryDonation donation : donations) {
                    User donor = userService.getUserById(donation.getDonorId());
                    String donorName = (donor != null) ? donor.getFirstName() + " "  + donor.getLastName() : "Anonymous Donor";
                    System.out.println("    Donor: " + donorName);
                    System.out.println("    Amount Donated: PHP " + donation.getDonationAmount());
                    System.out.println("    Date: " + donation.getDonationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    System.out.println("  -----------------------------");
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error viewing fundraising history: " + e.getMessage());
        }
    }

    private static void cancelBloodDonation(User user, BloodDonationService bloodDonationService) {
        Scanner scanner = new Scanner(System.in);
        List<BloodDonation> pendingDonations;
        int choice;

        try {
            pendingDonations = bloodDonationService.getPendingBloodDonationsByUserId(user.getUserId());

            if (pendingDonations.isEmpty()) {
                System.out.println("\n📋 You currently have no pending blood donations to cancel.");
                return;
            }

            System.out.println("\n--- Pending Blood Donations ---");
            System.out.println("Below is a list of your scheduled blood donations. You can cancel a specific donation by selecting its number.\n(Type '0' or 'exit' at any time to go back.)");

            // Display pending donations
            for (int i = 0; i < pendingDonations.size(); i++) {
                BloodDonation donation = pendingDonations.get(i);
                System.out.printf("%d. Donation ID: %s, Scheduled Date: %s\n",
                        i + 1,
                        donation.getDonationId(),
                        donation.getDonationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

            System.out.print("\nEnter the number of the donation you wish to cancel: ");
            String choiceString = scanner.nextLine().trim();

            if (choiceString.equalsIgnoreCase("exit") || choiceString.equals("0")) {
                System.out.println("Returning to the main menu.");
                return;
            }

            // Validate input
            try {
                choice = Integer.parseInt(choiceString);
            } catch (NumberFormatException e) {
                throw new BloodDonationServiceException("Invalid choice. Please enter a valid number from the list.");
            }

            if (choice < 1 || choice > pendingDonations.size()) {
                throw new BloodDonationServiceException("Invalid selection. Please choose a donation from the list.");
            }

            BloodDonation donationToCancel = pendingDonations.get(choice - 1);
            System.out.print("\nAre you sure you want to cancel the donation scheduled on " +
                    donationToCancel.getDonationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                    "? (Type 'yes' to confirm or 'no' to go back): ");
            String confirmation = scanner.nextLine().trim();

            if (confirmation.equalsIgnoreCase("no")) {
                System.out.println("Cancellation process aborted. Returning to the main menu.");
                return;
            }

            if (confirmation.equalsIgnoreCase("yes")) {
                if (!bloodDonationService.cancelBloodDonation(donationToCancel.getDonationId())) {
                    throw new BloodDonationServiceException("Failed to cancel the blood donation. Please contact support if this issue persists.");
                }
                System.out.println("\n✅ Blood donation cancelled successfully!");
            } else {
                System.out.println("Invalid input. Returning to the main menu.");
            }

        } catch (BloodDonationServiceException e) {
            System.err.println("⚠️ Error: " + e.getMessage());
        }
    }

}