package ikonek.views;

import ikonek.exceptions.AdminServiceException;
import ikonek.exceptions.BloodDonationServiceException;
import ikonek.exceptions.HospitalServiceException;
import ikonek.models.*;
import ikonek.services.*;
import ikonek.utils.InputValidator;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class AdminMenu {


    public static void displayAdminMenu(Admin admin, AdminService adminService, HospitalService hospitalService, FundraiserService fundraiserService, MonetaryDonationService monetaryDonationService, BloodDonationService bloodDonationService, UserService userService) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Admin Menu (Logged in as: " + admin.getFirstName() + " " + admin.getLastName() + ") ---");
            System.out.println("\nWelcome to the iKonek Admin Dashboard!");
            System.out.println("Manage the system with ease and make every drop count!");

            System.out.println("\nPlease choose an option:");
            System.out.println("1. 🏥 Hospital Management");
            System.out.println("2. 👥 View User List");
            System.out.println("3. 💉 View Blood Donations");
            System.out.println("4. 💰 View Fundraising Initiatives");
            System.out.println("5. 📊 Generate Reports");
            System.out.println("6. 🚪 Logout");

            System.out.print("\nEnter your choice (1-6): ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n🩺 Entering Hospital Management...");
                    manageHospitals(hospitalService);
                    break;
                case 2:
                    System.out.println("\n👤 Viewing the User List...");
                    viewUsers(adminService);
                    break;
                case 3:
                    System.out.println("\n💉 Managing Blood Donations...");
                    manageBloodDonations(bloodDonationService, hospitalService, userService);
                    break;
                case 4:
                    System.out.println("\n💰 Managing Fundraisers...");
                    manageFundraisers(fundraiserService, userService);
                    break;
                case 5:
                    System.out.println("\n📊 Generating Reports...");
                    generateReports(userService, bloodDonationService, monetaryDonationService);
                    break;
                case 6:
                    System.out.println("\n🚪 Logging out... Thank you for your contribution!");
                    break;
                default:
                    System.out.println("\n❌ Invalid choice. Please choose a number between 1 and 6.");
            }
        } while (choice != 6);
    }

    public static void loginAdmin(AdminService adminService, HospitalService hospitalService, FundraiserService fundraiserService, MonetaryDonationService monetaryDonationService, BloodDonationService bloodDonationService, UserService userService) {
        Scanner scanner = new Scanner(System.in);
        Admin admin = null;

        System.out.println("\n=======================================");
        System.out.println("          iKonek Admin Login           ");
        System.out.println("Every Drop Counts, Every Click Matters");
        System.out.println("=======================================\n");

        while (admin == null) {
            String username;
            String password;

            try {
                System.out.print("Enter username (or type '0' to exit): ");
                username = scanner.nextLine();
                if (username.equals("0")) {
                    System.out.println("Exiting login process...");
                    return;
                }
                if (username.isBlank()) {
                    throw new AdminServiceException("⚠️ Username cannot be empty. Please try again.");
                }

                System.out.print("Enter password (or type '0' to exit): ");
                password = scanner.nextLine();
                if (password.equals("0")) {
                    System.out.println("Exiting login process...");
                    return;
                }
                if (password.isBlank()) {
                    throw new AdminServiceException("⚠️ Password cannot be empty. Please try again.");
                }

                admin = adminService.loginAdmin(username, password);
                if (admin == null) {
                    throw new AdminServiceException("❌ Invalid username or password. Please check your credentials and try again.");
                }

            } catch (AdminServiceException e) {
                System.err.println("Error: " + e.getMessage());
                System.out.println("Please try again or type '0' to return to the main menu.");
                System.out.println("--------------------------------------");
            }
        }

        // Success message and redirection to the admin menu
        if (admin != null) {
            System.out.println("\n✅ Login successful! Welcome, " + admin.getUsername() + "!");
            displayAdminMenu(admin, adminService, hospitalService, fundraiserService, monetaryDonationService, bloodDonationService, userService);
        } else {
            System.out.println("Returning to the Main Menu...");
        }
    }

    private static void viewUsers(AdminService adminService) {
        try {
            List<User> users = adminService.getAllUsers();

            if (users.isEmpty()) {
                System.out.println("\n--- User Directory ---");
                System.out.println("There are currently no users registered in the system.");
                return;
            }

            System.out.println("\n===========================================");
            System.out.println("   iKonek Registered Users");
            System.out.println("   Every Drop Counts, Every Click Matters");
            System.out.println("===========================================\n");

            System.out.print("+------+---------------------------------------+--------------------------------+----------------------------+\n");
            System.out.printf("| %-4s | %-37s | %-30s | %-26s |\n", "No.", "Name", "Email", "Contact Number");
            System.out.print("+------+---------------------------------------+--------------------------------+----------------------------+\n");

            // Table rows
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                System.out.printf("| %-4d | %-37s | %-30s | %-26s |\n",
                        i + 1,
                        user.getFirstName() + " " + user.getLastName(),
                        user.getEmail(),
                        user.getContactNumber());
            }
            System.out.print("+------+---------------------------------------+--------------------------------+----------------------------+\n");

            System.out.println("\nEnd of User List.");

        } catch (Exception e) {
            System.err.println("⚠️ Error retrieving users: " + e.getMessage());
        }
    }

    private static void manageBloodDonations(BloodDonationService bloodDonationService, HospitalService hospitalService, UserService userService) {
        Scanner scanner = new Scanner(System.in);
        List<BloodDonation> bloodDonations;
        int choice;
        String status;
        String reason = null;

        try {
            bloodDonations = bloodDonationService.getAllBloodDonations();

            // Inform if no donations are available to manage
            if (bloodDonations.isEmpty()) {
                System.out.println("\nThere are currently no blood donations available to manage.");
                return;
            }

            // Display blood donations
            System.out.println("\n--- Blood Donation Management ---");
            System.out.println("Below are the available blood donations. Please review and select a donation to update its status.\n");

            for (int i = 0; i < bloodDonations.size(); i++) {
                BloodDonation donation = bloodDonations.get(i);
                Hospital hospital = hospitalService.getHospitalById(donation.getHospitalId());
                User user = userService.getUserById(donation.getDonorId());
                System.out.println((i + 1) + ". Donation ID: " + donation.getDonationId());
                System.out.println("    Donor Name: " + user.getFirstName() + " " + user.getLastName());
                System.out.println("    Hospital: " + hospital.getName());
                System.out.println("    Donation Date: " + donation.getDonationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                System.out.println("    Current Status: " + donation.getStatus());
                System.out.println("    Failure Reason: " + (donation.getFailureReason() != null ? donation.getFailureReason() : "N/A"));
                System.out.println("---------------------------------------------------------");
            }

            // User prompt to select a donation to manage
            System.out.print("\nPlease enter the number of the donation you'd like to edit status, or type '0' to return to the previous menu: ");
            String choiceString = scanner.nextLine();
            if (choiceString.equalsIgnoreCase("0")) {
                System.out.println("Returning to the previous menu...");
                return;
            }

            try {
                choice = Integer.parseInt(choiceString);
            } catch (NumberFormatException e) {
                throw new BloodDonationServiceException("Invalid entry. Please enter a valid number.");
            }

            if (choice < 1 || choice > bloodDonations.size()) {
                throw new BloodDonationServiceException("Selection out of range. Please choose a valid donation number.");
            }

            BloodDonation donation = bloodDonations.get(choice - 1);

            // Prompt for updating status
            System.out.print("\nEnter the new status for this donation ('Successful', 'Failed', or 'Pending'): ");
            status = scanner.nextLine();

            // If status is "Failed," prompt for reason
            if (status.equalsIgnoreCase("Failed")) {
                System.out.print("Please enter a reason for the failure: ");
                reason = scanner.nextLine();
            }

            // Update and confirm status update
            donation.setStatus(status);
            donation.setFailureReason(reason);
            bloodDonationService.updateBloodDonation(donation);
            System.out.println("✅ Blood donation status has been successfully updated!");

        } catch (BloodDonationServiceException | NumberFormatException e) {
            System.err.println("⚠️ Error: " + e.getMessage());
        }
    }

    private static void manageFundraisers(FundraiserService fundraiserService, UserService userService) {
        List<FundraisingInitiative> initiatives;

        try {
            initiatives = fundraiserService.getAllFundraisingInitiatives();

            // Message if no initiatives are available
            if (initiatives.isEmpty()) {
                System.out.println("\n🔹 There are currently no active fundraising initiatives to manage.");
                return;
            }

            // Header for Fundraising Initiatives Section
            System.out.println("\n--- Fundraising Initiatives ---");
            System.out.println("Below is the list of active initiatives. Review each one for updates or management.\n");

            for (int i = 0; i < initiatives.size(); i++) {
                FundraisingInitiative initiative = initiatives.get(i);
                User creator = userService.getUserById(initiative.getUserId()); // Get creator's name

                // Display detailed information for each initiative
                System.out.printf("%d. Initiative: %s\n", (i + 1), initiative.getCause());
                System.out.printf("   📝 Description: %s\n", initiative.getShortDescription());
                System.out.printf("   🎯 Target Amount: %.2f PHP\n", initiative.getTargetAmount());
                System.out.printf("   💰 Amount Raised: %.2f PHP\n", initiative.getAmountReceived());
                System.out.printf("   📅 Deadline: %s\n", initiative.getDeadline().toString());
                System.out.printf("   👤 Created By: %s\n", (creator != null ? creator.getFirstName() + " " + creator.getLastName() : "Unknown User"));
                System.out.println("------------------------------------");
            }

        } catch (Exception e) {
            System.err.println("⚠️ Error: Unable to retrieve fundraising initiatives due to: " + e.getMessage());
        }
    }

    private static void generateReports(UserService userService, BloodDonationService bloodDonationService, MonetaryDonationService monetaryDonationService) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // Display report options
            System.out.println("\n--- Generate Reports ---");
            System.out.println("1. Total Number of Registered Users");
            System.out.println("2. Donations by Blood Type");
            System.out.println("3. Total Funds Raised");
            System.out.println("4. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayTotalRegisteredUsers(userService);
                    break;
                case 2:
                    displayDonationsByBloodType(bloodDonationService);
                    break;
                case 3:
                    displayTotalFundsRaised(monetaryDonationService);
                    break;
                case 4:
                    System.out.println("Returning to Admin Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    private static void displayTotalRegisteredUsers(UserService userService) {
        int totalUsers = userService.getTotalRegisteredUsers();
        System.out.println("\nTotal Number of Registered Users: " + totalUsers);
    }

    private static void displayDonationsByBloodType(BloodDonationService bloodDonationService) {
        try{
            bloodDonationService.displayDonationsByBloodType();
        }catch(Exception e){
            System.err.println("Error generating report: " + e.getMessage());

        }
    }

    private static void displayTotalFundsRaised(MonetaryDonationService monetaryDonationService) {
        try{
            monetaryDonationService.displayTotalFundsRaised();
        }catch(Exception e){
            System.err.println("Error generating report: " + e.getMessage());
        }
    }

    private static void manageHospitals(HospitalService hospitalService) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Hospital Management ---");
            System.out.println("1. Add Hospital");
            System.out.println("2. View Available Hospitals");
            System.out.println("3. Update Hospital Information");
            System.out.println("4. Delete Hospital");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createHospital(hospitalService);
                    break;
                case 2:
                    readHospitals(hospitalService);
                    break;
                case 3:
                    updateHospital(hospitalService);
                    break;
                case 4:
                    deleteHospital(hospitalService);
                    break;
                case 5:
                    System.out.println("Returning to Admin Menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private static void createHospital(HospitalService hospitalService) {
        Scanner scanner = new Scanner(System.in);
        String name, city, province, contactNumber;

        while (true) {
            try {
                System.out.println("\n--- Register a New Hospital ---");
                System.out.println("Please enter the details for the new hospital to add it to the iKonek network.\n");

                System.out.print("🏥 Hospital Name (or type 'exit' to cancel): ");
                name = scanner.nextLine().trim();
                if (name.equalsIgnoreCase("exit")) {
                    System.out.println("You have exited the hospital registration process.");
                    break;
                }

                System.out.print("🌆 City (or type 'exit' to cancel): ");
                city = scanner.nextLine().trim();
                if (city.equalsIgnoreCase("exit")) {
                    System.out.println("You have exited the hospital registration process.");
                    break;
                }

                System.out.print("🌍 Province (or type 'exit' to cancel): ");
                province = scanner.nextLine().trim();
                if (province.equalsIgnoreCase("exit")) {
                    System.out.println("You have exited the hospital registration process.");
                    break;
                }

                System.out.print("📞 Contact Number (or type 'exit' to cancel): ");
                contactNumber = scanner.nextLine().trim();
                if (contactNumber.equalsIgnoreCase("exit")) {
                    System.out.println("You have exited the hospital registration process.");
                    break;
                }

                if (!InputValidator.isValidName(name) || !InputValidator.isValidName(city) ||
                        !InputValidator.isValidName(province) || !InputValidator.isValidContactNumber(contactNumber)) {
                    System.out.println("⚠️ Invalid input detected. Please review and ensure all details are correct.");
                    System.out.println("You may type 'exit' at any point to cancel.\n");
                    continue; // Allow the user to retry if input is invalid
                }

                int hospitalId = hospitalService.createHospital(name, city, province, contactNumber);
                if (hospitalId != -1) {
                    System.out.println("\n✅ Hospital registration successful!");
                    break;
                }

            } catch (Exception e) {
                System.err.println("\n🚫 Error: Unable to create hospital - " + e.getMessage());
                System.out.println("You may type 'exit' at any point to cancel.\n");
                continue;
            }
        }
    }

    private static void readHospitals(HospitalService hospitalService) {
        List<Hospital> hospitals = hospitalService.getAllHospitals();

        if (hospitals.isEmpty()) {
            System.out.println("\nNo hospitals found.");
            return;
        }

        int nameColumnWidth = 40;
        int cityColumnWidth = 20;
        int contactNumberColumnWidth = 27;

        System.out.print("+----+----------------------------------------+----------------------+" +
                "----------------------------+\n");
        System.out.printf("| %-2s | %-"+(nameColumnWidth-1)+"s | %-"+(cityColumnWidth-1)+"s | %-"+(contactNumberColumnWidth-1)+"s |\n",
                "No", "Hospital Name", "City", "Contact Number");
        System.out.print("+----+----------------------------------------+----------------------+" +
                "----------------------------+\n");

        for (int i = 0; i < hospitals.size(); i++) {
            Hospital hospital = hospitals.get(i);
            System.out.printf("| %-2d | %-"+(nameColumnWidth-1)+"s | %-"+(cityColumnWidth-1)+"s | %-"+(contactNumberColumnWidth-1)+"s |\n",
                    i + 1,
                    hospital.getName(),
                    hospital.getCity(),
                    hospital.getContactNumber());
        }
        System.out.print("+----+----------------------------------------+----------------------+" +
                "----------------------------+\n");
    }

    private static void updateHospital(HospitalService hospitalService) {
        Scanner scanner = new Scanner(System.in);
        List<Hospital> hospitals = hospitalService.getAllHospitals();
        int choice;
        String name, city, province, contactNumber;

        if (hospitals.isEmpty()) {
            System.out.println("\n❌ No hospitals found to update. Please add hospitals first.");
            return;
        }

        System.out.println("\n--- Hospitals Available for Update ---");
        for (int i = 0; i < hospitals.size(); i++) {
            System.out.println((i + 1) + ". " + hospitals.get(i).getName() + " (Located in: " + hospitals.get(i).getCity() + ")");
        }

        System.out.print("\n📍 Enter the number of the hospital you wish to update or type '0' to exit: ");
        choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 0) {
            System.out.println("\n🚪 Exiting update process. Returning to main menu...");
            return;
        }

        if (choice < 1 || choice > hospitals.size()) {
            System.out.println("❌ Invalid choice. Please enter a valid number between 1 and " + hospitals.size() + " to proceed.");
            return;
        }

        Hospital hospital = hospitals.get(choice - 1);
        System.out.println("\n🔧 --- Updating Hospital: " + hospital.getName() + " ---\n");
        System.out.println("Current details for " + hospital.getName() + ":");

        printHospitalTable(hospital);

        try {
            System.out.print("\n🖊️ Enter new name (leave blank if no change): ");
            name = scanner.nextLine();
            System.out.print("🖊️ Enter new city (leave blank if no change): ");
            city = scanner.nextLine();
            System.out.print("🖊️ Enter new province (leave blank if no change): ");
            province = scanner.nextLine();
            System.out.print("🖊️ Enter new contact number (leave blank if no change): ");
            contactNumber = scanner.nextLine();

            // Update hospital attributes if user input is not empty
            if (!name.isEmpty()) {
                hospital.setName(name);
            }
            if (!city.isEmpty()) {
                hospital.setCity(city);
            }
            if (!province.isEmpty()) {
                hospital.setProvince(province);
            }
            if (!contactNumber.isEmpty()) {
                hospital.setContactNumber(contactNumber);
            }

            if (!InputValidator.isValidName(hospital.getName()) || !InputValidator.isValidName(hospital.getCity()) ||
                    !InputValidator.isValidName(hospital.getProvince()) || !InputValidator.isValidContactNumber(hospital.getContactNumber())) {
                throw new HospitalServiceException("❌ Invalid input. Please check your details.");
            }

            if (hospitalService.updateHospital(hospital)) {
                System.out.println("\n✅ Hospital updated successfully!");
            } else {
                throw new HospitalServiceException("❌ Failed to update hospital. Please check the entered information.");
            }

        } catch (HospitalServiceException | NumberFormatException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private static void printHospitalTable(Hospital hospital) {
        int nameColumnWidth = 43;
        int cityColumnWidth = 24;
        int provinceColumnWidth = 24;
        int contactNumberColumnWidth = 24;

        System.out.println("+-----+--------------------------------------------+-------------------------+-------------------------+-------------------------+");
        System.out.printf("| %-3s | %-"+(nameColumnWidth-1)+"s | %-"+(cityColumnWidth-1)+"s | %-"+(provinceColumnWidth-1)+"s | %-"+(contactNumberColumnWidth-1)+"s |\n",
                "ID", "Name", "City", "Province", "Contact Number");
        System.out.println("+-----+--------------------------------------------+-------------------------+-------------------------+-------------------------+");

        System.out.printf("| %-3s | %-"+(nameColumnWidth-1)+"s | %-"+(cityColumnWidth-1)+"s | %-"+(provinceColumnWidth-1)+"s | %-"+(contactNumberColumnWidth-1)+"s |\n",
                hospital.getHospitalId(), hospital.getName(), hospital.getCity(), hospital.getProvince(), hospital.getContactNumber());
        System.out.println("+-----+--------------------------------------------+-------------------------+-------------------------+-------------------------+");
    }

    private static void deleteHospital(HospitalService hospitalService) {
        Scanner scanner = new Scanner(System.in);
        List<Hospital> hospitals = hospitalService.getAllHospitals();
        int choice;

        if (hospitals.isEmpty()) {
            System.out.println("\n❌ No hospitals available to delete. Please add a hospital first.");
            return;
        }

        System.out.println("\n--- 📍 List of Hospitals ---");
        for (int i = 0; i < hospitals.size(); i++) {
            System.out.printf("  %d. %s\n", (i + 1), hospitals.get(i).getName());
        }

        System.out.print("\nPlease select the hospital you wish to delete by entering the corresponding number: ");
        choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > hospitals.size()) {
            System.out.println("❌ Invalid selection. Please choose a number between 1 and " + hospitals.size() + ".");
            return;
        }

        Hospital hospital = hospitals.get(choice - 1);
        System.out.println("\n🔴 Deleting a Hospital");
        System.out.printf("You have selected: %s\n", hospital.getName());
        System.out.print("\nAre you sure you want to permanently delete this hospital? (y/n): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("y")) {
            hospitalService.deleteHospital(hospital.getHospitalId());
            System.out.println("\n✅ Hospital successfully deleted. Thank you for managing the system with iKonek.");
        } else {
            System.out.println("\n❌ Hospital deletion has been cancelled. No changes were made.");
        }
    }

}
