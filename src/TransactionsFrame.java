import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class TransactionsFrame extends JFrame {

    private int accountId;
    private AccountDAO dao;

    public TransactionsFrame(int accountId) {
        this.accountId = accountId;
        dao = new AccountDAO();

        setTitle("Transaction History");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = {"Transaction ID", "Type", "Amount", "Date"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        loadTransactions(model);

        JScrollPane scrollPane = new JScrollPane(table);

        JLabel titleLabel = new JLabel("Transaction History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        setLayout(new BorderLayout(10, 10));
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadTransactions(DefaultTableModel model) {
        try {
            ResultSet rs = dao.getTransactions(accountId);

            while (rs != null && rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                String type = rs.getString("transaction_type");
                double amount = rs.getDouble("amount");
                String date = rs.getString("transaction_date");

                model.addRow(new Object[]{transactionId, type, amount, date});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading transactions.");
        }
    }
}
