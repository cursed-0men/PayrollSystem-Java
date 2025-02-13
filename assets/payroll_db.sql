-- Drop and recreate database
DROP DATABASE IF EXISTS payroll_db;
CREATE DATABASE payroll_db;
USE payroll_db;

-- Departments Table
CREATE TABLE departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL UNIQUE,
    location VARCHAR(255),
    budget DECIMAL(15,2) DEFAULT 0.00
);

-- Positions Table
CREATE TABLE positions (
    position_id INT AUTO_INCREMENT PRIMARY KEY,
    position_title VARCHAR(255) NOT NULL UNIQUE,
    min_salary DECIMAL(10,2) DEFAULT 0.00,
    max_salary DECIMAL(10,2) DEFAULT 0.00
);

-- Employees Table (Includes Payroll Managers)
CREATE TABLE employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    department_id INT NOT NULL,
    position_id INT NOT NULL,
    hire_date DATE DEFAULT (CURRENT_DATE),
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE,
    bank_account VARCHAR(255) UNIQUE NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,
    FOREIGN KEY (position_id) REFERENCES positions(position_id) ON DELETE CASCADE
);

-- Users Table (System Login, Now Linked to Employees)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'HR', 'Accountant') NOT NULL DEFAULT 'HR',
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- Salary Structure Table
CREATE TABLE salaries (
    salary_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    basic_salary DECIMAL(10,2) NOT NULL CHECK (basic_salary >= 0),
    housing_allowance DECIMAL(10,2) DEFAULT 0.00 CHECK (housing_allowance >= 0),
    transportation_allowance DECIMAL(10,2) DEFAULT 0.00 CHECK (transportation_allowance >= 0),
    effective_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- Payroll Records Table
CREATE TABLE payroll (
    payroll_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    pay_date DATE DEFAULT (CURRENT_DATE),
    gross_salary DECIMAL(10,2) NOT NULL CHECK (gross_salary >= 0),
    deductions DECIMAL(10,2) DEFAULT 0.00 CHECK (deductions >= 0),
    net_salary DECIMAL(10,2) NOT NULL CHECK (net_salary >= 0),
    payment_method ENUM('Bank Transfer', 'Cheque', 'Cash') DEFAULT 'Bank Transfer',
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- Deductions Table
CREATE TABLE deductions (
    deduction_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    deduction_type ENUM('Income Tax', 'Health Insurance', 'Pension', 'Other') NOT NULL,
    amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
    deduction_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- Attendance Table
CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    date DATE NOT NULL DEFAULT (CURRENT_DATE),
    check_in TIME DEFAULT NULL,
    check_out TIME DEFAULT NULL,
    status ENUM('Present', 'Absent', 'Leave') NOT NULL DEFAULT 'Absent',
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

