CREATE DATABASE payroll_db;
use payroll_db;


-- Departments Table
CREATE TABLE departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    budget DECIMAL(15,2)
);

-- Positions Table
CREATE TABLE positions (
    position_id INT PRIMARY KEY AUTO_INCREMENT,
    position_title VARCHAR(255) NOT NULL,
    min_salary DECIMAL(10,2),
    max_salary DECIMAL(10,2)
);

-- Employees Table (Main)
CREATE TABLE employees (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    department_id INT,
    position_id INT,
    hire_date DATE,
    email VARCHAR(255),
    phone VARCHAR(15),
    bank_account VARCHAR(255),
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
    FOREIGN KEY (position_id) REFERENCES positions(position_id)
);

-- Salary Structure Table
CREATE TABLE salaries (
    salary_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    basic_salary DECIMAL(10,2),
    housing_allowance DECIMAL(10,2),
    transportation_allowance DECIMAL(10,2),
    effective_date DATE,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Payroll Records Table
CREATE TABLE payroll (
    payroll_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    pay_date DATE,
    gross_salary DECIMAL(10,2),
    deductions DECIMAL(10,2),
    net_salary DECIMAL(10,2),
    payment_method VARCHAR(50),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Tax and Deductions Table
CREATE TABLE deductions (
    deduction_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    deduction_type ENUM('Income Tax', 'Health Insurance', 'Pension', 'Other'),
    amount DECIMAL(10,2),
    deduction_date DATE,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Attendance Table
CREATE TABLE attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT,
    date DATE,
    check_in TIME,
    check_out TIME,
    status ENUM('Present', 'Absent', 'Leave'),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Users Table (for system access)
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    employee_id INT UNIQUE,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role ENUM('Admin', 'HR', 'Accountant'),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);


-- Insert Departments
INSERT INTO departments (department_name, location, budget)
VALUES 
('Human Resources', 'New York', 500000),
('Finance', 'London', 750000),
('IT', 'San Francisco', 1000000);

-- Insert Positions
INSERT INTO positions (position_title, min_salary, max_salary)
VALUES
('HR Manager', 60000, 90000),
('Software Engineer', 70000, 120000),
('Accountant', 50000, 80000);

desc employees;
show tables;
