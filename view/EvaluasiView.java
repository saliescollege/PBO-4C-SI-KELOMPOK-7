package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.db.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class EvaluasiView extends BaseFrame {
    private JTable evaluasiTable;
    private DefaultTableModel tableModel;

    public EvaluasiView(String username) {
        super("Daftar Evaluasi Kemoterapi", username);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Agar tidak menutup aplikasi utama
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Panel utama dengan border
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Judul
        JLabel titleLabel = new JLabel("Daftar Hasil Evaluasi Sesi Kemoterapi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Setup Tabel
        String[] columns = {"ID Eval", "Tanggal", "Pasien", "Dokter", "Sesi Ke", "Catatan Penting"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat sel tidak bisa diedit
            }
        };

        evaluasiTable = new JTable(tableModel);
        evaluasiTable.setFillsViewportHeight(true);
        evaluasiTable.setRowHeight(25);
        evaluasiTable.getColumnModel().getColumn(0).setMaxWidth(60);  // ID
        evaluasiTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Tanggal
        evaluasiTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Pasien
        evaluasiTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Dokter
        evaluasiTable.getColumnModel().getColumn(4).setMaxWidth(60); // Sesi
        evaluasiTable.getColumnModel().getColumn(5).setPreferredWidth(300); // Catatan

        JScrollPane scrollPane = new JScrollPane(evaluasiTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton tambahButton = new JButton("Tambah Evaluasi Baru");
        tambahButton.setBackground(new Color(40, 167, 69));
        tambahButton.setForeground(Color.WHITE);
        
        JButton refreshButton = new JButton("Refresh Data");

        buttonPanel.add(tambahButton);
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Tambahkan mainPanel ke frame
        add(mainPanel);

        // Aksi Tombol
        refreshButton.addActionListener(e -> loadEvaluasiData());
        tambahButton.addActionListener(e -> {
            new EvaluasiTambah(this, username).setVisible(true); // Buka form tambah
        });

        // Load data awal
        loadEvaluasiData();
    }

    public void loadEvaluasiData() {
        tableModel.setRowCount(0); // Kosongkan tabel sebelum memuat data baru

        // Query ini menggabungkan beberapa tabel untuk mendapatkan informasi yang relevan
        String sql = "SELECT " +
                     "  e.evaluasi_id, e.tanggal_evaluasi, e.catatan, " +
                     "  p.nama_lengkap AS nama_pasien, " +
                     "  d.nama AS nama_dokter, " +
                     "  jt.sesi_ke " +
                     "FROM evaluasi_kemo e " +
                     "JOIN jadwal_terapi jt ON e.jadwal_id = jt.jadwal_id " +
                     "JOIN rencana_terapi rt ON jt.terapi_id = rt.terapi_id " +
                     "JOIN pasien p ON rt.pasien_id = p.pasien_id " +
                     "JOIN dokter d ON rt.dokter_id = d.dokter_id " +
                     "ORDER BY e.tanggal_evaluasi DESC";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int evaluasiId = rs.getInt("evaluasi_id");
                Timestamp tanggalEvaluasi = rs.getTimestamp("tanggal_evaluasi");
                String catatan = rs.getString("catatan");
                String namaPasien = rs.getString("nama_pasien");
                String namaDokter = rs.getString("nama_dokter");
                int sesiKe = rs.getInt("sesi_ke");

                tableModel.addRow(new Object[]{
                    evaluasiId,
                    new SimpleDateFormat("dd-MM-yyyy HH:mm").format(tanggalEvaluasi),
                    namaPasien,
                    namaDokter,
                    sesiKe,
                    catatan
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saat mengambil data evaluasi: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}