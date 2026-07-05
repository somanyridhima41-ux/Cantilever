 import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private AccountDAO accountDAO;

    public LoginFrame() {
        accountDAO = new AccountDAO();

        setTitle("Banking Portal - Login");
        setSize(500, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(240, 248, 255));

        // Heading
        JLabel heading = new JLabel("Welcome to SecureBank");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBounds(95, 30, 320, 30);
        mainPanel.add(heading);

        JLabel subHeading = new JLabel("Login to continue");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 15));
        subHeading.setBounds(180, 65, 150, 20);
        mainPanel.add(subHeading);

        // Email label
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        emailLabel.setBounds(70, 120, 100, 25);
        mainPanel.add(emailLabel);

        // Email field
        emailField = new JTextField();
        emailField.setBounds(180, 120, 220, 32);
        mainPanel.add(emailField);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordLabel.setBounds(70, 170, 100, 25);
        mainPanel.add(passwordLabel);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setBounds(180, 170, 220, 32);
        mainPanel.add(passwordField);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(120, 245, 110, 38);
        mainPanel.add(loginButton);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(260, 245, 110, 38);
        mainPanel.add(registerButton);

        // Login button action
        loginButton.addActionListener(e -> loginUser());

        // Register button action
        registerButton.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }

    private void loginUser() {
        String email = emailField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.");
            return;
        }

      Account account = accountDAO.loginAccount(email, password);

        if (account != null) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            new DashboardFrame(account);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.");
        }
    }
} 
    

