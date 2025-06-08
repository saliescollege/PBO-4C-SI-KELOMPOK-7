package view;

import controller.PasienController;
import model.Pasien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PasienList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public PasienList() {
        setTitle("Daftar Pasien");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama Lengkap", "No. Telepon", "Dokter Penanggung Jawab"}, 0);
        table = new JTable(tableModel);
        loadPasien();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Pasien"));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnDetail = new JButton("Lihat Detail");
        btnDetail.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int pasienId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                new PasienDetail(pasienId).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih pasien terlebih dahulu.");
            }
        });

        JButton btnTambah = new JButton("Tambah Pasien");
        btnTambah.addActionListener(e -> new PasienTambahForm().setVisible(true));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnTambah);
        bottomPanel.add(btnDetail);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadPasien() {
        List<Pasien> pasienList = PasienController.getAllPasien();
        tableModel.setRowCount(0); // clear previous data

        if (pasienList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada pasien terdaftar.");
        } else {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PasienList().setVisible(true));
    }
}