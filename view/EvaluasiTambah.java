package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class EvaluasiTambah extends JFrame {
    private JTextField jadwalIdField;
    private JTextArea kondisiField, efekSampingField, catatanField;
    private JButton simpanButton;
    private EvaluasiView evaluasiViewCaller; // Untuk merefresh tabel setelah data disimpan

    public EvaluasiTambah(EvaluasiView caller, String username) {
        this.evaluasiViewCaller = caller;

        setTitle("Form Tambah Evaluasi Sesi");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Form Evaluasi Sesi Kemoterapi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Jadwal ID
        gbc.gridy++; gbc.gridwidth = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("ID Jadwal Terapi:"), gbc);

        gbc.gridx = 1;
        jadwalIdField = new JTextField();
        jadwalIdField.setToolTipText("Masukkan ID dari jadwal terapi yang akan dievaluasi.");
        formPanel.add(jadwalIdField, gbc);

        // Kondisi Post Terapi
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Kondisi Pasca-Terapi:"), gbc);
        
        gbc.gridx = 1;
        kondisiField = new JTextArea(4, 20);
        kondisiField.setLineWrap(true);
        kondisiField.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(kondisiField), gbc);

        // Efek Samping
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Efek Samping:"), gbc);

        gbc.gridx = 1;
        efekSampingField = new JTextArea(4, 20);
        efekSampingField.setLineWrap(true);
        efekSampingField.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(efekSampingField), gbc);
        
        // Catatan Tambahan
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Catatan Tambahan:"), gbc);

        gbc.gridx = 1;
        catatanField = new JTextArea(4, 20);
        catatanField.setLineWrap(true);
        catatanField.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(catatanField), gbc);

        add(formPanel, BorderLayout.CENTER);

        // Tombol Simpan
        simpanButton = new JButton("Simpan Evaluasi");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        buttonPanel.add(simpanButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listener
        simpanButton.addActionListener(e -> simpanEvaluasi());
    }

    private void simpanEvaluasi() {
        // Validasi input
        String jadwalIdStr = jadwalIdField.getText().trim();
        String kondisi = kondisiField.getText().trim();
        String efek = efekSampingField.getText().trim();
        String catatan = catatanField.getText().trim();

        if (jadwalIdStr.isEmpty() || kondisi.isEmpty() || efek.isEmpty() || catatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int jadwalId;
        try {
            jadwalId = Integer.parseInt(jadwalIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Jadwal harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Cek apakah jadwal_id ada di database
        if (!isJadwalExists(jadwalId)) {
            JOptionPane.showMessageDialog(this, "ID Jadwal Terapi " + jadwalId + " tidak ditemukan di database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Proses penyimpanan ke database
        String sql = "INSERT INTO evaluasi_kemo (jadwal_id, kondisi_post_terapi, efek_samping, catatan, tanggal_evaluasi) VALUES (?, ?, ?, ?, NOW())";
        
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, jadwalId);
            pstmt.setString(2, kondisi);
            pstmt.setString(3, efek);
            pstmt.setString(4, catatan);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Data evaluasi berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                evaluasiViewCaller.loadEvaluasiData(); // Refresh tabel di frame sebelumnya
                dispose(); // Tutup jendela ini
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data ke database: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Fungsi helper untuk memeriksa keberadaan jadwal_id
    private boolean isJadwalExists(int jadwalId) {
        String sql = "SELECT 1 FROM jadwal_terapi WHERE jadwal_id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jadwalId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // true jika ada, false jika tidak
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}