package view;

import model.User;

import javax.swing.*;

import java.awt.event.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    // Pindahkan method main() langsung ke dalam kelas LoginForm
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm(); // Buat dan tampilkan LoginForm di EDT
        });
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

        // Aksi tombol login
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = UserDatabase.login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login Berhasil!");
                dispose(); // Tutup form login
                new Dashboard(user.getUsername(), user.getRole()); // Buka dashboard
            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah!");
            }
        });

        // Aksi tombol register
        registerButton.addActionListener(e -> {
            new RegisterForm(); // buka form registrasi
            dispose(); // tutup form login
        });

        setVisible(true);
    }
}