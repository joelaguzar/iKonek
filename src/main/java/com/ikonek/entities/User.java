package com.ikonek.entities;

import java.time.LocalDate;
import java.time.Period;

public class User {
    private int userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private String email;
    private String password;
    private String bloodType;
    private double weight;
    private String contactNumber;
    private String barangay;
    private String municipality;
    private String province;
    private String region;

    // Constructors
    public User() {} //use because of the db

    //might delete later just for flexibility
    public User(String firstName, String middleName, String lastName, String gender, LocalDate birthDate, String email, String password, String bloodType, double weight, String contactNumber, String barangay, String municipality, String province, String region) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.bloodType = bloodType;
        this.weight = weight;
        this.contactNumber = contactNumber;
        this.barangay = barangay;
        this.municipality = municipality;
        this.province = province;
        this.region = region;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    // Helper method to calculate age
    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return  "User ID: " + userId + "\n" +
                "Name: " + firstName + " " + middleName + " " + lastName + "\n" +
                "Gender: " + gender + "\n" +
                "Birth Date: " + birthDate + "\n" +
                "Email: " + email + "\n" +
                "Blood Type: " + bloodType + "\n" +
                "Weight: " + weight + "\n" +
                "Contact Number: " + contactNumber + "\n" +
                "Barangay: " + barangay + "\n" +
                "Municipality: " + municipality + "\n" +
                "Province: " + province + "\n" +
                "Region: " + region + "\n" +
                "Age: " + getAge();
    }
}
