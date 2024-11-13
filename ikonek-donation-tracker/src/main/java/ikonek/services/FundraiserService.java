package ikonek.services;

import ikonek.dao.FundraisingInitiativeDao;
import ikonek.dao.UserDao;
import ikonek.exceptions.FundraiserServiceException;
import ikonek.models.FundraisingInitiative;
import ikonek.models.User;
import ikonek.utils.InputValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FundraiserService {

    private final UserDao userDao;
    private final FundraisingInitiativeDao fundraisingInitiativeDao;

    public FundraiserService(UserDao userDao, FundraisingInitiativeDao fundraisingInitiativeDao) {
        this.userDao = userDao;
        this.fundraisingInitiativeDao = fundraisingInitiativeDao;
    }

    public int createFundraisingInitiative(int userId, String cause, double targetAmount, String shortDescription, String deadline) {
        try {
            if (!InputValidator.isValidName(cause) || !InputValidator.isValidName(shortDescription) || targetAmount <= 0) {
                throw new FundraiserServiceException("Invalid input for fundraising initiative.");
            }
        } catch (FundraiserServiceException e) {
            System.err.println("Error creating fundraising initiative: " + e.getMessage());
            return -1;
        }

        LocalDate parsedDeadline;
        try {
            parsedDeadline = LocalDate.parse(deadline, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid deadline format. Use yyyy-MM-dd.");
            return -1;
        }

        try {
            User user = userDao.getUserById(userId);
            if (user == null) {
                throw new FundraiserServiceException("User with ID " + userId + " does not exist.");
            }
        } catch (FundraiserServiceException e) {
            System.err.println("Error: " + e.getMessage());
            return -1;
        }

        try {
            FundraisingInitiative initiative = new FundraisingInitiative(userId, cause, targetAmount, 0.0, shortDescription, parsedDeadline);
            int newInitiativeId = fundraisingInitiativeDao.createFundraisingInitiative(initiative);
            if (newInitiativeId == -1) {
                throw new FundraiserServiceException("Failed to create fundraising initiative.");
            }
            return newInitiativeId;
        } catch (FundraiserServiceException e) {
            System.err.println("Error creating fundraising initiative: " + e.getMessage());
            return -1;
        }
    }

    public List<FundraisingInitiative> getAllFundraisingInitiatives() {
        return fundraisingInitiativeDao.getAllFundraisingInitiatives();
    }

    public FundraisingInitiative getFundraisingInitiativeById(int fundraisingId) {
        return fundraisingInitiativeDao.getFundraisingInitiativeById(fundraisingId);
    }

    public boolean updateFundraisingInitiative(FundraisingInitiative fundraisingInitiative) {
        return fundraisingInitiativeDao.updateFundraisingInitiative(fundraisingInitiative);
    }

    public boolean deleteFundraisingInitiative(int fundraisingId) {
        return fundraisingInitiativeDao.deleteFundraisingInitiative(fundraisingId);
    }

    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}