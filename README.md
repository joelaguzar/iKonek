# 🌟 iKonek: Every Drop Counts, Every Click Matters 🌟
_A Java-based Non-Profit Donation Tracking System_

---

## 📖 I. Project Overview
## Overview
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

## II. iKonek: Implementation of OOP Principles

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

## 🌍 III. Sustainable Development Goal (SDG) Integration

**iKonek** directly supports **SDG 3: Good Health and Well-being** by ensuring a steady and reliable supply of blood donations, which are essential for saving lives and improving healthcare outcomes. The system makes it easier for Filipinos to donate blood, contributing to emergency care, surgeries, and ongoing treatments. 

Additionally, iKonek’s fundraising feature extends beyond healthcare, enabling support for various causes such as **disaster relief**, **education**, and **community development**. This allows Filipinos to contribute to the well-being of those in need, strengthening both individual and community resilience.

---

## 🏗️ IV. Project Structure and Database Schema

### 🛠️ **Technology Used**
- **Language**: Java 21
- **Database**: MySQL 8.0
- **Database Connector:** MySQL Connector/J 9.1.0
- **Build Tool:** Maven

### 📂 **Project Structure**
![Project Structure]

*   **`models`:** This package houses the Java classes representing the core data structures of the application. These classes encapsulate data and provide methods for accessing and manipulating that data. Key classes include `User`, `Admin`, `Hospital`, `FundraisingInitiative`, `MonetaryDonationImpl`, and `BloodDonationImpl`.  These classes adhere to the principles of encapsulation and data integrity.  Interfaces `Donation`, `MonetaryDonation`, and `BloodDonation` are also present in this package.

*   **`services`:** This package contains service classes that encapsulate the business logic of the application. Service classes interact with Data Access Objects (DAOs) to perform database operations and implement application-specific rules and workflows.  Key classes include `UserService`, `AdminService`, `HospitalService`, `FundraiserService`, `MonetaryDonationService`, and `BloodDonationService`.

*   **`dao`:** This package houses the Data Access Objects (DAOs) that manage interactions with the MySQL database. Key classes include `UserDao`, `AdminDao`, `HospitalDao`, `FundraisingInitiativeDao`, `MonetaryDonationDao`, and `BloodDonationDao`.

*   **`views`:** This package contains the classes responsible for the console-based user interface (UI).  These classes manage user interactions, display menus, handle user input, and present data to the user in a clear and organized format. Key classes include `MainMenu`, `UserMenu`, `AdminMenu`, and `DonationTicketView`.

*   **`utils`:** This package includes utility classes that provide reusable functionality across multiple parts of the application.  These classes encapsulate common functions to improve code reusability and maintainability.  Key classes include `DatabaseConnection`, `InputValidator`, and `PasswordHasher`.

*   **`exceptions`:** This package contains custom exception classes that are used to handle errors and provide context-specific information about issues arising in different parts of the application. The package includes `UserServiceException.java`, `AdminServiceException.java`, `HospitalServiceException.java`, `FundraiserServiceException.java`, `BloodDonationServiceException.java` and `MonetaryDonationServiceException.java`
  
### 📊 Database Schema

#### **Users Table**
![Users Table Schema](https://github.com/joelaguzar/iKonek/blob/main/images/users_table.png?raw=true)

#### **Admins Table**
![Admins Table Schema](https://github.com/joelaguzar/iKonek/blob/main/images/admins_table.png?raw=true)

#### **Hospitals Table**
![Hospitals Table Schema](https://github.com/joelaguzar/iKonek/blob/main/images/hospitals_table.png?raw=true)

#### **Blood Donations Table**
![Blood Donations Table Schema](https://github.com/joelaguzar/iKonek/blob/main/images/blooddonations_table.png?raw=true)

#### **Fundraising Initiatives Table**
![Fundraising Initiatives Table Schema](https://github.com/joelaguzar/iKonek/blob/main/images/fundraisinginitiatives_table.png?raw=true)

#### **Donations Table**
![Donations Table Schema](https://github.com/joelaguzar/iKonek/blob/main/images/donations_table.png?raw=true)

---

