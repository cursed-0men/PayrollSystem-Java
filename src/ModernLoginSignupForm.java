import javax.swing.*;
import java.awt.*;

public class ModernLoginSignupForm {
    private JFrame frame;
    private JPanel loginPanel, signupPanel;
    private JTextField emailField, signupNameField, signupEmailField;
    private JPasswordField passwordField, signupPasswordField;
    private JButton loginButton, signupButton, forgotPasswordButton, registerButton, backButton;

    public ModernLoginSignupForm() {
        frame = new JFrame("Payroll Management - Login");
        frame.setSize(450, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        initLoginPanel();
        initSignupPanel();

        frame.add(loginPanel);
        frame.setVisible(true);
    }

    private void initLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(Color.decode("#FFD54F")); // Yellow background

        JLabel titleLabel = new JLabel("Payroll Mgmt System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(50, 30, 350, 40);
        loginPanel.add(titleLabel);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setBounds(50, 100, 300, 25);
        loginPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(50, 125, 350, 40);
        emailField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        loginPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setBounds(50, 180, 300, 25);
        loginPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(50, 205, 350, 40);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        loginPanel.add(passwordField);

        forgotPasswordButton = new JButton("Forgot password");
        forgotPasswordButton.setBounds(300, 250, 120, 25);
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setForeground(Color.BLACK);
        forgotPasswordButton.addActionListener(e -> forgotPassword());
        loginPanel.add(forgotPasswordButton);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 300, 250, 50);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(Color.BLACK);
        loginButton.addActionListener(e -> loginUser());
        loginPanel.add(loginButton);

        signupButton = new JButton("Sign Up");
        signupButton.setBounds(100, 370, 250, 50);
        signupButton.setFont(new Font("Arial", Font.BOLD, 18));
        signupButton.setBackground(Color.decode("#4C66FF")); // Blue
        signupButton.setForeground(Color.WHITE);
        signupButton.addActionListener(e -> switchToSignup());
        loginPanel.add(signupButton);
    }

    private void initSignupPanel() {
        signupPanel = new JPanel();
        signupPanel.setLayout(null);
        signupPanel.setBackground(Color.decode("#FFD54F")); // Yellow background

        JLabel titleLabel = new JLabel("Sign Up", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(50, 30, 350, 40);
        signupPanel.add(titleLabel);

        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setBounds(50, 100, 300, 25);
        signupPanel.add(nameLabel);

        signupNameField = new JTextField();
        signupNameField.setBounds(50, 125, 350, 40);
        signupNameField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        signupPanel.add(signupNameField);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setBounds(50, 180, 300, 25);
        signupPanel.add(emailLabel);

        signupEmailField = new JTextField();
        signupEmailField.setBounds(50, 205, 350, 40);
        signupEmailField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        signupPanel.add(signupEmailField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setBounds(50, 260, 300, 25);
        signupPanel.add(passwordLabel);

        signupPasswordField = new JPasswordField();
        signupPasswordField.setBounds(50, 285, 350, 40);
        signupPasswordField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        signupPanel.add(signupPasswordField);

        registerButton = new JButton("Register");
        registerButton.setBounds(100, 350, 250, 50);
        registerButton.setFont(new Font("Arial", Font.BOLD, 18));
        registerButton.setBackground(Color.decode("#4C66FF")); // Blue
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> registerUser());
        signupPanel.add(registerButton);

        backButton = new JButton("Back to Login");
        backButton.setBounds(100, 420, 250, 50);
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(e -> switchToLogin());
        signupPanel.add(backButton);
    }

    private void switchToSignup() {
        frame.remove(loginPanel);
        frame.add(signupPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void switchToLogin() {
        frame.remove(signupPanel);
        frame.add(loginPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void loginUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
            return;
        }
        JOptionPane.showMessageDialog(frame, "Login Attempted with:\nEmail: " + email);
    }

    private void registerUser() {
        String name = signupNameField.getText();
        String email = signupEmailField.getText();
        String password = new String(signupPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
            return;
        }
        JOptionPane.showMessageDialog(frame, "Signup Successful!\nWelcome, " + name);
        switchToLogin();
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
        SwingUtilities.invokeLater(ModernLoginSignupForm::new);
    }
}
