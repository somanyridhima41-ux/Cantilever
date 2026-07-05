import javax.swing.*;
import java.awt.*;


public class DashboardFrame extends JFrame {

    private Account loggedInAccount;
    private JLabel balanceLabel;
    private AccountDAO accountDAO;

    public DashboardFrame(Account account) {
        this.loggedInAccount = account;
        this.accountDAO = new AccountDAO();

        setTitle("Banking Portal - Dashboard");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(240, 248, 255));
        
        JLabel titleLabel = new JLabel("SecureBank Dashboard");
titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
titleLabel.setBounds(190, 20, 320, 40);
mainPanel.add(titleLabel);

JLabel welcomeLabel = new JLabel("Welcome, " + loggedInAccount.getName() + "!");
welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 22));
welcomeLabel.setBounds(60, 90, 300, 30);
mainPanel.add(welcomeLabel);



JLabel emailLabel = new JLabel("Email: " + loggedInAccount.getEmail());
emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
emailLabel.setBounds(60, 130, 350, 25);
mainPanel.add(emailLabel);

JLabel accountNumberLabel = new JLabel("Account Number: " + generateAccountNumber(loggedInAccount.getId()));
accountNumberLabel.setFont(new Font("Arial", Font.PLAIN, 18));
accountNumberLabel.setBounds(60, 160, 350, 25);
mainPanel.add(accountNumberLabel);


balanceLabel = new JLabel("Current Balance: ₹" + loggedInAccount.getBalance());
balanceLabel.setFont(new Font("Arial", Font.BOLD, 26));
balanceLabel.setBounds(60, 220, 420, 35);
mainPanel.add(balanceLabel);

JLabel quickActionsLabel = new JLabel("Quick Actions");
quickActionsLabel.setFont(new Font("Arial", Font.BOLD, 20));
quickActionsLabel.setBounds(60, 280, 200, 30);
mainPanel.add(quickActionsLabel);

        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(60, 330, 150, 45);
        mainPanel.add(depositButton);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(240, 330, 150, 45);
        mainPanel.add(withdrawButton);

        JButton refreshButton = new JButton("Refresh Balance");
        refreshButton.setBounds(420, 330, 180, 45);
        mainPanel.add(refreshButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(460, 400, 140, 45);
        mainPanel.add(logoutButton);

        JButton transactionsBtn = new JButton("View Transactions");
        transactionsBtn.setBounds(240, 400, 200, 45);
        mainPanel.add(transactionsBtn);

        JButton transferButton = new JButton("Transfer Money");
        transferButton.setBounds(60, 400, 150, 45);
        mainPanel.add(transferButton);

        // Deposit action
        depositButton.addActionListener(e -> depositMoney());

        // Withdraw action
        withdrawButton.addActionListener(e -> withdrawMoney());

        // Refresh action
        refreshButton.addActionListener(e -> refreshBalance());

        // Logout action
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });


        transactionsBtn.addActionListener(e -> {
           new TransactionsFrame(loggedInAccount.getId());
        });

        transferButton.addActionListener(e -> transferMoney());

      

        add(mainPanel);
        setVisible(true);
    }

    private void depositMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            double amount = Double.parseDouble(input);

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a valid amount.");
                return;
            }

            boolean success = accountDAO.depositBalance(loggedInAccount.getId(), amount);

            if (success) {
                JOptionPane.showMessageDialog(this, "₹" + amount + " deposited successfully.");
                refreshBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Deposit failed.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter numbers only.");
        }
    }

    private void withdrawMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");

        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            double amount = Double.parseDouble(input);

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a valid amount.");
                return;
            }

            double currentBalance = accountDAO.getBalanceById(loggedInAccount.getId());

            if (amount > currentBalance) {
                JOptionPane.showMessageDialog(this, "Insufficient balance.");
                return;
            }

            boolean success = accountDAO.withdrawBalance(loggedInAccount.getId(), amount);

            if (success) {
                JOptionPane.showMessageDialog(this, "₹" + amount + " withdrawn successfully.");
                refreshBalance();
            } else {
                JOptionPane.showMessageDialog(this, "Withdrawal failed.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter numbers only.");
        }
    }

    private void refreshBalance() {
        double updatedBalance = accountDAO.getBalanceById(loggedInAccount.getId());
        loggedInAccount.setBalance(updatedBalance);
        balanceLabel.setText("Current Balance: ₹" + updatedBalance);
    }
        private void transferMoney() {
    String receiverEmail = JOptionPane.showInputDialog(this, "Enter receiver email:");
    String amountStr = JOptionPane.showInputDialog(this, "Enter amount to transfer:");

    if (receiverEmail == null || amountStr == null) return;

    try {
        double amount = Double.parseDouble(amountStr);

        Account receiver = accountDAO.getAccountByEmail(receiverEmail);

        if (receiver == null) {
            JOptionPane.showMessageDialog(this, "Receiver account not found.");
            return;
        }

        if (receiver.getId() == loggedInAccount.getId()) {
            JOptionPane.showMessageDialog(this, "You cannot transfer money to your own account.");
            return;
        }
       

        boolean success = accountDAO.transferMoney(
                loggedInAccount.getId(),
                receiver.getId(),
                amount
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Money transferred successfully!");
            refreshBalance();
        } else {
            JOptionPane.showMessageDialog(this, "Transfer failed. Check balance.");
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid amount entered.");
    }
}
private String generateAccountNumber(int id) {
    return "ACC" + id;
}}