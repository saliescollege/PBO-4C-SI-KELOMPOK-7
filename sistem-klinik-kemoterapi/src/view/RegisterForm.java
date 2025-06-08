package view;

import javax.swing.*;

public class RegisterForm extends JFrame {
    public RegisterForm() {
        setTitle("Form Registrasi");
        setSize(300, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 80, 25);
        add(userLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(100, 20, 150, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 60, 80, 25);
        add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(100, 60, 150, 25);
        add(passwordField);

        JLabel roleLabel = new JLabel("Kategori:");
        roleLabel.setBounds(20, 100, 80, 25);
        add(roleLabel);

        // Dropdown pilihan role
        String[] roles = { "dokter", "pasien" };
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(100, 100, 150, 25);
        add(roleComboBox);

        JButton registerBtn = new JButton("Daftar");
        registerBtn.setBounds(100, 150, 100, 25);
        add(registerBtn);

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan password tidak boleh kosong!");
                return;
            }

            if (UserDatabase.register(username, password, role)) {
                JOptionPane.showMessageDialog(this, "Registrasi berhasil!");
                new LoginForm();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan");
            }
        });

        setVisible(true);
    }
}
