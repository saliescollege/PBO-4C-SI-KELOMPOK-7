package dashboard;

import login.LoginForm;
import login.User;
import login.UserDatabase;

import javax.swing.*;
import java.awt.*;

public class Profile extends JDialog {

    private String currentUsername;
    private String currentRole;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public Profile(JFrame parent, String username, String role) {
        super(parent, "Profil Pengguna", true);
        this.currentUsername = username;
        this.currentRole = role;

        setSize(350, 220);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));
        setResizable(false);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(currentUsername);
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Role:"));
        JTextField roleField = new JTextField(currentRole);
        roleField.setEditable(false);
        formPanel.add(roleField);

        formPanel.add(new JLabel("Password baru:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Simpan");
        JButton logoutBtn = new JButton("Logout");
        JButton cancelBtn = new JButton("Batal");

        buttonPanel.add(saveBtn);
        buttonPanel.add(logoutBtn);
        buttonPanel.add(cancelBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action tombol Simpan
        saveBtn.addActionListener(e -> {
            String newUsername = usernameField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();

            if (newUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newUsername.equals(currentUsername) && UserDatabase.isUsernameExists(newUsername)) {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update user di database
            boolean success = UserDatabase.updateUser(currentUsername, newUsername, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui.");
                this.currentUsername = newUsername; // update internal
                dispose();
                // Biasanya setelah update username, kamu mungkin mau refresh dashboard label
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui profil.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action tombol Logout
        logoutBtn.addActionListener(e -> {
            dispose();
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow != null) {
                parentWindow.dispose(); // tutup dashboard
            }
            new LoginForm(); // buka login lagi
        });

        // Action tombol Batal
        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }
}
