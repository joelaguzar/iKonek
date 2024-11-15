# 🌟 iKonek: Every Drop Counts, Every Click Matters 🌟
_A Java-based Non-Profit Donation Tracking System_

---

## 📖 Overview
**iKonek** is a Java console application that simplifies the process of blood donations and fundraising. Designed with Object-Oriented Programming (OOP) principles and powered by MySQL, iKonek offers a seamless experience for both users and administrators.

---

## 🎯 Key Features

### 👤 **User Management**
- **User Registration**:  
  Secure and streamlined process, capturing key details like name, contact, and blood type.
- **Profile Management**:  
  Update profile (except immutable fields like blood type).
- **Donation & Fundraising History**:
    - Track blood donation records.
    - View contributions to fundraising initiatives.

### 🩸 **Blood Donation**
- **Eligibility Checks**:  
  Validates age (16–65 years) and weight (≥50 kg).
- **Scheduling Donations**:
    - Choose from a list of registered hospitals.
    - Receive a unique donation ticket.
- **Manage Donations**:  
  Option to cancel donations 24 hours before the schedule.

### 💰 **Fundraising**
- **Create Initiatives**:  
  Define cause, target amount, deadline, and details.
- **Donation to Initiatives**:  
  Users can donate to active campaigns with real-time updates on progress.

### 🔑 **Admin Management**
- **Hospital Management**:  
  Add, update, or remove hospital details.
- **User and Donation Management**:  
  View user records and update donation statuses (e.g., "Successful" or "Failed").
- **Reports**:  
  Generate insights such as donations by blood type or total funds raised.

---

## 🏗️ Architecture

### 📂 **Project Structure**
1. ikonek-donation-tracker/
2. ├── src/main/java/ikonek/
3. │   ├── models/
4. │   │   ├── User.java
5. │   │   ├── Donation.java
6. │   │   ├── MonetaryDonation.java
7. │   │   ├── BloodDonation.java
8. │   │   ├── MonetaryDonationImpl.java
9. │   │   ├── BloodDonationImpl.java
10. │   │   ├── FundraisingInitiative.java
11. │   │   ├── Hospital.java
12. │   │   └── Admin.java
13. │   ├── services/
14. │   │   ├── UserService.java
15. │   │   ├── AdminService.java
16. │   │   ├── BloodDonationService.java
17. │   │   ├── MonetaryDonationService.java
18. │   │   ├── FundraiserService.java
19. │   │   └── HospitalService.java
20. │   ├── dao/
21. │   │   ├── UserDao.java
22. │   │   ├── MonetaryDonationDao.java
23. │   │   ├── BloodDonationDao.java
24. │   │   ├── FundraisingInitiativeDao.java
25. │   │   ├── HospitalDao.java
26. │   │   └── AdminDao.java
27. │   ├── exceptions/
28. │   │   ├── UserServiceException.java
29. │   │   ├── AdminServiceException.java
30. │   │   ├── HospitalServiceException.java
31. │   │   ├── FundraiserServiceException.java
32. │   │   ├── MonetaryDonationServiceException.java
33. │   │   └── BloodDonationServiceException.java
34. │   ├── views/                
35. │   │   ├── MainMenu.java
36. │   │   ├── UserMenu.java
37. │   │   ├── AdminMenu.java
38. │   │   └── DonationTicketView.java
39. │   ├── utils/
40. │   │   ├── DatabaseConnection.java
41. │   │   ├── InputValidator.java
42. │   │   └── PasswordHasher.java
43. │   └── MainApp.java          
44. ├── src/main/resources/
45. │   ├── db.properties
46. └── pom.xml 


### 🛠️ **Technology Stack**
- **Language**: Java 21
- **Database**: MySQL 8.0
- 

---

## 📊 Database Schema

### **Users Table**
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    birth_date DATE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    blood_type ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-') NOT NULL,
    weight DECIMAL(5,2) NOT NULL,
    contact_number VARCHAR(15) NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


### **Admins Table**
CREATE TABLE Admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    contact_number VARCHAR(15) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


### **Hospitals Table**
CREATE TABLE Hospitals (
    hospital_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    province VARCHAR(50) NOT NULL,
    contact_number VARCHAR(15) NOT NULL
);

### **Blood Donations Table**
CREATE TABLE BloodDonations (
    donation_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    hospital_id INT NOT NULL,
    donation_date DATE NOT NULL,
    status ENUM('Pending', 'Successful', 'Failed', 'Cancelled') DEFAULT 'Pending',
    failure_reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (hospital_id) REFERENCES Hospitals(hospital_id) ON DELETE CASCADE
);

### **Fundraising Initiatives Table**
CREATE TABLE FundraisingInitiatives (
    fundraiser_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    cause VARCHAR(255) NOT NULL,
    target_amount DECIMAL(10,2) NOT NULL,
    amount_received DECIMAL(10,2) DEFAULT 0,
    short_description VARCHAR(500),
    deadline DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

### **Donations Table**
CREATE TABLE Donations (
    donation_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    fundraiser_id INT NOT NULL,
    donation_amount DECIMAL(10,2) NOT NULL,
    donation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (fundraiser_id) REFERENCES FundraisingInitiatives(fundraiser_id) ON DELETE CASCADE
);


---

