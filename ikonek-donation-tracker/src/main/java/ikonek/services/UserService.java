package ikonek.services;

import ikonek.dao.UserDao;
import ikonek.exceptions.UserServiceException;
import ikonek.models.User;
import ikonek.utils.InputValidator;
import ikonek.utils.PasswordHasher;

import java.time.LocalDate;
import java.time.Period;

public class UserService {

    private final UserDao userDao;
    private final PasswordHasher passwordHasher;

    public UserService(UserDao userDao, PasswordHasher passwordHasher) {
        this.userDao = userDao;
        this.passwordHasher = passwordHasher;
    }

    public User registerUser(String firstName, String middleName, String lastName, String gender, LocalDate birthDate,
                             String email, String password, String bloodType, double weight, String contactNumber) {

        // Input validation (using InputValidator)
        if (!InputValidator.isValidEmail(email)) {
            throw new UserServiceException("Invalid email format.");
        }
        if (!isUniqueEmail(email)) {
            throw new UserServiceException("Email address already registered.");
        }
        if (!InputValidator.isValidPassword(password)) {
            throw new UserServiceException("Password must be at least 8 characters long.");
        }
        if (!InputValidator.isValidName(firstName) || !InputValidator.isValidName(lastName)) {
            throw new UserServiceException("First and last names cannot be blank.");
        }
        if (!InputValidator.isValidGender(gender)) {
            throw new UserServiceException("Invalid gender.");
        }
        if (!InputValidator.isValidBirthDate(birthDate)) {
            throw new UserServiceException("Invalid birth date.");
        }
        if (!InputValidator.isValidBloodType(bloodType)) {
            throw new UserServiceException("Invalid blood type.");
        }
        if (!InputValidator.isValidWeight(weight)) {
            throw new UserServiceException("Invalid weight.");
        }
        if (!InputValidator.isValidContactNumber(contactNumber)) {
            throw new UserServiceException("Invalid contact number.");
        }

        //Check if email already exists
        if (userDao.getUserByEmail(email) != null) {
            throw new UserServiceException("Email already exists. Please use another email.");
        }

        //Check if password is not empty
        if (password == null || password.isEmpty() || password.isBlank()) {
            throw new UserServiceException("Password cannot be empty.");
        }

        String hashedPassword = passwordHasher.hashPassword(password);
        User newUser = new User(firstName, middleName, lastName, gender, birthDate, email, hashedPassword, bloodType, weight, contactNumber);
        return userDao.createUser(newUser);
    }

    public boolean isUniqueEmail(String email) {
        return userDao.getUserByEmail(email) == null;
    }

    public User loginUser(String email, String password) {
        if (!InputValidator.isValidEmail(email) || password == null || password.isBlank()) {
            return null;
        }

        User user = userDao.getUserByEmail(email);
        if (user != null && passwordHasher.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    public User updateUser(User user, String password) {
        String hashedPassword = passwordHasher.hashPassword(password);
        user.setPasswordHash(hashedPassword);
        User updatedUser = userDao.updateUser(user);
        if (updatedUser == null) {
            throw new UserServiceException("Failed to update user information.");
        }
        return updatedUser;
    }

    public User updateUser(User user) {
        User updatedUser = userDao.updateUser(user);
        if (updatedUser == null) {
            throw new UserServiceException("Failed to update user information.");
        }
        return updatedUser;
    }

    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public boolean deleteUser(int userId) {
        return userDao.deleteUser(userId); }

    public boolean isEligibleForBloodDonation(User user) {
        int age = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
        return age >= 16 && age <= 65 && user.getWeight() >= 50;
    }
}