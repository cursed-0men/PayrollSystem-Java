import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PayrollManagementSystem {
    private JFrame frame;
    private JPanel panel;
    private JButton addEmployeeBtn, viewEmployeesBtn, calculateSalaryBtn, updateSalaryBtn, exitBtn;
    private Connection conn;

    public PayrollManagementSystem() {
        frame = new JFrame("Payroll Management System");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(50, 50, 50));

        addEmployeeBtn = createStyledButton("Add Employee");
        viewEmployeesBtn = createStyledButton("View Employees");
        calculateSalaryBtn = createStyledButton("Calculate Salary");
        updateSalaryBtn = createStyledButton("Update Employee Salary");
        exitBtn = createStyledButton("Exit");

        panel.add(addEmployeeBtn);
        panel.add(viewEmployeesBtn);
        panel.add(calculateSalaryBtn);
        panel.add(updateSalaryBtn);
        panel.add(exitBtn);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        connectDatabase();

        addEmployeeBtn.addActionListener(e -> addEmployee());
        viewEmployeesBtn.addActionListener(e -> viewEmployees());
        calculateSalaryBtn.addActionListener(e -> calculateSalary());
        updateSalaryBtn.addActionListener(e -> updateSalary());
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return button;
    }

    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/payroll?useSSL=false&allowPublicKeyRetrieval=true",
                "root", "Qwerty@123"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployee() {
        String name = JOptionPane.showInputDialog("Enter Employee Name:");
        String payLevel = JOptionPane.showInputDialog("Enter Pay Level (1-10):");
        String salary = JOptionPane.showInputDialog("Enter Initial Salary:");

        try {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO employees (name, pay_level, salary, da_percentage, hra_percentage, other_allowances, deductions) " +
                "VALUES (?, ?, ?, 38.0, 24.0, 0.0, 0.0)"
            );
            ps.setString(1, name);
            ps.setInt(2, Integer.parseInt(payLevel));
            ps.setDouble(3, Double.parseDouble(salary));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Employee Added Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error Adding Employee!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewEmployees() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

            StringBuilder employees = new StringBuilder("ID | Name | Pay Level | DA% | HRA% | Allowances | Deductions\n");
            while (rs.next()) {
                employees.append(rs.getInt("id")).append(" | ")
                         .append(rs.getString("name")).append(" | ")
                         .append(rs.getInt("pay_level")).append(" | ")
                         .append(rs.getDouble("da_percentage")).append("% | ")
                         .append(rs.getDouble("hra_percentage")).append("% | ")
                         .append(rs.getDouble("other_allowances")).append(" | ")
                         .append(rs.getDouble("deductions")).append("\n");
            }

            JOptionPane.showMessageDialog(frame, employees.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error Fetching Employees!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateSalary() {
        String id = JOptionPane.showInputDialog("Enter Employee ID:");
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT salary, da_percentage, hra_percentage, other_allowances, deductions " +
                "FROM employees WHERE id=?"
            );
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double basicSalary = rs.getDouble("salary");
                double da = (rs.getDouble("da_percentage") / 100) * basicSalary;
                double hra = (rs.getDouble("hra_percentage") / 100) * basicSalary;
                double otherAllowances = rs.getDouble("other_allowances");
                double deductions = rs.getDouble("deductions");
                double netSalary = basicSalary + da + hra + otherAllowances - deductions;

                JOptionPane.showMessageDialog(frame, "Net Salary: ₹" + netSalary);
            } else {
                JOptionPane.showMessageDialog(frame, "Employee Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error Calculating Salary!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSalary() {
        String id = JOptionPane.showInputDialog("Enter Employee ID to Update:");
        String newPayLevel = JOptionPane.showInputDialog("Enter New Pay Level:");
        String newDA = JOptionPane.showInputDialog("Enter New DA%:");
        String newHRA = JOptionPane.showInputDialog("Enter New HRA%:");
        String newAllowances = JOptionPane.showInputDialog("Enter Other Allowances:");
        String newDeductions = JOptionPane.showInputDialog("Enter New Deductions:");

        try {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE employees SET pay_level=?, da_percentage=?, hra_percentage=?, other_allowances=?, deductions=? WHERE id=?"
            );
            ps.setInt(1, Integer.parseInt(newPayLevel));
            ps.setDouble(2, Double.parseDouble(newDA));
            ps.setDouble(3, Double.parseDouble(newHRA));
            ps.setDouble(4, Double.parseDouble(newAllowances));
            ps.setDouble(5, Double.parseDouble(newDeductions));
            ps.setInt(6, Integer.parseInt(id));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Employee Salary Updated Successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error Updating Salary!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new PayrollManagementSystem();
    }
}