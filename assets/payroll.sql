-- Create the database
CREATE DATABASE IF NOT EXISTS payroll;

-- Use the payroll database
USE payroll;


-- Create the Employee table without a default for date_of_joining
CREATE TABLE IF NOT EXISTS employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    dob DATE NOT NULL,
    date_of_joining DATE,
    salary DECIMAL(10, 2) NOT NULL,
    role VARCHAR(50) NOT NULL,
    bank_no VARCHAR(20) NOT NULL
);

-- Create a trigger to set the current date on insert
DELIMITER //

CREATE TRIGGER before_insert_employee
BEFORE INSERT ON employee
FOR EACH ROW
BEGIN
    IF NEW.date_of_joining IS NULL THEN
        SET NEW.date_of_joining = CURDATE();
    END IF;
END//

DELIMITER ;

-- Create the Attendance table
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(10) CHECK (status IN ('Present', 'Absent')),
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE
);

-- Create the Users table for login
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('Admin', 'Employee') NOT NULL
);

