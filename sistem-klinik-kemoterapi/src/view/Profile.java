package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import db.DBConnection;

public class Profile extends JFrame {
    private JTextField namaField, emailField, phoneField;
    private JButton editBtn, saveBtn, cancelBtn;

    private final String username;

    public Profile(String username) {
        this.username = username;

        setTitle("Profil");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Navbar
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(new Color(173, 216, 230));
        navbar.setPreferredSize(new Dimension(900, 50));

        // Logo dan teks yang bisa diklik
        ImageIcon logoIcon = new ImageIcon("assets/Logo-Klinik.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaledLogo));
        logo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        logo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Dashboard(username).setVisible(true);
                dispose();
            }
        });

        JLabel klinikLabel = new JLabel("KLINIK SENTRA MEDIKA");
        klinikLabel.setFont(new Font("Arial", Font.BOLD, 18));
        klinikLabel.setForeground(Color.BLACK);
        klinikLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        klinikLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        klinikLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Dashboard(username).setVisible(true);
                dispose();
            }
        });

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        logoPanel.add(logo);
        logoPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        logoPanel.add(klinikLabel);

        // Label user kanan atas
        JLabel userLabel = new JLabel("👤 " + username);
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        navbar.add(logoPanel, BorderLayout.WEST);
        navbar.add(userLabel, BorderLayout.EAST);

        add(navbar, BorderLayout.NORTH);

        // Panel utama konten profil
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridy++;
        namaField = new JTextField(20);
        namaField.setEnabled(false);
        contentPanel.add(namaField, gbc);

        gbc.gridy++;
        contentPanel.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        emailField = new JTextField(20);
        emailField.setEnabled(false);
        contentPanel.add(emailField, gbc);

        gbc.gridy++;
        contentPanel.add(new JLabel("No Telepon:"), gbc);
        gbc.gridy++;
        phoneField = new JTextField(20);
        phoneField.setEnabled(false);
        contentPanel.add(phoneField, gbc);

        // Panel tombol
        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editBtn = new JButton("Edit");
        saveBtn = new JButton("Simpan");
        cancelBtn = new JButton("Batal");

        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        buttonPanel.add(editBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        gbc.gridwidth = 2;
        contentPanel.add(buttonPanel, gbc);

        add(contentPanel, BorderLayout.CENTER);

        // Load profil dari DB
        loadProfile();

        // Event listeners
        editBtn.addActionListener(e -> setEditMode(true));

        saveBtn.addActionListener(e -> {
            if (validateInput() && updateProfile()) {
                JOptionPane.showMessageDialog(this, "Profil berhasil disimpan!");
                setEditMode(false);
            }
        });

        cancelBtn.addActionListener(e -> {
            loadProfile();
            setEditMode(false);
        });

        setVisible(true);
    }

    private void loadProfile() {
        try (Connection conn = DBConnection.connect()) {
            String query = "SELECT full_name, email, phone FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                namaField.setText(rs.getString("full_name"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
            } else {
                JOptionPane.showMessageDialog(this, "Data profil tidak ditemukan.");
                dispose();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat profil: " + e.getMessage());
        }
    }

    private boolean updateProfile() {
        try (Connection conn = DBConnection.connect()) {
            String query = "UPDATE users SET full_name = ?, email = ?, phone = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, namaField.getText().trim());
            stmt.setString(2, emailField.getText().trim());
            stmt.setString(3, phoneField.getText().trim());
            stmt.setString(4, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan profil: " + e.getMessage());
            return false;
        }
    }

    private void setEditMode(boolean editable) {
        namaField.setEnabled(editable);
        emailField.setEnabled(editable);
        phoneField.setEnabled(editable);

        saveBtn.setEnabled(editable);
        cancelBtn.setEnabled(editable);
        editBtn.setEnabled(!editable);
    }

    private boolean validateInput() {
        if (namaField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.");
            return false;
        }

        if (!emailField.getText().contains("@")) {
            JOptionPane.showMessageDialog(this, "Format email tidak valid.");
            return false;
        }

        return true;
    }
}