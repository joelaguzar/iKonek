package ikonek.models;

public class Admin extends User {
    private int adminId;           // unique identifier for admin
    private String username;        //admin login

    // Constructor
    public Admin(String firstName, String middleName, String lastName, String email, String passwordHash, String contactNumber, String username) {
        super(firstName, middleName, lastName, email, passwordHash, contactNumber);
        this.username = username;
    }

    // Getters and Setters
    public String getPasswordHash() {
        return super.getPasswordHash();
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", username='" + username + '\'' +
                ", " + super.toString() +
                '}';
    }
}
