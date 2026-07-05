import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField balanceField;
    private AccountDAO accountDAO;

    public RegisterFrame() {
        accountDAO = new AccountDAO();

        setTitle("Banking Portal - Register");
        setSize(520, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(245, 248, 252));

        JLabel heading = new JLabel("Create New Account");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBounds(130, 25, 260, 30);
        mainPanel.add(heading);

        JLabel subHeading = new JLabel("Fill your details to register");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 14));
        subHeading.setBounds(160, 60, 220, 20);
        mainPanel.add(subHeading);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        nameLabel.setBounds(70, 110, 100, 25);
        mainPanel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(210, 110, 220, 32);
        mainPanel.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        emailLabel.setBounds(70, 155, 100, 25);
        mainPanel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(210, 155, 220, 32);
        mainPanel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordLabel.setBounds(70, 200, 100, 25);
        mainPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(210, 200, 220, 32);
        mainPanel.add(passwordField);

        JLabel balanceLabel = new JLabel("Initial Balance:");
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        balanceLabel.setBounds(70, 245, 120, 25);
        mainPanel.add(balanceLabel);

        balanceField = new JTextField();
        balanceField.setBounds(210, 245, 220, 32);
        mainPanel.add(balanceField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(120, 315, 120, 38);
        mainPanel.add(registerButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(270, 315, 140, 38);
        mainPanel.add(backButton);

        registerButton.addActionListener(e -> registerUser());

        backButton.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        add(mainPanel);
        setVisible(true);
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String balanceText = balanceField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || balanceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        double balance;

        try {
            balance = Double.parseDouble(balanceText);

            if (balance < 0) {
                JOptionPane.showMessageDialog(this, "Initial balance cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid balance amount.");
            return;
        }

        Account account = new Account(name, email, password, balance);
        boolean registered = accountDAO.registerAccount(account);

        if (registered) {
            JOptionPane.showMessageDialog(this, "Account created successfully!");
            new LoginFrame();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Email may already exist.");
        }
    }
}
