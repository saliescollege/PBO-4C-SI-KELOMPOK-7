package PBO_4C_SI_KELOMPOK_7.view; // <-- Menggunakan package yang sesuai

import PBO_4C_SI_KELOMPOK_7.controller.PasienController;
import PBO_4C_SI_KELOMPOK_7.model.Pasien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PasienList extends BaseFrame { // <-- Mengubah dari JFrame menjadi BaseFrame

    private JTable table;
    private DefaultTableModel tableModel;

    public PasienList(String username) {
        super("Daftar Pasien", username); // <-- Panggil konstruktor BaseFrame

        // Hapus kode navbar yang sebelumnya ada di sini karena sudah ditangani oleh BaseFrame.

        // Tabel
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama Lengkap", "No. Telepon", "Dokter Penanggung Jawab"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // agar sel tabel tidak bisa diedit langsung
            }
        };

        table = new JTable(tableModel);
        loadPasien();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Pasien"));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Tombol bawah
        JButton btnDetail = new JButton("Lihat Detail");
        btnDetail.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int pasienId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                // PERUBAHAN: Sekarang PasienDetail menerima username
                new PasienDetail(pasienId, username).setVisible(true); //
                // dispose(); // Opsional, kalau mau tutup PasienList saat buka detail
            } else {
                JOptionPane.showMessageDialog(this, "Pilih pasien terlebih dahulu.");
            }
        });

        JButton btnTambah = new JButton("Tambah Pasien");
        btnTambah.addActionListener(e -> {
            new PasienTambahForm(username).setVisible(true);
            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(btnTambah);
        bottomPanel.add(btnDetail);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadPasien() {
        List<Pasien> pasienList = PasienController.getAllPasien();
        tableModel.setRowCount(0);

        if (!pasienList.isEmpty()) {
            for (Pasien p : pasienList) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getNama(),
                        p.getTelepon(),
                        "Dokter ID: " + p.getDokterId()
                });
            }
        }
    }
}