package ikonek;

import ikonek.dao.*;
import ikonek.services.*;
import ikonek.utils.PasswordHasher;
import ikonek.views.MainMenu;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // initialize DAOs
        UserDao userDao = new UserDao();
        AdminDao adminDao = new AdminDao();
        HospitalDao hospitalDao = new HospitalDao();
        FundraisingInitiativeDao fundraisingInitiativeDao = new FundraisingInitiativeDao();
        MonetaryDonationDao monetaryDonationDao = new MonetaryDonationDao();
        BloodDonationDao bloodDonationDao = new BloodDonationDao();

        // initialize services
        PasswordHasher passwordHasher = new PasswordHasher();
        UserService userService = new UserService(userDao, passwordHasher);
        AdminService adminService = new AdminService(adminDao, passwordHasher, userDao);
        HospitalService hospitalService = new HospitalService(hospitalDao);
        FundraiserService fundraiserService = new FundraiserService(userDao, fundraisingInitiativeDao);
        MonetaryDonationService monetaryDonationService = new MonetaryDonationService(monetaryDonationDao, fundraiserService, userDao, fundraisingInitiativeDao);
        BloodDonationService bloodDonationService = new BloodDonationService(bloodDonationDao, userDao, hospitalDao);

        // initialize MainMenu
        MainMenu mainMenu = new MainMenu(userService, adminService, hospitalService, fundraiserService, monetaryDonationService, bloodDonationService);

        int choice;
            do {
                choice = mainMenu.displayMenu();
                mainMenu.handleMenuChoice(choice, userService, adminService, hospitalService, fundraiserService, monetaryDonationService, bloodDonationService);
            } while (choice != 4);

        scanner.close();
    }
}