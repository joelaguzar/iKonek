package com.ikonek.services;

import com.ikonek.data.UserData;
import com.ikonek.entities.User;
import com.ikonek.utils.InputValidation;
import com.ikonek.utils.PasswordHashing;
import com.ikonek.data.UserData.UserNotFoundException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class UserServices {
    private final UserData userData;
    private final InputValidation inputValidation;
    private final PasswordHashing passwordHashing;

    public UserServices(UserData userData, InputValidation inputValidation, PasswordHashing passwordHashing) {
        this.userData = userData;
        this.inputValidation = inputValidation;
        this.passwordHashing = passwordHashing;
    }

    public void registerUser(String email, String password) throws SQLException {
        if (!inputValidation.isValidEmail(email) || !inputValidation.isValidPassword(password)) {
            System.out.println("Invalid email or password.");
            return;
        }

        try {
            if (userData.getUserByEmail(email) != null) {
                System.out.println("Email already exists. Please choose a different email.");
                return;
            }
        } catch (UserNotFoundException e) {
            // Email doesn't exist, so continue
        } catch (SQLException e) {
            System.err.println("Error checking for duplicate email: " + e.getMessage());
            throw e;
        }

        String hashedPassword = passwordHashing.hashPassword(password);

        try {
            userData.insertEmailPassword(email, hashedPassword);
        } catch (SQLException e) {
            System.err.println("Error temporarily storing email/password: " + e.getMessage());
            throw e;
        }

        System.out.println("Email and password accepted. Please provide remaining details.");
        completeRegistration(email, hashedPassword);
    }

    private void completeRegistration(String email, String hashedPassword) {
        try (Scanner scanner = new Scanner(System.in)) {
            String firstName, middleName, lastName, gender, birthDate, bloodType, weight, contactNumber, barangay, municipality, province, region;
            LocalDate birthDateObj = null;

            System.out.print("Enter first name: ");
            firstName = scanner.nextLine();

            System.out.print("Enter middle name: ");
            middleName = scanner.nextLine();

            System.out.print("Enter last name: ");
            lastName = scanner.nextLine();

            System.out.print("Enter gender (Male/Female/Other): ");
            gender = scanner.nextLine();

            System.out.print("Enter birth date (YYYY-MM-DD): ");
            birthDate = scanner.nextLine();

            if (inputValidation.isValidDate(birthDate)) {
                birthDateObj = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } else {
                System.out.println("Invalid Date!");
                return;
            }

            System.out.print("Enter blood type: ");
            bloodType = scanner.nextLine();

            System.out.print("Enter weight (kg): ");
            weight = scanner.nextLine();

            double weightValue;
            try {
                weightValue = Double.parseDouble(weight);
                if (!inputValidation.isValidWeight(weightValue)) {
                    System.out.println("Invalid Weight");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid weight format. Please enter a number.");
                return;
            }

            System.out.print("Enter contact number: ");
            contactNumber = scanner.nextLine();

            System.out.print("Enter Barangay: ");
            barangay = scanner.nextLine();

            System.out.print("Enter Municipality: ");
            municipality = scanner.nextLine();

            System.out.print("Enter Province: ");
            province = scanner.nextLine();

            System.out.print("Enter Region: ");
            region = scanner.nextLine();

            if (!inputValidation.isValidName(firstName) || !inputValidation.isValidName(lastName) || !inputValidation.isValidName(middleName) || !inputValidation.isValidGender(gender) || !inputValidation.isValidBloodType(bloodType) || !inputValidation.isValidContactNumber(contactNumber) || !inputValidation.isValidName(barangay) || !inputValidation.isValidName(municipality) || !inputValidation.isValidName(province) || !inputValidation.isValidName(region)) {
                System.out.println("Invalid Input");
                return;
            }

            User user = new User(firstName, middleName, lastName, gender, birthDateObj, email, hashedPassword, bloodType, weightValue, contactNumber, barangay, municipality, province, region);

            try {
                User existingUser = userData.getUserByEmail(email);
                if (existingUser != null) {
                    user.setUserId(existingUser.getUserId());
                    userData.updateUser(user);
                }
            } catch (UserNotFoundException e) {
                System.err.println("Error updating user: " + e.getMessage());
                return; // Or handle differently
            } catch (SQLException e) {
                System.err.println("Error: " + e.getMessage());
                return; // Or handle differently
            }
            System.out.println("Registration Complete!");
        }
    }

    public User loginUser(String email, String password) throws SQLException {
        if (!inputValidation.isValidEmail(email) || !inputValidation.isValidPassword(password)) {
            System.out.println("Invalid Email or Password");
            return null;
        }

        try {
            User user = userData.getUserByEmail(email);
            if (user != null && passwordHashing.verifyPassword(password, user.getPassword())) {
                return user;
            } else {
                System.out.println("Invalid Email or Password");
                return null;
            }
        } catch (UserNotFoundException e) {
            System.err.println("Login Failed: " + e.getMessage());
            return null;
        }
    }


    public User getUserById(int userId) throws SQLException {
        try {
            return userData.getUserById(userId);
        } catch (UserNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }


        public void updateUser(int userId, String firstName, String middleName, String lastName, String gender, String birthDate, String email, String password, String bloodType, String weight, String contactNumber, String barangay, String city, String province, String region) throws SQLException, UserNotFoundException {
        // Input Validation
        if (!inputValidation.isValidName(firstName) || !inputValidation.isValidName(lastName) || !inputValidation.isValidName(middleName) || !inputValidation.isValidGender(gender) || !inputValidation.isValidEmail(email) || !inputValidation.isValidPassword(password) || !inputValidation.isValidBloodType(bloodType) || !inputValidation.isValidWeight(Double.parseDouble(weight)) || !inputValidation.isValidContactNumber(contactNumber) || !inputValidation.isValidName(barangay) || !inputValidation.isValidName(city) || !inputValidation.isValidName(province) || !inputValidation.isValidName(region)) {
            System.out.println("Invalid Input");
            return;
        }
        LocalDate birthDateObj = null;
        if (inputValidation.isValidDate(birthDate)) {
            birthDateObj = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else {
            System.out.println("Invalid birth date format. Please use YYYY-MM-DD.");
            return; // Or handle the error as needed
        }
        try {
            User user = userData.getUserById(userId);

            if(user != null){ //Check if User Exists
                String hashedPassword = passwordHashing.hashPassword(password); //Hash the Password

                user.setFirstName(firstName);
                user.setMiddleName(middleName);
                user.setLastName(lastName);
                user.setGender(gender);
                user.setBirthDate(birthDateObj);
                user.setEmail(email);
                user.setPassword(hashedPassword);
                user.setBloodType(bloodType);
                user.setWeight(Double.parseDouble(weight));
                user.setContactNumber(contactNumber);
                user.setBarangay(barangay);
                user.setCity(city);
                user.setProvince(province);
                user.setRegion(region);

                userData.updateUser(user);
                System.out.println("User updated successfully.");


            }else{
                System.out.println("User Not Found");
            }

        } catch (UserNotFoundException | SQLException | DateTimeParseException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw e;
        }
    }

    public void deleteUser(int userId) throws SQLException {
        try {
            userData.deleteUser(userId);
            System.out.println("User deleted successfully.");
        } catch (UserNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() throws SQLException {
        return userData.getAllUsers();
    }

}