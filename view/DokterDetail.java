package view;
import javax.swing.*;
import java.awt.*;

public class DokterDetail extends JFrame {

    public DokterDetail(String nama, String spesialisasi, String ruangan, String jadwal) {
        setTitle("Detail Dokter");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[][] data = {
            {"Nama", nama},
            {"Spesialisasi", spesialisasi},
            {"Ruangan", ruangan},
            {"Jadwal", jadwal},
            {"Pendidikan", "Universitas Kedokteran Indonesia"},
            {"Legalitas", "STR aktif, SIP No. 123456789"}
        };

        String[] kolom = {"Keterangan", "Detail"};

        JTable detailTable = new JTable(data, kolom) {
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        detailTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailTable.setRowHeight(28);
        detailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        JButton pasienBtn = new JButton("Pasien yang Ditangani");
        pasienBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pasienBtn.setBackground(new Color(100, 149, 237));
        pasienBtn.setForeground(Color.WHITE);
        pasienBtn.setFocusPainted(false);
        pasienBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Fitur menampilkan pasien akan ditambahkan."));

        JPanel tombolPanel = new JPanel();
        tombolPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        tombolPanel.add(pasienBtn);
        add(tombolPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
