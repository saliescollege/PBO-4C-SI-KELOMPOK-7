package view;

import java.security.*;
import java.sql.*;
import javax.swing.*;

public class RegisterForm extends JFrame {
    public RegisterForm() {
        setTitle("Form Registrasi");
        setSize(350, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 100, 25);
        add(userLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(130, 20, 180, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 60, 100, 25);
        add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(130, 60, 180, 25);
        add(passwordField);

        JLabel nameLabel = new JLabel("Nama Lengkap:");
        nameLabel.setBounds(20, 100, 100, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(130, 100, 180, 25);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 140, 100, 25);
        add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(130, 140, 180, 25);
        add(emailField);

        JLabel phoneLabel = new JLabel("Telepon:");
        phoneLabel.setBounds(20, 180, 100, 25);
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(130, 180, 180, 25);
        add(phoneField);

        JLabel roleLabel = new JLabel("Kategori:");
        roleLabel.setBounds(20, 220, 100, 25);
        add(roleLabel);

        String[] roles = { "dokter", "pasien" };
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(130, 220, 180, 25);
        add(roleComboBox);

        JButton registerBtn = new JButton("Daftar");
        registerBtn.setBounds(130, 260, 100, 30);
        add(registerBtn);

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String fullName = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username, password, dan nama wajib diisi.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_chemoclinic", "root", "")) {

                // Cek apakah username sudah digunakan
                PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username sudah digunakan.");
                    return;
                }

                // Simpan user baru
                String insertSQL = "INSERT INTO users (username, password_hash, full_name, email, phone) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                insertStmt.setString(1, username);
                insertStmt.setString(2, md5(password));
                insertStmt.setString(3, fullName);
                insertStmt.setString(4, email);
                insertStmt.setString(5, phone);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Registrasi berhasil!");

                new LoginForm();
                dispose();

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data.");
            }
        });

        setVisible(true);
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
