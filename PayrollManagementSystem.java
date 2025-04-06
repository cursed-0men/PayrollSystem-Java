import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class PayrollManagementSystem {
    private JFrame frame;
    private JPanel panel, headerPanel;
    private JButton addEmployeeBtn, viewEmployeesBtn, calculateSalaryBtn, updateSalaryBtn, searchBtn, deleteBtn, clearBtn, exitBtn, attendanceBtn;
    private Connection conn;

    public PayrollManagementSystem() {
        frame = new JFrame("Payroll Management System");
        frame.setSize(800, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(25, 25, 35));

        headerPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(58, 123, 213), getWidth(), getHeight(), new Color(0, 210, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(800, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titleLabel = new JLabel("Payroll Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        panel = new JPanel(new GridLayout(5, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        panel.setBackground(new Color(25, 25, 35));

        addEmployeeBtn = createStyledButton("Add Employee");
        viewEmployeesBtn = createStyledButton("View Employees");
        calculateSalaryBtn = createStyledButton("Calculate Salary");
        updateSalaryBtn = createStyledButton("Update Employee Salary");
        searchBtn = createStyledButton("Search Employee");
        deleteBtn = createStyledButton("Delete Employee");
        clearBtn = createStyledButton("Clear All Fields");
        exitBtn = createStyledButton("Exit");
        attendanceBtn = createStyledButton("Mark Attendance");

        addToolTip(addEmployeeBtn, "Add a new employee");
        addToolTip(viewEmployeesBtn, "View all employees");
        addToolTip(calculateSalaryBtn, "Calculate net salary");
        addToolTip(updateSalaryBtn, "Update salary components");
        addToolTip(searchBtn, "Search employee by ID");
        addToolTip(deleteBtn, "Delete employee by ID");
        addToolTip(clearBtn, "Clear inputs (not applicable)");
        addToolTip(exitBtn, "Exit application");
        addToolTip(attendanceBtn, "Mark employee attendance");

        panel.add(addEmployeeBtn);
        panel.add(viewEmployeesBtn);
        panel.add(calculateSalaryBtn);
        panel.add(updateSalaryBtn);
        panel.add(searchBtn);
        panel.add(deleteBtn);
        panel.add(clearBtn);
        panel.add(exitBtn);
        panel.add(attendanceBtn);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        connectDatabase();

        addEmployeeBtn.addActionListener(e -> addEmployee());
        viewEmployeesBtn.addActionListener(e -> viewEmployees());
        calculateSalaryBtn.addActionListener(e -> calculateSalary());
        updateSalaryBtn.addActionListener(e -> updateSalary());
        searchBtn.addActionListener(e -> searchEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());
        attendanceBtn.addActionListener(e -> markAttendance());
        clearBtn.addActionListener(e -> CustomDialog.showMessage(frame, "No input fields to clear."));
        exitBtn.addActionListener(e -> {
            int confirm = CustomDialog.showConfirm(frame, "Are you sure you want to exit?", "Exit Confirmation");
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(40, 170, 225));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 191, 255));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(40, 170, 225));
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
            CustomDialog.showError(frame, "Database connection failed!\n" + e.getMessage());
        }
    }

    private void addEmployee() {
        try {
            String name = CustomDialog.showInput(frame, "Enter Employee Name:");
            int payLevel = Integer.parseInt(CustomDialog.showInput(frame, "Enter Pay Level (1-10):"));
            double salary = Double.parseDouble(CustomDialog.showInput(frame, "Enter Initial Salary:"));

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO employees (name, pay_level, salary, da_percentage, hra_percentage, other_allowances, deductions) " +
                            "VALUES (?, ?, ?, 38.0, 24.0, 0.0, 0.0)"
            );
            ps.setString(1, name);
            ps.setInt(2, payLevel);
            ps.setDouble(3, salary);
            ps.executeUpdate();

            CustomDialog.showMessage(frame, "Employee added successfully.");
        } catch (Exception e) {
            CustomDialog.showError(frame, "Error Adding Employee\n" + e.getMessage());
        }
    }

    private void viewEmployees() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

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
            scrollPane.setPreferredSize(new Dimension(700, 300));

            JOptionPane.showMessageDialog(frame, scrollPane, "Employee Records", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            CustomDialog.showError(frame, "Error Fetching Employees\n" + e.getMessage());
        }
    }

    private void calculateSalary() {
        try {
            int id = Integer.parseInt(CustomDialog.showInput(frame, "Enter Employee ID:"));
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

                CustomDialog.showMessage(frame, String.format("Net Salary: ₹%.2f", net));
            } else {
                CustomDialog.showMessage(frame, "Employee not found.");
            }
        } catch (Exception e) {
            CustomDialog.showError(frame, "Error Calculating Salary\n" + e.getMessage());
        }
    }

    private void updateSalary() {
        try {
            int id = Integer.parseInt(CustomDialog.showInput(frame, "Enter Employee ID to update:"));
            int level = Integer.parseInt(CustomDialog.showInput(frame, "Enter new Pay Level:"));
            double da = Double.parseDouble(CustomDialog.showInput(frame, "Enter new DA%:"));
            double hra = Double.parseDouble(CustomDialog.showInput(frame, "Enter new HRA%:"));
            double allowances = Double.parseDouble(CustomDialog.showInput(frame, "Enter Other Allowances:"));
            double deductions = Double.parseDouble(CustomDialog.showInput(frame, "Enter New Deductions:"));

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
            CustomDialog.showMessage(frame, "Salary updated successfully.");
        } catch (Exception e) {
            CustomDialog.showError(frame, "Error Updating Salary\n" + e.getMessage());
        }
    }

    private void searchEmployee() {
        try {
            int id = Integer.parseInt(CustomDialog.showInput(frame, "Enter Employee ID:"));
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
                CustomDialog.showMessage(frame, info);
            } else {
                CustomDialog.showMessage(frame, "Employee not found.");
            }
        } catch (Exception e) {
            CustomDialog.showError(frame, "Error Searching Employee\n" + e.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            int id = Integer.parseInt(CustomDialog.showInput(frame, "Enter Employee ID to delete:"));
            int confirm = CustomDialog.showConfirm(frame, "Delete employee ID " + id + "?", "Confirm Delete");
            if (confirm == JOptionPane.YES_OPTION) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM employees WHERE id=?");
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    CustomDialog.showMessage(frame, "Employee deleted.");
                } else {
                    CustomDialog.showMessage(frame, "Employee not found.");
                }
            }
        } catch (Exception e) {
            CustomDialog.showError(frame, "Error Deleting Employee\n" + e.getMessage());
        }
    }

    private void markAttendance() {
        try {
            int id = Integer.parseInt(CustomDialog.showInput(frame, "Enter Employee ID:"));
            String[] options = {"Present", "Absent"};
            int choice = JOptionPane.showOptionDialog(frame, "Mark Attendance:", "Attendance",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice != -1) {
                String status = options[choice];
                LocalDate today = LocalDate.now();

                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO attendance (employee_id, date, status) VALUES (?, ?, ?)"
                );
                ps.setInt(1, id);
                ps.setDate(2, Date.valueOf(today));
                ps.setString(3, status);
                ps.executeUpdate();

                CustomDialog.showMessage(frame, "Attendance marked as " + status + " for " + today + ".");
            }
        } catch (Exception e) {
            CustomDialog.showError(frame, "Error Marking Attendance\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayrollManagementSystem::new);
    }
}

// ---------------------------
// Custom Dialog Class
// ---------------------------
class CustomDialog {
    static Color bgColor = new Color(30, 30, 50);
    static Color fgColor = Color.WHITE;

    public static String showInput(Component parent, String message) {
        UIManager.put("OptionPane.background", bgColor);
        UIManager.put("Panel.background", bgColor);
        UIManager.put("OptionPane.messageForeground", fgColor);
        return JOptionPane.showInputDialog(parent, message);
    }

    public static void showMessage(Component parent, String message) {
        UIManager.put("OptionPane.background", bgColor);
        UIManager.put("Panel.background", bgColor);
        UIManager.put("OptionPane.messageForeground", fgColor);
        JOptionPane.showMessageDialog(parent, message);
    }

    public static void showError(Component parent, String message) {
        UIManager.put("OptionPane.background", bgColor);
        UIManager.put("Panel.background", bgColor);
        UIManager.put("OptionPane.messageForeground", Color.RED);
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static int showConfirm(Component parent, String message, String title) {
        UIManager.put("OptionPane.background", bgColor);
        UIManager.put("Panel.background", bgColor);
        UIManager.put("OptionPane.messageForeground", fgColor);
        return JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION);
    }
}
