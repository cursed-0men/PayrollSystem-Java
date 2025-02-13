import javax.swing.*;
import java.awt.*;


import java.sql.*;

public class LoginSignupForm {
    private JFrame frame;
    private JPanel panel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, toggleButton, forgotPasswordButton;
    private boolean isLogin = true;

    public LoginSignupForm() {
        frame = new JFrame("Payroll Management - Login/Signup");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.decode("#4A90E2"), getWidth(), getHeight(), Color.decode("#D9E021"));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Payroll Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 30, 300, 40);
        panel.add(titleLabel);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 100, 300, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(50, 125, 300, 30);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 170, 300, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 195, 300, 30);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(50, 250, 140, 40);
        panel.add(loginButton);

        signupButton = new JButton("Sign Up");
        signupButton.setBounds(210, 250, 140, 40);
        signupButton.setVisible(false);
        panel.add(signupButton);

        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setBounds(50, 300, 300, 30);
        panel.add(forgotPasswordButton);

        toggleButton = new JButton("Don't have an account? Sign Up");
        toggleButton.setBounds(50, 340, 300, 30);
        panel.add(toggleButton);

        frame.add(panel);
        frame.setVisible(true);

        toggleButton.addActionListener(e -> {
            isLogin = !isLogin;
            loginButton.setVisible(isLogin);
            signupButton.setVisible(!isLogin);
            toggleButton.setText(isLogin ? "Don't have an account? Sign Up" : "Already have an account? Login");
        });

        loginButton.addActionListener(e -> loginUser());
        signupButton.addActionListener(e -> signupUser());
        forgotPasswordButton.addActionListener(e -> forgotPassword());
    }

    private void loginUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_db", "root", "password");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE email=? AND password=?")) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(frame, "Login Successful");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database Error");
        }
    }

    private void signupUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_db", "root", "password");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (email, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(frame, "Signup Successful. You can login now.");
                isLogin = true;
                loginButton.setVisible(true);
                signupButton.setVisible(false);
                toggleButton.setText("Don't have an account? Sign Up");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Signup Failed. Email might already exist.");
        }
    }

    private void forgotPassword() {
        String email = JOptionPane.showInputDialog(frame, "Enter your registered email:");
        if (email != null && !email.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Password reset link sent to " + email);
        } else {
            JOptionPane.showMessageDialog(frame, "Please enter a valid email.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginSignupForm::new);
    }
}
