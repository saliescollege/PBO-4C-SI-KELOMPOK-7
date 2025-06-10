package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.PasienController;
import PBO_4C_SI_KELOMPOK_7.model.Pasien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PasienDitangani extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public PasienDitangani(int dokterId, String dokterNama, String username) {
        setTitle("Pasien Ditangani oleh " + dokterNama);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Agar hanya menutup jendela ini
        setLayout(new BorderLayout(10, 10));

        // Panel utama dengan padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Judul
        JLabel titleLabel = new JLabel("Daftar Pasien Ditangani oleh " + dokterNama, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Setup Tabel
        String[] columns = {"ID Pasien", "Nama Pasien", "No. Telepon"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat sel tidak dapat diedit
            }
        };
        table = new JTable(tableModel);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Tombol tutup
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Tutup");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Memuat data pasien dari controller
        loadPasienDitangani(dokterId);
    }

    private void loadPasienDitangani(int dokterId) {
        // Panggil metode baru dari PasienController
        List<Pasien> pasienList = PasienController.getPasienByDokterId(dokterId);
        tableModel.setRowCount(0); // Kosongkan tabel sebelum diisi

        if (!pasienList.isEmpty()) {
            for (Pasien p : pasienList) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getNama(),
                        p.getTelepon()
                });
            }
        } else {
            // Jika tidak ada pasien, tambahkan pesan di tabel
            tableModel.addRow(new Object[]{"-", "Belum ada pasien yang ditangani", "-"});
        }
    }
}