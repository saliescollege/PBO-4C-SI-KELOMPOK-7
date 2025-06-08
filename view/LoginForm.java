package view;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.security.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }

    public LoginForm() {
        setTitle("Halaman Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 80, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 150, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 60, 80, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 60, 150, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(40, 100, 100, 25);
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(150, 100, 100, 25);
        add(registerButton);

        // Aksi login
        loginButton.addActionListener(e -> login());

        // Aksi register
        registerButton.addActionListener(e -> {
            new RegisterForm();
            dispose();
        });

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db_chemoclinic", "root", "")) {

            String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, md5(password));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("full_name");
                JOptionPane.showMessageDialog(this, "Login berhasil!\nSelamat datang " + fullName);

                // Cek role berdasarkan username (atau tambahkan kolom role)
                if (username.startsWith("dr_")) {
                    new DashboardDokter(); // kalau login sebagai dokter
                } else {
                    new DashboardPasien(); // kalau pasien
                }

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan koneksi database.");
        }
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
