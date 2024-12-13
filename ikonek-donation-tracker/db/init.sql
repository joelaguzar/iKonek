-- Note before you execute/run this, please make sure you have a local account.
-- Update src/main/resources/db.properties with your database credentials.
-- Run all the CREATE Queries for the program to work as intended.
-- You can optionally execute/run the INSERT admin query, just make sure to add an admin for the admin function to work.

CREATE DATABASE IF NOT EXISTS ikonek_db;
USE ikonek_db;

-- Table to store user information
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

-- Table to store admin information
CREATE TABLE Admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
	first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50),
    last_name VARCHAR(50) NOT NULL,
    contact_number VARCHAR(15), 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table to store hospitals for blood donations
CREATE TABLE Hospitals (
    hospital_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    province VARCHAR(50) NOT NULL,
    contact_number VARCHAR(15) NOT NULL
);

-- Table to store blood donation records
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

-- Table to store fundraising initiatives
CREATE TABLE FundraisingInitiatives (
    fundraising_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    cause VARCHAR(255) NOT NULL,
    target_amount DECIMAL(10,2) NOT NULL,
    amount_received DECIMAL(10,2) DEFAULT 0,
    short_description VARCHAR(500),
    deadline DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Table to store donations made to fundraising initiatives
CREATE TABLE Donations (
    donation_id INT AUTO_INCREMENT PRIMARY KEY,
    donor_id INT NOT NULL,
    fundraising_id INT NOT NULL,
    donation_amount DECIMAL(10,2) NOT NULL,
    donation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (donor_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (fundraising_id) REFERENCES FundraisingInitiatives(fundraising_id) ON DELETE CASCADE
);

-- Add default Admin to the admin table
-- You can change anything/ use the password hashing algo to change password:)
INSERT INTO `ikonek_db`.`admins`
(`username`,
`password_hash`,
`email`,
`first_name`,
 `middle_name`,
 `last_name`,
`contact_number`)
VALUES
('admin_1', -- username for login: admin_1
'c1d321ab524056', -- default password for login: admin123
'ikonek.admin@org.com',
'Admin',
'',
 '1',
'09181701889'); -- contact number for testing only

-- Add default Hospitals to the hospital table
-- so the hospitals here are just for testing purposes, you can also add hospitals through the admin menu
INSERT INTO `ikonek_db`.`hospitals`
(`name`,
 `city`,
 `province`,
 `contact_number`)
VALUES
('Batangas Medical Center', 'Batangas City', 'Batangas', '09536404961'),
('Bauan General Hospital', 'Bauan', 'Batangas', '09078589317'),
('Cabuyao City Hospital', 'Cabuyao City', 'Laguna', '09491867375'),
('Ospital ng Maynila Medical Center', 'Manila', 'Metro Manila', '09352843529'),
('UP-Philippine Genreal Hospital', 'Manila', 'Metro Manila', '09143012506'),
('East Avenue Medical Center', 'Quezon City', 'Metro Manila', '09414527457')

