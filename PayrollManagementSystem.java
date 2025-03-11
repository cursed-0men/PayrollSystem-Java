import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
import java.sql.*;

public class PayrollManagementSystem {
    private JFrame frame;
    private JPanel panel;
    private JButton addEmployeeBtn, viewEmployeesBtn, calculateSalaryBtn, exitBtn;
    private Connection conn;

    public PayrollManagementSystem() {
        frame = new JFrame("Payroll Management System");
        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        
        addEmployeeBtn = new JButton("Add Employee");
        viewEmployeesBtn = new JButton("View Employees");
        calculateSalaryBtn = new JButton("Calculate Salary");
        exitBtn = new JButton("Exit");
        
        panel.add(addEmployeeBtn);
        panel.add(viewEmployeesBtn);
        panel.add(calculateSalaryBtn);
        panel.add(exitBtn);
        
        frame.add(panel);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        connectDatabase();
        
        addEmployeeBtn.addActionListener(e -> addEmployee());
        viewEmployeesBtn.addActionListener(e -> viewEmployees());
        calculateSalaryBtn.addActionListener(e -> calculateSalary());
        exitBtn.addActionListener(e -> System.exit(0));
    }
    
    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll?useSSL=false&allowPublicKeyRetrieval=true", "root", "Qwerty@123");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Database Connection Failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addEmployee() {
        String name = JOptionPane.showInputDialog("Enter Employee Name:");
        String salary = JOptionPane.showInputDialog("Enter Basic Salary:");
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO employees (name, salary) VALUES (?, ?)");
            ps.setString(1, name);
            ps.setDouble(2, Double.parseDouble(salary));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Employee Added Successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error Adding Employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewEmployees() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
            StringBuilder employees = new StringBuilder("ID | Name | Salary\n");
            while (rs.next()) {
                employees.append(rs.getInt("id")).append(" | ")
                         .append(rs.getString("name")).append(" | ")
                         .append(rs.getDouble("salary")).append("\n");
            }
            JOptionPane.showMessageDialog(frame, employees.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error Fetching Employees!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void calculateSalary() {
        String id = JOptionPane.showInputDialog("Enter Employee ID:");
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT salary FROM employees WHERE id=?");
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double basicSalary = rs.getDouble("salary");
                double hra = basicSalary * 0.1;
                double da = basicSalary * 0.05;
                double tax = basicSalary * 0.02;
                double netSalary = basicSalary + hra + da - tax;
                JOptionPane.showMessageDialog(frame, "Net Salary: " + netSalary);
            } else {
                JOptionPane.showMessageDialog(frame, "Employee Not Found!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error Calculating Salary!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        new PayrollManagementSystem();
    }
}
