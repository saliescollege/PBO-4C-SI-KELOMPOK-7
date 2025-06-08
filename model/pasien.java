package model;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Pasien extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel emptyLabel;

    public Pasien() {
        setTitle("asien");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel(new BorderLayout());

        // Tombol tambah pasien
        JButton btnTambah = new JButton("Tambah Pasien");
        btnTambah.addActionListener(e -> tambahPasien());

        panel.add(btnTambah, BorderLayout.NORTH);

        // Model tabel
        tableModel = new DefaultTableModel(new Object[]{"Nama Lengkap", "No. Telepon", "Dokter Penanggung Jawab"}, 0);
        table = new JTable(tableModel);

        // Scroll pane untuk tabel
        JScrollPane scrollPane = new JScrollPane(table);

        // Label jika data kosong
        emptyLabel = new JLabel("Tidak ada pasien terdaftar", SwingConstants.CENTER);

        // Cek awal apakah ada data
        updateDisplay(panel, scrollPane);

        add(panel);
    }

    private void updateDisplay(JPanel panel, JScrollPane scrollPane) {
        if (tableModel.getRowCount() == 0) {
            panel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            panel.remove(emptyLabel);
            panel.add(scrollPane, BorderLayout.CENTER);
        }
        panel.revalidate();
        panel.repaint();
    }

    private void tambahPasien() {
        JTextField namaField = new JTextField();
        JTextField teleponField = new JTextField();
        JTextField dokterField = new JTextField();

        Object[] message = {
            "Nama Lengkap:", namaField,
            "No. Telepon:", teleponField,
            "Dokter Penanggung Jawab:", dokterField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Tambah Pasien", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nama = namaField.getText();
            String telepon = teleponField.getText();
            String dokter = dokterField.getText();

            if (!nama.isEmpty() && !telepon.isEmpty() && !dokter.isEmpty()) {
                tableModel.addRow(new Object[]{nama, telepon, dokter});
                updateDisplay((JPanel) getContentPane().getComponent(0), new JScrollPane(table));
            } else {
                JOptionPane.showMessageDialog(this, "Semua kolom harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Pasien().setVisible(true));
    }
}