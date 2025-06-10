package PBO_4C_SI_KELOMPOK_7.view; 

import java.awt.*;
import java.security.*;
import java.sql.*;
import javax.swing.*;

public class RegisterForm extends JFrame {
    public RegisterForm() {
        setTitle("Registrasi");
        setSize(350, 470);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // ==== Logo ====
        ImageIcon logoIcon = new ImageIcon("assets/Logo-Klinik.png");
        Image logoImg = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setBounds((350 - 70) / 2, 20, 70, 70);
        add(logoLabel);

        // ==== Nama Klinik ====
        JLabel klinikLabel = new JLabel("KLINIK SENTRA MEDIKA", SwingConstants.CENTER);
        klinikLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        klinikLabel.setBounds(75, 95, 200, 20);
        add(klinikLabel);

        // ==== Title "Registrasi" ====
        JLabel titleLabel = new JLabel("Registrasi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBounds(75, 130, 200, 30);
        add(titleLabel);

        // ==== Form Fields ====
        int labelX = 35;
        int fieldX = 135;
        int widthLabel = 100;
        int widthField = 215;
        int y = 170; 
        int gap = 40;

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(labelX, y, widthLabel, 25);
        add(userLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(fieldX, y, widthField - 50, 25);
        add(usernameField);

        y += gap;
        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(labelX, y, widthLabel, 25);
        add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(fieldX, y, widthField - 50, 25);
        add(passwordField);

        y += gap;
        JLabel nameLabel = new JLabel("Nama Lengkap");
        nameLabel.setBounds(labelX, y, widthLabel, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(fieldX, y, widthField - 50, 25);
        add(nameField);

        y += gap;
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(labelX, y, widthLabel, 25);
        add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(fieldX, y, widthField - 50, 25);
        add(emailField);

        y += gap;
        JLabel phoneLabel = new JLabel("Telepon");
        phoneLabel.setBounds(labelX, y, widthLabel, 25);
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(fieldX, y, widthField - 50, 25);
        add(phoneField);

        // ==== Tombol Daftar ====
        y += gap; 
        JButton registerBtn = new JButton("Daftar");
        registerBtn.setBounds(labelX, y, 265, 30);
        add(registerBtn);

        // ==== Aksi tombol ====
        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String fullName = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username, password, dan nama wajib diisi.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_chemoclinic", "root", "")) {

                PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username sudah digunakan.");
                    return;
                }

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