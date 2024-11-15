package ikonek.services;

import ikonek.dao.AdminDao;
import ikonek.dao.UserDao;
import ikonek.exceptions.AdminServiceException;
import ikonek.models.Admin;
import ikonek.models.User;
import ikonek.utils.InputValidator;
import ikonek.utils.PasswordHasher;

import java.util.ArrayList;
import java.util.List;

public class AdminService {

    private final AdminDao adminDao;
    private final PasswordHasher passwordHasher;
    private final UserDao userDao;

    public AdminService(AdminDao adminDao, PasswordHasher passwordHasher, UserDao userDao) {
        this.adminDao = adminDao;
        this.passwordHasher = passwordHasher;
        this.userDao = userDao;
    }

    public Admin registerAdmin(String firstName, String middleName, String lastName, String email, String password, String contactNumber, String username) {
        if (!InputValidator.isValidName(firstName) || !InputValidator.isValidName(lastName) ||
                !InputValidator.isValidEmail(email) || !InputValidator.isValidContactNumber(contactNumber) ||
                username == null || username.isBlank() || password == null || password.isBlank()) {
            System.err.println("Invalid input. Please check your details.");
            return null;
        }

        if (adminDao.getAdminByUsername(username) != null) {
            System.err.println("Username already exists. Please use another username.");
            return null;
        }

        String hashedPassword = passwordHasher.hashPassword(password);
        Admin newAdmin = new Admin(firstName, middleName, lastName, email, hashedPassword, contactNumber, username);

        int adminId = adminDao.createAdmin(newAdmin);
        if (adminId == -1) {
            System.err.println("Admin registration failed.");
            return null;
        }

        newAdmin.setAdminId(adminId);
        return newAdmin;
    }

    public Admin loginAdmin(String username, String password) {
        Admin admin = adminDao.getAdminByUsername(username);
        if (admin == null) {
            throw new AdminServiceException("Login failed. Invalid username.");
        }

        String storedHashedPassword = admin.getPasswordHash();

        if (storedHashedPassword == null) {
            throw new AdminServiceException("Error: Hashed password not found for this user.");
        }

        boolean passwordMatch = passwordHasher.verifyPassword(password, storedHashedPassword);

        if (passwordMatch) {
            return admin;
        } else {
            return null;
        }
    }

    public List<User> getAllUsers() {
        try {
            return userDao.getAllUsers();
        } catch (Exception e) {
            System.err.println("Error retrieving users: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean updateAdmin(Admin admin) {
        if (!InputValidator.isValidName(admin.getFirstName()) || !InputValidator.isValidName(admin.getLastName()) ||
                !InputValidator.isValidEmail(admin.getEmail()) || !InputValidator.isValidContactNumber(admin.getContactNumber()) ||
                admin.getUsername() == null || admin.getUsername().isBlank()) {

            System.err.println("Invalid input. Please check your details.");
            return false;
        }
        return adminDao.updateAdmin(admin);
    }

    public Admin getAdminById(int adminId) {
        return adminDao.getAdminById(adminId);
    }

    public boolean deleteAdmin(int adminId) {
        return adminDao.deleteAdmin(adminId);
    }
}