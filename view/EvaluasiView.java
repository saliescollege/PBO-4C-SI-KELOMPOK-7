package PBO_4C_SI_KELOMPOK_7.view; // Use the correct package name 

import PBO_4C_SI_KELOMPOK_7.db.DBConnection; // Import your DBConnection class
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EvaluasiView extends JFrame {
    private JTable evaluasiTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;

    public EvaluasiView() {
        setTitle("Daftar Evaluasi Sesi Kemoterapi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Table setup
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID Evaluasi");
        tableModel.addColumn("ID Jadwal Terapi");
        tableModel.addColumn("Kondisi Post Terapi");
        tableModel.addColumn("Efek Samping");
        tableModel.addColumn("Catatan");
        tableModel.addColumn("Tanggal Evaluasi");

        evaluasiTable = new JTable(tableModel);
        evaluasiTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(evaluasiTable);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        refreshButton = new JButton("Refresh Data");
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> loadEvaluasiData());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEvaluasiData(); // Load initial data
        setVisible(true);
    }

    private void loadEvaluasiData() {
        // Clear existing data
        tableModel.setRowCount(0);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.connect();
            if (conn != null) {
                String sql = "SELECT * FROM evaluasi_kemo ORDER BY tanggal_evaluasi DESC";
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    int evaluasiId = rs.getInt("evaluasi_id");
                    int jadwalId = rs.getInt("jadwal_id");
                    String kondisiPostTerapi = rs.getString("kondisi_post_terapi");
                    String efekSamping = rs.getString("efek_samping");
                    String catatan = rs.getString("catatan");
                    Timestamp tanggalEvaluasi = rs.getTimestamp("tanggal_evaluasi");

                    tableModel.addRow(new Object[]{evaluasiId, jadwalId, kondisiPostTerapi, efekSamping, catatan, tanggalEvaluasi});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching data from database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EvaluasiView());
    }
}
