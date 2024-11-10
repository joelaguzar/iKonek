package ikonek.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private int userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private String email;
    private String passwordHash;
    private String bloodType;
    private double weight;
    private String contactNumber;
    private LocalDate registrationDate;

    // Constructor
    public User(String firstName, String middleName, String lastName, String gender, LocalDate birthDate, String email, String passwordHash, String bloodType, double weight, String contactNumber) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.passwordHash = passwordHash;
        this.bloodType = bloodType;
        this.weight = weight;
        this.contactNumber = contactNumber;
        this.registrationDate = LocalDate.now(); // Set registration date to current date
    }

    // admin inherits common user fields
    public User(String firstName, String middleName, String lastName, String email, String passwordHash, String contactNumber) {
    }


    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalDate getRegistrationDate() {  // Getter for registrationDate
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = LocalDate.from(registrationDate);
    }

    @Override
    public String toString() { // For easier printing/debugging
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", weight=" + weight +
                ", contactNumber='" + contactNumber + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}