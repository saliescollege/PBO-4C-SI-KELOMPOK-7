import javax.swing.SwingUtilities;
import view.LoginForm;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm();
        });
    }
}