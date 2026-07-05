import java.util.Scanner;

public class BankingSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountDAO accountDAO = new AccountDAO();

        while (true) {
            System.out.println("\n===== BANKING SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume leftover newline

            switch (choice) {
                case 1:
                    System.out.println("\n--- Register New Account ---");
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter password: ");
                    String password = sc.nextLine();

                    System.out.print("Enter initial balance: ");
                    double balance = sc.nextDouble();
                    sc.nextLine();

                    Account newAccount = new Account(name, email, password, balance);

                    boolean registered = accountDAO.registerAccount(newAccount);

                    if (registered) {
                        System.out.println("Account created successfully!");
                    } else {
                        System.out.println("Registration failed. Try again.");
                    }
                    break;

                case 2:
                    System.out.println("\n--- Login ---");
                    System.out.print("Enter email: ");
                    String loginEmail = sc.nextLine();

                    System.out.print("Enter password: ");
                    String loginPassword = sc.nextLine();

                    Account loggedInAccount = accountDAO.loginAccount(loginEmail, loginPassword);

                    if (loggedInAccount != null) {
                        System.out.println("\nLogin successful. Welcome, " + loggedInAccount.getName() + "!");

                        boolean isLoggedIn = true;

                        while (isLoggedIn) {
                            System.out.println("\n--- Account Menu ---");
                            System.out.println("1. Check Balance");
                            System.out.println("2. Deposit");
                            System.out.println("3. Withdraw");
                            System.out.println("4. Logout");
                            System.out.print("Enter your choice: ");
                            int accountChoice = sc.nextInt();

                            switch (accountChoice) {
                                case 1:
                                    double currentBalance = accountDAO.getBalanceById(loggedInAccount.getId());
                                    System.out.println("Current Balance: ₹" + currentBalance);
                                    break;

                                case 2:
                                    System.out.print("Enter amount to deposit: ");
                                    double depositAmount = sc.nextDouble();

                                    if (depositAmount > 0) {
                                        boolean depositSuccess = accountDAO.depositBalance(loggedInAccount.getId(), depositAmount);

                                        if (depositSuccess) {
                                            System.out.println("Amount deposited successfully.");
                                        } else {
                                            System.out.println("Deposit failed.");
                                        }
                                    } else {
                                        System.out.println("Invalid amount.");
                                    }
                                    break;

                                case 3:
                                    System.out.print("Enter amount to withdraw: ");
                                    double withdrawAmount = sc.nextDouble();

                                    if (withdrawAmount > 0) {
                                        boolean withdrawSuccess = accountDAO.withdrawBalance(loggedInAccount.getId(), withdrawAmount);

                                        if (withdrawSuccess) {
                                            System.out.println("Withdrawal successful.");
                                        } else {
                                            System.out.println("Insufficient balance or withdrawal failed.");
                                        }
                                    } else {
                                        System.out.println("Invalid amount.");
                                    }
                                    break;

                                case 4:
                                    isLoggedIn = false;
                                    System.out.println("Logged out successfully.");
                                    break;

                                default:
                                    System.out.println("Invalid choice.");
                            }
                        }
                    } else {
                        System.out.println("Invalid email or password.");
                    }
                    break;

                case 3:
                    System.out.println("Thank you for using Banking System!");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
