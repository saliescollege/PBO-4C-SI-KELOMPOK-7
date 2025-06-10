package PBO_4C_SI_KELOMPOK_7;
import javax.swing.SwingUtilities;
import PBO_4C_SI_KELOMPOK_7.view.LoginForm;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm();
        });
    }
}