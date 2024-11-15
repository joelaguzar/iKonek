package ikonek.utils;

import ikonek.dao.UserDao;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    public static boolean isValidPassword(String password) { // check for null, empty, or less than 8 characters
        return password != null && !password.isBlank() && password.length() >= 8;
    }

    public static boolean isValidName(String name) {  // Checks for null, empty, and whitespace-only names
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidGender(String gender) {
        return gender != null && (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("Other"));
    }

    public static boolean isValidBirthDate(LocalDate birthDate) {
        return birthDate != null && birthDate.isBefore(LocalDate.now()); // Birthdate must be in the past
    }

    public static boolean isValidWeight(double weight) {
        return weight > 0; // Weight must be positive
    }

    public static boolean isValidBloodType(String bloodType) {
        if(bloodType == null){
            return false;
        }

        String[] validBloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        for (String validType : validBloodTypes) {
            if (validType.equalsIgnoreCase(bloodType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidContactNumber(String contactNumber) {
        return contactNumber != null && contactNumber.matches("[0-9]+");
    }

}