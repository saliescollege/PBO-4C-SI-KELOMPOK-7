package PBO_4C_SI_KELOMPOK_7.view; // Menggunakan package yang sesuai

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.security.*;

import PBO_4C_SI_KELOMPOK_7.view.Dashboard;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }

    public LoginForm() {
        setTitle("Login");
        setSize(350, 470);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // ==== Logo ====
        ImageIcon logoIcon = new ImageIcon("src\\PBO_4C_SI_KELOMPOK_7\\assets\\Logo_Klinik.png");
        Image logoImg = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setBounds((350 - 70) / 2, 20, 70, 70);
        add(logoLabel);

        // ==== Nama Klinik ====
        JLabel klinikLabel = new JLabel("KLINIK SENTRA MEDIKA", SwingConstants.CENTER);
        klinikLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        klinikLabel.setBounds(75, 95, 200, 20);
        add(klinikLabel);

        // ==== Judul Login ====
        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBounds(75, 130, 200, 30);
        add(titleLabel);

        // ==== Form Login ====
        int labelX = 35;
        int fieldX = 135;
        int widthLabel = 100;
        int widthField = 215;
        int y = 180;
        int gap = 40;

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(labelX, y, widthLabel, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(fieldX, y, widthField - 50, 25);
        add(usernameField);

        y += gap;
        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(labelX, y, widthLabel, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(fieldX, y, widthField - 50, 25);
        add(passwordField);

        // ==== Tombol Login ====
        y += gap + 10;
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(labelX, y, 265, 30);
        add(loginButton);

        // ==== Label Registrasi ====
        y += 30;
        String html = "<html>Belum punya akun? <a href=''>Daftar</a></html>";
        JLabel registerLabel = new JLabel(html, SwingConstants.CENTER);
        registerLabel.setSize(265, 30);
        registerLabel.setLocation((350 - 265) / 2, y);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(registerLabel);

        // ==== Aksi tombol Login ====
        loginButton.addActionListener(e -> login());

        // ==== Aksi klik label Registrasi ====
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterForm();
                dispose();
            }
        });

        // ==== Tampilkan Frame ====
        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan isi username dan password.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/db_chemoclinic", "root", "")) {

            String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, md5(password));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("full_name");
                JOptionPane.showMessageDialog(this, "Login berhasil!\nSelamat datang, " + fullName + "."); 

                // === Tambahkan baris ini ===
                new Dashboard(fullName); // Buka dashboard setelah login berhasil
                dispose();       // Tutup halaman login
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
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}