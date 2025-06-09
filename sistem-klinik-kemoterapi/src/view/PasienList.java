package view;

import controller.PasienController;
import model.Pasien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PasienList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private final String username;

    public PasienList(String username) {
        this.username = username;

        setTitle("Daftar Pasien");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Navbar
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(new Color(173, 216, 230));
        navbar.setPreferredSize(new Dimension(900, 50));

        ImageIcon logoIcon = new ImageIcon("assets/Logo-Klinik.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaledLogo));
        logo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        logo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel klinikLabel = new JLabel("KLINIK SENTRA MEDIKA");
        klinikLabel.setFont(new Font("Arial", Font.BOLD, 18));
        klinikLabel.setForeground(Color.BLACK);
        klinikLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        klinikLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        MouseAdapter toDashboard = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Dashboard(username).setVisible(true);
                dispose();
            }
        };
        logo.addMouseListener(toDashboard);
        klinikLabel.addMouseListener(toDashboard);

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        logoPanel.add(logo);
        logoPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        logoPanel.add(klinikLabel);

        JLabel userLabel = new JLabel("👤 " + username);
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        navbar.add(logoPanel, BorderLayout.WEST);
        navbar.add(userLabel, BorderLayout.EAST);
        add(navbar, BorderLayout.NORTH);

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
                // Perbaikan: panggil konstruktor PasienDetail yang hanya butuh pasienId
                new PasienDetail(pasienId).setVisible(true);
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
}
