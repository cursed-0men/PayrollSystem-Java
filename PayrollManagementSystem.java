import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PayrollManagementSystem {
    private JFrame frame;
    private JPanel panel, headerPanel;
    private JButton addEmployeeBtn, viewEmployeesBtn, calculateSalaryBtn, updateSalaryBtn, searchBtn, deleteBtn, clearBtn, exitBtn;
    private Connection conn;

    public PayrollManagementSystem() {
        frame = new JFrame("Payroll Management System");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Gradient header
        headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.BLUE, getWidth(), getHeight(), Color.CYAN);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(600, 70));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Payroll Management System");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Button panel
        panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(Color.DARK_GRAY);

        addEmployeeBtn = createStyledButton("Add Employee");
        viewEmployeesBtn = createStyledButton("View Employees");
        calculateSalaryBtn = createStyledButton("Calculate Salary");
        updateSalaryBtn = createStyledButton("Update Employee Salary");
        searchBtn = createStyledButton("Search Employee");
        deleteBtn = createStyledButton("Delete Employee");
        clearBtn = createStyledButton("Clear All Fields");
        exitBtn = createStyledButton("Exit");

        addToolTip(addEmployeeBtn, "Add a new employee");
        addToolTip(viewEmployeesBtn, "View all employees");
        addToolTip(calculateSalaryBtn, "Calculate employee's net salary");
        addToolTip(updateSalaryBtn, "Update employee's salary components");
        addToolTip(searchBtn, "Search employee by ID");
        addToolTip(deleteBtn, "Delete employee from database");
        addToolTip(clearBtn, "Clear inputs (not applicable in dialog-based UI)");
        addToolTip(exitBtn, "Close the application");

        panel.add(addEmployeeBtn);
        panel.add(viewEmployeesBtn);
        panel.add(calculateSalaryBtn);
        panel.add(updateSalaryBtn);
        panel.add(searchBtn);
        panel.add(deleteBtn);
        panel.add(clearBtn);
        panel.add(exitBtn);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        connectDatabase();

        // Event Listeners
        addEmployeeBtn.addActionListener(e -> addEmployee());
        viewEmployeesBtn.addActionListener(e -> viewEmployees());
        calculateSalaryBtn.addActionListener(e -> calculateSalary());
        updateSalaryBtn.addActionListener(e -> updateSalary());
        searchBtn.addActionListener(e -> searchEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        clearBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "No fields to clear in this mode."));
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215));
            }
        });
        return button;
    }

    private void addToolTip(JButton btn, String text) {
        btn.setToolTipText(text);
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
        if (name == null || name.trim().isEmpty()) return;

        String payLevelInput = JOptionPane.showInputDialog("Enter Pay Level (1-10):");
        if (payLevelInput == null || payLevelInput.trim().isEmpty()) return;

        String salaryInput = JOptionPane.showInputDialog("Enter Initial Salary:");
        if (salaryInput == null || salaryInput.trim().isEmpty()) salaryInput = "0.0";

        try {
            int payLevel = Integer.parseInt(payLevelInput.trim());
            double salary = Double.parseDouble(salaryInput.trim());

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO employees (name, pay_level, salary, da_percentage, hra_percentage, other_allowances, deductions) " +
                "VALUES (?, ?, ?, 38.0, 24.0, 0.0, 0.0)"
            );
            ps.setString(1, name.trim());
            ps.setInt(2, payLevel);
            ps.setDouble(3, salary);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Employee Added Successfully!");
        } catch (NumberFormatException ne) {
            JOptionPane.showMessageDialog(frame, "Invalid number input!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
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
        if (id == null || id.trim().isEmpty()) return;

        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT salary, da_percentage, hra_percentage, other_allowances, deductions FROM employees WHERE id=?"
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
            JOptionPane.showMessageDialog(frame, "Error Calculating Salary!\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSalary() {
        String id = JOptionPane.showInputDialog("Enter Employee ID to Update:");
        if (id == null || id.trim().isEmpty()) return;

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

    private void searchEmployee() {
        String id = JOptionPane.showInputDialog("Enter Employee ID:");
        if (id == null || id.trim().isEmpty()) return;

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM employees WHERE id=?");
            ps.setInt(1, Integer.parseInt(id));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String info = "Name: " + rs.getString("name") +
                              "\nPay Level: " + rs.getInt("pay_level") +
                              "\nDA: " + rs.getDouble("da_percentage") + "%" +
                              "\nHRA: " + rs.getDouble("hra_percentage") + "%" +
                              "\nOther Allowances: " + rs.getDouble("other_allowances") +
                              "\nDeductions: " + rs.getDouble("deductions");
                JOptionPane.showMessageDialog(frame, info);
            } else {
                JOptionPane.showMessageDialog(frame, "Employee Not Found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error Searching Employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEmployee() {
        String id = JOptionPane.showInputDialog("Enter Employee ID to Delete:");
        if (id == null || id.trim().isEmpty()) return;

        try {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure to delete employee ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM employees WHERE id=?");
                ps.setInt(1, Integer.parseInt(id));
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Employee Deleted Successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Employee Not Found!", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error Deleting Employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new PayrollManagementSystem();
    }
}
