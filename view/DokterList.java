package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.DokterController;
import PBO_4C_SI_KELOMPOK_7.model.Dokter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class DokterList extends BaseFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private String currentUsername;

    public DokterList(String username) {
        super("Daftar Dokter", username);
        this.currentUsername = username;

        // Setup Tabel - Kolom "Detail" dihilangkan
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama Dokter", "Spesialisasi", "Jadwal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Semua sel tidak bisa diedit langsung
                return false;
            }
        };

        table = new JTable(tableModel);
        // Menyesuaikan tinggi baris untuk mengakomodasi jadwal multi-baris
        table.setRowHeight(90);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Hanya bisa pilih satu baris

        // Mendapatkan ColumnModel untuk mengatur lebar kolom
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(60);
        columnModel.getColumn(0).setMinWidth(30);

        // Custom Cell Renderer untuk kolom "Jadwal" agar bisa multi-baris
        table.getColumn("Jadwal").setCellRenderer(new MultiLineTableCellRenderer());
        
        // Kode untuk Button Renderer dan Editor dihilangkan karena kolomnya sudah tidak ada

        loadDokter();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Daftar Dokter"));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel bawah untuk menampung tombol "Lihat Detail"
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JButton btnDetail = new JButton("Lihat Detail Dokter");

        btnDetail.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    // Mendapatkan ID dokter dari kolom pertama (indeks 0) pada baris yang dipilih
                    int dokterId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                    
                    // Membuka jendela DokterDetail, mirip seperti pada PasienList
                    new DokterDetail(dokterId, currentUsername).setVisible(true);
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID Dokter tidak valid.", "Error Data", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Memberi tahu pengguna untuk memilih dokter dari tabel terlebih dahulu
                JOptionPane.showMessageDialog(this, "Pilih dokter terlebih dahulu dari daftar.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        bottomPanel.add(btnDetail);

        // Menambahkan panel utama dan panel tombol ke frame
        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    public void loadDokter() {
        List<Dokter> dokterList = DokterController.getAllDokter(); //
        tableModel.setRowCount(0);

        if (!dokterList.isEmpty()) {
            for (Dokter d : dokterList) {
                String jadwalHtml = "<html>";
                if (d.getJadwal().isEmpty()) { //
                    jadwalHtml += "Tidak Ada Jadwal";
                } else {
                    for (String s : d.getJadwal()) { //
                        jadwalHtml += s + "<br>";
                    }
                }
                jadwalHtml += "</html>";

                // Menambahkan data ke baris tabel, tanpa tombol "Lihat"
                tableModel.addRow(new Object[]{
                        d.getId(), //
                        d.getNama(), //
                        d.getSpesialisasi(), //
                        jadwalHtml
                });
            }
        }
    }
    
    public String getCurrentUsername() {
        return currentUsername;
    }
    
    // Kelas ini tetap diperlukan untuk merender jadwal multi-baris
    static class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {
        public MultiLineTableCellRenderer() {
            setOpaque(true);
            setLineWrap(true);
            setWrapStyleWord(true);
            setEditable(false);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "" : value.toString().replaceAll("<br>", "\n").replaceAll("<html>|</html>", ""));
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }
}