# 🌟 iKonek: Every Drop Counts, Every Click Matters 🌟
_A Java-based Non-Profit Donation Tracking System_

---

## Table of Contents
-  [I. Project Overview](#overview)
-  [II. iKonek: Implementation of OOP Principles](#oop)
-  [III. Sustainable Development Goal (SDG) Integration](#sdg)
-  [IV. Project Structure and Database Schema](#struct)
-  [V. Running the Program](#run)
-  [VI. Future Enhancements](#future)
-  [VII. Contributor](#contrib)
-  [VIII. Course Information](#course)

---

## <a id = "overview">📖 I. Project Overview
## Introduction
**iKonek** is a *Java-based console application* designed to streamline the management of **blood donations** and **fundraising initiatives** for non-profit organizations. Built using *Object-Oriented Programming (OOP)* principles, it offers a **secure** and **efficient platform** that prioritizes **user-friendliness** and **data integrity**. Users can register, manage profiles, schedule blood donations, and participate in fundraising campaigns, while administrators gain access to *robust tools* for managing hospital data, verifying donations, generating reports, and overseeing user accounts. By leveraging **MySQL** for *persistent data storage*, **iKonek** ensures **scalability** and **reliability**, making it a comprehensive solution for enhancing *community-driven efforts* in blood donation and charitable fundraising.

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
  Option to cancel donations before the schedule.

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

## <a id = "oop">II. iKonek: Implementation of OOP Principles

The **iKonek donation system** is structured using the four core **OOP principles** to ensure code clarity, maintainability, and extensibility:  

### **A. Abstraction**  
The system employs **abstraction** by defining a `Donation` interface. This interface acts as a **contract**, specifying essential methods common to all donation types. This design promotes flexibility, making it easier to add new donation types in the future without extensive code changes. The `Donation` interface, along with its child interfaces `MonetaryDonation` and `BloodDonation`, clearly defines the required methods for each donation type, enhancing **code organization** and reducing redundancy.  

### **B. Encapsulation**  
Each class in iKonek enforces **encapsulation** by declaring data fields as `private` and providing controlled access through public getter and setter methods. This approach protects **data integrity** and simplifies **maintenance** by restricting direct manipulation of the internal state. **Data validation** is performed within the setter methods, ensuring that only valid data is stored.  

### **C. Inheritance**  
**Inheritance** is effectively applied in the `Admin` class, which extends the `User` class. This reflects the **"is-a" relationship**, where an admin *is a* user with elevated privileges. This mechanism promotes **code reuse** and enhances **maintainability**. The `Admin` class adds admin-specific attributes, such as `adminId` and `username`, while reusing common attributes and methods from the `User` class.  

### **D. Polymorphism**  
**Polymorphism** is implemented through the `processDonation()` method within the `Donation` interface. Concrete implementations of `processDonation()` in `MonetaryDonationImpl` and `BloodDonationImpl` handle the unique processing logic for **monetary** and **blood donations**, respectively. This enables flexible processing of different donation types without requiring explicit type checks. The `DonationService` class utilizes polymorphic method calls to process various donation types, enhancing the system’s **adaptability**.  

---

## <a id = "sdg">🌍 III. Sustainable Development Goal (SDG) Integration

**iKonek** directly supports **SDG 3: Good Health and Well-being** by ensuring a steady and reliable supply of blood donations, which are essential for saving lives and improving healthcare outcomes. The system makes it easier for Filipinos to donate blood, contributing to emergency care, surgeries, and ongoing treatments. 

Additionally, iKonek’s fundraising feature extends beyond healthcare, enabling support for various causes such as **disaster relief**, **education**, and **community development**. This allows Filipinos to contribute to the well-being of those in need, strengthening both individual and community resilience.

---

## <a id = "struct">🏗️ IV. Project Structure and Database Schema

### 🛠️ **Technology Used**
- **Language**: Java 21
- **Database**: MySQL 8.0
- **Database Connector:** MySQL Connector/J 9.1.0
- **Build Tool:** Maven
- **IDE:** Intellij IDEA
- **Version Control:** Git
  
### 📂 **Project Structure**
```
iKonek-Donation-Tracker/
├── src/main/java/ikonek/
│   ├── models/         
│   ├── services/
│   ├── dao/
│   ├── views/
│   ├── exceptions/
│   ├── utils/
│   └── MainApp.java
├── src/main/resources/
│   └── db.properties
└── pom.xml
```

*   **`models`:** This package houses the Java classes representing the core data structures of the application. These classes encapsulate data and provide methods for accessing and manipulating that data. Key classes include `User`, `Admin`, `Hospital`, `FundraisingInitiative`, `MonetaryDonationImpl`, and `BloodDonationImpl`.  These classes adhere to the principles of encapsulation and data integrity.  Interfaces `Donation`, `MonetaryDonation`, and `BloodDonation` are also present in this package.

*   **`services`:** This package contains service classes that encapsulate the business logic of the application. Service classes interact with Data Access Objects (DAOs) to perform database operations and implement application-specific rules and workflows.  Key classes include `UserService`, `AdminService`, `HospitalService`, `FundraiserService`, `MonetaryDonationService`, and `BloodDonationService`.

*   **`dao`:** This package houses the Data Access Objects (DAOs) that manage interactions with the MySQL database. Key classes include `UserDao`, `AdminDao`, `HospitalDao`, `FundraisingInitiativeDao`, `MonetaryDonationDao`, and `BloodDonationDao`.

*   **`views`:** This package contains the classes responsible for the console-based user interface (UI).  These classes manage user interactions, display menus, handle user input, and present data to the user in a clear and organized format. Key classes include `MainMenu`, `UserMenu`, `AdminMenu`, and `DonationTicketView`.

*   **`utils`:** This package includes utility classes that provide reusable functionality across multiple parts of the application.  These classes encapsulate common functions to improve code reusability and maintainability.  Key classes include `DatabaseConnection`, `InputValidator`, and `PasswordHasher`.

*   **`exceptions`:** This package contains custom exception classes that are used to handle errors and provide context-specific information about issues arising in different parts of the application. The package includes `UserServiceException.java`, `AdminServiceException.java`, `HospitalServiceException.java`, `FundraiserServiceException.java`, `BloodDonationServiceException.java` and `MonetaryDonationServiceException.java`
  
### 📊 Database Schema

#### **Users Table**
```
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
```

#### **Admins Table**
```
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
```

#### **Hospitals Table**
```
CREATE TABLE Hospitals (
    hospital_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    province VARCHAR(50) NOT NULL,
    contact_number VARCHAR(15) NOT NULL
);
```

#### **Blood Donations Table**
```
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
```

#### **Fundraising Initiatives Table**
```
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
```

#### **Donations Table**
```
CREATE TABLE Donations (
    donation_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    fundraiser_id INT NOT NULL,
    donation_amount DECIMAL(10,2) NOT NULL,
    donation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (fundraiser_id) REFERENCES FundraisingInitiatives(fundraiser_id) ON DELETE CASCADE
);
```

---

## <a id = "run">📥 V. Running the Program 

### Prerequisites  
1. Install **Java Development Kit (JDK) 17** or higher.  
2. Install **MySQL Database Server 8** or higher.  
3. Ensure the **MySQL Connector/J** (version 9.1.0) is included in your project's `lib` folder (or manage via Maven).

### Steps
1. **Clone the Repository:** Clone the project from GitHub:  `git clone https://github.com/joelaguzar/iKonek.git`
 
2. **Database Setup:** Create a MySQL database named "ikonek_db" and use the SQL schema from the Database Schema section. Update `src/main/resources/db.properties` with your database credentials.
 
3. **Build and Run (using Maven):**
 -   Navigate to the project directory in your terminal.<br/>
 -   Run `mvn clean install` to build the project.<br/>
 -   Run `mvn exec:java -Dexec.mainClass="ikonek.MainApp"` to execute the application.<br/>

---

## <a id = "future">🚀 VI. Future Enhancements

Key features for future updates:

#### 🖥️ User Interface
- Build a responsive interface for enhanced accessibility, ensuring users and admins can interact with the system from any device.

#### 💳 Payment Gateway Integration
- Integrate with a secure payment gateway for seamless and safe online donations.

#### 📊 Reporting and Analytics
- Implement advanced reporting and analytics features for admins to monitor donation trends, fundraising progress, and more.

#### 📱 SMS/Email Notification System
- Add SMS/Email notification capabilities for real-time updates on donation statuses, upcoming donation schedules, and fundraising initiative alerts.

#### 🔒 Enhanced Security Measures
-  Upgrade the platform with robust security protocols and password hashing algorithm to protect against common vulnerabilities, such as SQL injection and Password hacking.


##  <a id = "contrib"> 👷‍ VII. Contributor </a> <br>

| Name | Role | E-mail |
| --- | --- | --- |
| <a href = "https://github.com/joelaguzar">Joel Lazernie A. Aguzar</a> | Developer | aguzarjoel07@gmail.com |

## <a id = "course">💙 VIII. Course Information
- **Course:** CS 211: Object Oriented Programming
- **Instructor:** Ms. Fatima Marie P. Agdon
