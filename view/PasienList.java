package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.PasienController;
import PBO_4C_SI_KELOMPOK_7.model.Pasien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PasienList extends BaseFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private String currentUsername;

    public PasienList(String username) {
        super("Daftar Pasien", username);
        this.currentUsername = username;

        // Tabel
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama Lengkap", "No. Telepon", "Dokter Penanggung Jawab"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // agar sel tabel tidak bisa diedit langsung
            }
        };

        table = new JTable(tableModel);
        loadPasien(); // Initial load

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Pasien"));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Tombol bawah
        JButton btnDetail = new JButton("Lihat Detail");
        btnDetail.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    // Mendapatkan ID pasien dari kolom pertama baris yang dipilih
                    int pasienId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                    // Membuka jendela PasienDetail
                    new PasienDetail(pasienId, currentUsername).setVisible(true);
                } catch (NumberFormatException ex) {
                    // Menangani jika ID pasien bukan angka
                    JOptionPane.showMessageDialog(this, "ID Pasien tidak valid. Error: " + ex.getMessage(), "Error Data", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    // Menangani kesalahan umum lainnya saat membuka detail
                    JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat membuka detail pasien: " + ex.getMessage(), "Error Aplikasi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace(); // Cetak stack trace untuk debugging lebih lanjut
                }
            } else {
                // Memberi tahu pengguna untuk memilih pasien terlebih dahulu
                JOptionPane.showMessageDialog(this, "Pilih pasien terlebih dahulu.");
            }
        });

        JButton btnTambah = new JButton("Tambah Pasien");
        btnTambah.addActionListener(e -> {
            new PasienTambahForm(currentUsername, this).setVisible(true);
            this.setVisible(false); // Hide this PasienList temporarily
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(btnTambah);
        bottomPanel.add(btnDetail);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void loadPasien() {
        List<Pasien> pasienList = PasienController.getAllPasien();
        tableModel.setRowCount(0);

        if (!pasienList.isEmpty()) {
            for (Pasien p : pasienList) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getNama(),
                        p.getTelepon(),
                        p.getDosis() // This will now contain the dokter_nama
                });
            }
        }
    }
}