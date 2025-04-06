import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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

        // Gradient header panel
        headerPanel = new JPanel() {
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

        // Panel with buttons
        panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.setBackground(new Color(45, 45, 45)); // Dark background

        addEmployeeBtn = createStyledButton("Add Employee");
        viewEmployeesBtn = createStyledButton("View Employees");
        calculateSalaryBtn = createStyledButton("Calculate Salary");
        updateSalaryBtn = createStyledButton("Update Employee Salary");
        searchBtn = createStyledButton("Search Employee");
        deleteBtn = createStyledButton("Delete Employee");
        clearBtn = createStyledButton("Clear All Fields");
        exitBtn = createStyledButton("Exit");

        // Tooltips
        addToolTip(addEmployeeBtn, "Add a new employee");
        addToolTip(viewEmployeesBtn, "View all employees");
        addToolTip(calculateSalaryBtn, "Calculate net salary");
        addToolTip(updateSalaryBtn, "Update salary components");
        addToolTip(searchBtn, "Search employee by ID");
        addToolTip(deleteBtn, "Delete employee by ID");
        addToolTip(clearBtn, "Clear inputs (not applicable)");
        addToolTip(exitBtn, "Exit application");

        // Add buttons to panel
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

        // Button Actions
        addEmployeeBtn.addActionListener(e -> addEmployee());
        viewEmployeesBtn.addActionListener(e -> viewEmployees());
        calculateSalaryBtn.addActionListener(e -> calculateSalary());
        updateSalaryBtn.addActionListener(e -> updateSalary());
        searchBtn.addActionListener(e -> searchEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        clearBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "No input fields to clear."));
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(30, 136, 229)); // Blue shade
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(50, 158, 255));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(30, 136, 229));
            }
        });
        return button;
    }

    private void addToolTip(JButton btn, String text) {
        btn.setToolTipText(text);
    }

    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll?useSSL=false", "root", "Qwerty@123");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEmployee() {
        try {
            String name = prompt("Enter Employee Name:");
            String payLevelInput = prompt("Enter Pay Level (1-10):");
            String salaryInput = prompt("Enter Initial Salary:");

            int payLevel = Integer.parseInt(payLevelInput);
            double salary = Double.parseDouble(salaryInput);

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO employees (name, pay_level, salary, da_percentage, hra_percentage, other_allowances, deductions) " +
                "VALUES (?, ?, ?, 38.0, 24.0, 0.0, 0.0)"
            );
            ps.setString(1, name);
            ps.setInt(2, payLevel);
            ps.setDouble(3, salary);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Employee added successfully.");
        } catch (Exception e) {
            showError("Error Adding Employee", e);
        }
    }

    private void viewEmployees() {
    try {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Column names
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Data
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(frame, scrollPane, "Employee Records", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        showError("Error Fetching Employees", e);
    }
}

    
        
    private void calculateSalary() {
        try {
            int id = Integer.parseInt(prompt("Enter Employee ID:"));
            PreparedStatement ps = conn.prepareStatement(
                "SELECT salary, da_percentage, hra_percentage, other_allowances, deductions FROM employees WHERE id=?"
            );
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double basic = rs.getDouble("salary");
                double da = (rs.getDouble("da_percentage") / 100) * basic;
                double hra = (rs.getDouble("hra_percentage") / 100) * basic;
                double others = rs.getDouble("other_allowances");
                double deductions = rs.getDouble("deductions");
                double net = basic + da + hra + others - deductions;

                JOptionPane.showMessageDialog(frame, String.format("Net Salary: ₹%.2f", net));
            } else {
                JOptionPane.showMessageDialog(frame, "Employee not found.");
            }
        } catch (Exception e) {
            showError("Error Calculating Salary", e);
        }
    }

    private void updateSalary() {
        try {
            int id = Integer.parseInt(prompt("Enter Employee ID to update:"));
            int level = Integer.parseInt(prompt("Enter new Pay Level:"));
            double da = Double.parseDouble(prompt("Enter new DA%:"));
            double hra = Double.parseDouble(prompt("Enter new HRA%:"));
            double allowances = Double.parseDouble(prompt("Enter Other Allowances:"));
            double deductions = Double.parseDouble(prompt("Enter New Deductions:"));

            PreparedStatement ps = conn.prepareStatement(
                "UPDATE employees SET pay_level=?, da_percentage=?, hra_percentage=?, other_allowances=?, deductions=? WHERE id=?"
            );
            ps.setInt(1, level);
            ps.setDouble(2, da);
            ps.setDouble(3, hra);
            ps.setDouble(4, allowances);
            ps.setDouble(5, deductions);
            ps.setInt(6, id);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Salary updated successfully.");
        } catch (Exception e) {
            showError("Error Updating Salary", e);
        }
    }

    private void searchEmployee() {
        try {
            int id = Integer.parseInt(prompt("Enter Employee ID:"));
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM employees WHERE id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String info = "Name: " + rs.getString("name") +
                        "\nPay Level: " + rs.getInt("pay_level") +
                        "\nDA: " + rs.getDouble("da_percentage") + "%" +
                        "\nHRA: " + rs.getDouble("hra_percentage") + "%" +
                        "\nAllowances: " + rs.getDouble("other_allowances") +
                        "\nDeductions: " + rs.getDouble("deductions");
                JOptionPane.showMessageDialog(frame, info);
            } else {
                JOptionPane.showMessageDialog(frame, "Employee not found.");
            }
        } catch (Exception e) {
            showError("Error Searching Employee", e);
        }
    }

    private void deleteEmployee() {
        try {
            int id = Integer.parseInt(prompt("Enter Employee ID to delete:"));
            int confirm = JOptionPane.showConfirmDialog(frame, "Delete employee ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM employees WHERE id=?");
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame, "Employee deleted.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Employee not found.");
                }
            }
        } catch (Exception e) {
            showError("Error Deleting Employee", e);
        }
    }

    private String prompt(String message) {
        String input = JOptionPane.showInputDialog(message);
        if (input == null || input.trim().isEmpty()) throw new IllegalArgumentException("Input cannot be empty");
        return input.trim();
    }

    private void showError(String title, Exception e) {
        JOptionPane.showMessageDialog(frame, title + ":\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayrollManagementSystem::new);
    }
}
