# Payroll Management System 🧾

A Java-based Payroll Management System with a GUI built using **Swing** and a backend powered by **MySQL**. The system allows for employee management, salary calculations, attendance tracking, and visualizations through charts.

---

## 💡 Features

- Add, update, search, and delete employee records  
- Calculate net salary (includes DA, HRA, allowances, deductions)  
- Mark and visualize attendance (monthly overview)  
- View salary distribution with pie charts  
- Modern gradient UI using Swing  
- Tooltips for better UX  
- Robust error dialogs and confirmations

---

## 🖥️ Technologies Used

- **Java Swing** (GUI)  
- **MySQL** (Database)  
- **JDBC** (Java Database Connectivity)  
- **JFreeChart** (for chart visualizations)

---
## 🔧 Prerequisites

- Java JDK 11+ installed  
- MySQL Server running with database named `payroll`  
- JFreeChart library added to your project  
- MySQL JDBC Driver added to your project
---


## 🗃️ Database Setup
```sql
CREATE DATABASE payroll;

USE payroll;

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    pay_level INT,
    salary DOUBLE,
    da_percentage DOUBLE,
    hra_percentage DOUBLE,
    other_allowances DOUBLE,
    deductions DOUBLE
);

CREATE TABLE attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    emp_id INT,
    date DATE,
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);

CREATE TABLE pay_levels (
    level INT NOT NULL PRIMARY KEY,
    basic_salary DECIMAL(10, 2)
);
```
---
## 🧩 Custom Dialog Utility
CustomDialog.java includes reusable wrappers for JOptionPane:

1. showInput()
2. showMessage()
3. showError()
4. showConfirm()


