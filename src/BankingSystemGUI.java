import javax.swing.SwingUtilities;

public class BankingSystemGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}
