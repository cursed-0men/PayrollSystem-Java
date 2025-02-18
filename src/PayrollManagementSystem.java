import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PayrollManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/payroll";
    private static final String USER = "root";
    private static final String PASSWORD = "Qwerty@123";

    public static void main(String[] args) {
        // testConnection();
        SwingUtilities.invokeLater(PayrollManagementSystem::createLoginGUI);
    }

    private static void createLoginGUI() {
        JFrame frame = new JFrame("Payroll Management System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);  // Center the window
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0, 102, 204));

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        // Styling the login form
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.BLACK);  // Changed text color to black
        loginButton.setFocusPainted(false);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (authenticateUser(username, password)) {
                frame.dispose();
                openMainMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(loginButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static boolean authenticateUser(String username, String password) {
        String hardcodedUsername = "admin";
        String hardcodedPassword = "123123123";
        return username.equals(hardcodedUsername) && password.equals(hardcodedPassword);
    }

    private static void openMainMenu() {
        JFrame frame = new JFrame("Payroll Management System - Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);  // Center the window
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.setBackground(new Color(245, 245, 245));

        JButton manageEmployeesButton = new JButton("Manage Employees");
        JButton manageAttendanceButton = new JButton("Manage Attendance");
        JButton viewPayrollButton = new JButton("View Payroll");
        JButton logoutButton = new JButton("Logout");

        // Styling the buttons
        manageEmployeesButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        manageAttendanceButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        viewPayrollButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

        manageEmployeesButton.setBackground(new Color(0, 102, 204));
        manageAttendanceButton.setBackground(new Color(0, 102, 204));
        viewPayrollButton.setBackground(new Color(0, 102, 204));
        logoutButton.setBackground(new Color(255, 0, 0));

        manageEmployeesButton.setForeground(Color.BLACK);  // Changed text color to black
        manageAttendanceButton.setForeground(Color.BLACK);  // Changed text color to black
        viewPayrollButton.setForeground(Color.BLACK);  // Changed text color to black
        logoutButton.setForeground(Color.BLACK);  // Changed text color to black

        manageEmployeesButton.setFocusPainted(false);
        manageAttendanceButton.setFocusPainted(false);
        viewPayrollButton.setFocusPainted(false);
        logoutButton.setFocusPainted(false);

        panel.add(manageEmployeesButton);
        panel.add(manageAttendanceButton);
        panel.add(viewPayrollButton);
        panel.add(logoutButton);

        frame.add(panel);
        frame.setVisible(true);

        manageEmployeesButton.addActionListener(e -> manageEmployees());

        // manageAttendanceButton.addActionListener(e -> manageAttendance());
        
        // viewPayrollButton.addActionListener(e -> viewPayroll());
        logoutButton.addActionListener(e -> {
            frame.dispose();
            createLoginGUI();
        });
    }

    private static void manageEmployees() {
        JFrame frame = new JFrame("Manage Employees");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);  // Center the window
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField dobField = new JTextField("YYYY-MM-DD", 20);
        JTextField joiningDateField = new JTextField("YYYY-MM-DD", 20);
        JTextField salaryField = new JTextField(20);
        JTextField roleField = new JTextField(20);
        JTextField bankNoField = new JTextField(20);

        JButton addButton = new JButton("Add Employee");
        JButton updateButton = new JButton("Update Salary/Role");
        JButton deleteButton = new JButton("Delete Employee");
        JButton viewButton = new JButton("View Employees");

        // Styling the form fields and buttons
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        ageField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dobField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        joiningDateField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        salaryField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        bankNoField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        viewButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

        addButton.setBackground(new Color(0, 102, 204));
        updateButton.setBackground(new Color(0, 102, 204));
        deleteButton.setBackground(new Color(0, 102, 204));
        viewButton.setBackground(new Color(0, 102, 204));

        addButton.setForeground(Color.BLACK);  // Changed text color to black
        updateButton.setForeground(Color.BLACK);  // Changed text color to black
        deleteButton.setForeground(Color.BLACK);  // Changed text color to black
        viewButton.setForeground(Color.BLACK);  // Changed text color to black

        addButton.setFocusPainted(false);
        updateButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);
        viewButton.setFocusPainted(false);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("DOB:"));
        panel.add(dobField);
        panel.add(new JLabel("Date of Joining:"));
        panel.add(joiningDateField);
        panel.add(new JLabel("Salary:"));
        panel.add(salaryField);
        panel.add(new JLabel("Role:"));
        panel.add(roleField);
        panel.add(new JLabel("Bank No:"));
        panel.add(bankNoField);

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(viewButton);

        frame.add(panel);
        frame.setVisible(true);

        addButton.addActionListener(e -> addEmployee(
            nameField.getText(),
            ageField.getText(),
            dobField.getText(),
            joiningDateField.getText(),
            salaryField.getText(),
            roleField.getText(),
            bankNoField.getText()
        ));
        updateButton.addActionListener(e -> updateEmployee(
            nameField.getText(),
            salaryField.getText(),
            roleField.getText()
        ));
        deleteButton.addActionListener(e -> deleteEmployee(nameField.getText()));
        viewButton.addActionListener(e -> viewEmployees());
    }

    private static void addEmployee(String name, String age, String dob, String joiningDate, String salary, String role, String bankNo) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO employees (name, age, dob, joining_date, salary, role, bank_no) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, age);
                statement.setString(3, dob);
                statement.setString(4, joiningDate);
                statement.setString(5, salary);
                statement.setString(6, role);
                statement.setString(7, bankNo);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Employee added successfully");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding employee");
        }
    }

    private static void updateEmployee(String name, String salary, String role) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "UPDATE employees SET salary = ?, role = ? WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, salary);
                statement.setString(2, role);
                statement.setString(3, name);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "Employee updated successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Employee not found");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating employee");
        }
    }

    private static void deleteEmployee(String name) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "DELETE FROM employees WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "Employee deleted successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Employee not found");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting employee");
        }
    }

    private static void viewEmployees() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM employees";
            try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(query)) {
                StringBuilder employees = new StringBuilder();
                while (rs.next()) {
                    employees.append("Name: ").append(rs.getString("name")).append("\n");
                    employees.append("Age: ").append(rs.getInt("age")).append("\n");
                    employees.append("DOB: ").append(rs.getDate("dob")).append("\n");
                    employees.append("Joining Date: ").append(rs.getDate("joining_date")).append("\n");
                    employees.append("Salary: ").append(rs.getString("salary")).append("\n");
                    employees.append("Role: ").append(rs.getString("role")).append("\n");
                    employees.append("Bank No: ").append(rs.getString("bank_no")).append("\n");
                    employees.append("-----------------------------------\n");
                }
                JOptionPane.showMessageDialog(null, employees.toString(), "Employee List", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching employees.");
        }
    }
}
