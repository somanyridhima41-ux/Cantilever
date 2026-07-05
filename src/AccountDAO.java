import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDAO {

    public boolean registerAccount(Account account) {
        String query = "INSERT INTO accounts(name, email, password, balance) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, account.getName());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPassword());
            ps.setDouble(4, account.getBalance());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Account loginAccount(String email, String password) {
        String query = "SELECT * FROM accounts WHERE email = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Account(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDouble("balance")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===============================
    // G4 METHODS START HERE
    // ===============================

    public boolean depositBalance(int accountId, double amount) {
    String query = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setDouble(1, amount);
        ps.setInt(2, accountId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            addTransaction(accountId, "Deposit", amount);
            return true;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean withdrawBalance(int accountId, double amount) {
    String query = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setDouble(1, amount);
        ps.setInt(2, accountId);
        ps.setDouble(3, amount);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            addTransaction(accountId, "Withdraw", amount);
            return true;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    public double getBalanceById(int accountId) {
        String query = "SELECT balance FROM accounts WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
public ResultSet getTransactions(int accountId) {
    try {
        Connection con = DBConnection.getConnection();
        String query = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, accountId);
        return ps.executeQuery();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
public void addTransaction(int accountId, String type, double amount) {
    try {
        Connection con = DBConnection.getConnection();
        String query = "INSERT INTO transactions(account_id, transaction_type, amount) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, accountId);
        ps.setString(2, type);
        ps.setDouble(3, amount);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public Account getAccountByEmail(String email) {
    String query = "SELECT * FROM accounts WHERE email = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Account account = new Account();
            account.setId(rs.getInt("id"));
            account.setName(rs.getString("name"));
            account.setEmail(rs.getString("email"));
            account.setPassword(rs.getString("password"));
            account.setBalance(rs.getDouble("balance"));
            return account;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
    public boolean transferMoney(int senderId, int receiverId, double amount) {
    try {
        Connection con = DBConnection.getConnection();

        // 1. deduct from sender
        String deductQuery = "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?";
        PreparedStatement ps1 = con.prepareStatement(deductQuery);
        ps1.setDouble(1, amount);
        ps1.setInt(2, senderId);
        ps1.setDouble(3, amount);

        int rows1 = ps1.executeUpdate();

        if (rows1 > 0) {
            // 2. add to receiver
            String addQuery = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
            PreparedStatement ps2 = con.prepareStatement(addQuery);
            ps2.setDouble(1, amount);
            ps2.setInt(2, receiverId);

            int rows2 = ps2.executeUpdate();

            if (rows2 > 0) {
                addTransaction(senderId, "Transfer Sent", amount);
                addTransaction(receiverId, "Transfer Received", amount);
                return true;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
}