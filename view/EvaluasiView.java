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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Daftar Hasil Evaluasi Sesi Kemoterapi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Setup Tabel Sederhana
        String[] columns = {"Tanggal", "Nama Pasien", "Nama Dokter", "Catatan Penting"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        evaluasiTable = new JTable(tableModel);
        evaluasiTable.setFillsViewportHeight(true);
        evaluasiTable.setRowHeight(25);
        evaluasiTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        evaluasiTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        evaluasiTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        evaluasiTable.getColumnModel().getColumn(3).setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(evaluasiTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Tombol Tambah dihapus, diganti tombol Refresh
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh Data");
        buttonPanel.add(refreshButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        refreshButton.addActionListener(e -> loadEvaluasiData());

        loadEvaluasiData();
    }

    public void loadEvaluasiData() {
        tableModel.setRowCount(0);

        // Query disederhanakan
        String sql = "SELECT e.tanggal_evaluasi, e.catatan, p.nama_lengkap AS nama_pasien, d.nama AS nama_dokter " +
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
                Timestamp tanggalEvaluasi = rs.getTimestamp("tanggal_evaluasi");
                String catatan = rs.getString("catatan");
                String namaPasien = rs.getString("nama_pasien");
                String namaDokter = rs.getString("nama_dokter");

                tableModel.addRow(new Object[]{
                    new SimpleDateFormat("dd-MM-yyyy HH:mm").format(tanggalEvaluasi),
                    namaPasien,
                    namaDokter,
                    catatan
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saat mengambil data evaluasi: " + ex.getMessage(), "Error Database", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}