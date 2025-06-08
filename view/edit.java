package view;

import model.UserDatabase;

import javax.swing.*;
import java.awt.*;

public class Edit extends JFrame {

    private String currentUsername;
    private String currentRole;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public Edit(String username, String role) {
        this.currentUsername = username;
        this.currentRole = role;

        setTitle("Edit Profil");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
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

        formPanel.add(new JLabel("Password Baru:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Simpan");
        JButton exitBtn = new JButton("Keluar");
        JButton logoutBtn = new JButton("Logout");

        buttonPanel.add(saveBtn);
        buttonPanel.add(exitBtn);
        buttonPanel.add(logoutBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Tombol Simpan
        saveBtn.addActionListener(e -> {
            String newUsername = usernameField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();

            if (newUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username tidak boleh kosong!");
                return;
            }

            if (!newUsername.equals(currentUsername) && UserDatabase.isUsernameExists(newUsername)) {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan!");
                return;
            }

            boolean success = UserDatabase.updateUser(currentUsername, newUsername, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui.");
                currentUsername = newUsername;
                dispose(); // tutup edit
                // bisa arahkan kembali ke dashboard di sini
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui profil.");
            }
        });

        // Tombol Keluar
        exitBtn.addActionListener(e -> dispose());

        // Tombol Logout
        logoutBtn.addActionListener(e -> {
            dispose(); // tutup form edit
            new LoginForm(); // buka ulang login
        });

        setVisible(true);
    }
}
