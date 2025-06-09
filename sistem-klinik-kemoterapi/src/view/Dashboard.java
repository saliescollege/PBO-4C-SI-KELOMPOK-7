package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import db.DBConnection;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dashboard extends JFrame {
    public Dashboard(String username) {
        setTitle("Dashboard");
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Navbar
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(new Color(173, 216, 230));
        navbar.setPreferredSize(new Dimension(900, 50));

        // Logo + teks di Navbar 
        ImageIcon logoIcon = new ImageIcon("assets/Logo-Klinik.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaledLogo));
        logo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
        logo.setVerticalAlignment(JLabel.TOP);

        JLabel klinikLabel = new JLabel("KLINIK SENTRA MEDIKA");
        klinikLabel.setFont(new Font("Arial", Font.BOLD, 18));
        klinikLabel.setForeground(Color.BLACK);
        klinikLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); 

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        logoPanel.add(logo);
        logoPanel.add(Box.createRigidArea(new Dimension(10, 0))); 
        logoPanel.add(klinikLabel);

        navbar.add(logoPanel, BorderLayout.WEST);

        // User label yang clickable
        JLabel userLabel = new JLabel("👤 " + username);
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        userLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        userLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // buka Profile dan tutup Dashboard
                new Profile(username).setVisible(true);
                Dashboard.this.dispose();
            }
        });
        navbar.add(userLabel, BorderLayout.EAST);

        // Panel kiri
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2, 1, 10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        ImageIcon pasienImg = new ImageIcon(new ImageIcon("assets/Pasien.png").getImage().getScaledInstance(480, 270, Image.SCALE_SMOOTH));
        JButton pasienBtn = new JButton("Pasien", pasienImg);
        pasienBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        pasienBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        pasienBtn.setPreferredSize(new Dimension(480, 270));

        ImageIcon dokterImg = new ImageIcon(new ImageIcon("assets/Dokter.png").getImage().getScaledInstance(480, 270, Image.SCALE_SMOOTH));
        JButton dokterBtn = new JButton("Dokter", dokterImg);
        dokterBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        dokterBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
        dokterBtn.setPreferredSize(new Dimension(480, 270));

        leftPanel.add(pasienBtn);
        leftPanel.add(dokterBtn);

        // Panel kanan (tabel jadwal)
        JPanel rightPanel = new JPanel(new BorderLayout());

        String tanggalHariIni = new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date());
        JLabel judul = new JLabel("Jadwal: " + tanggalHariIni);
        judul.setFont(new Font("Arial", Font.BOLD, 16));
        judul.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] kolom = {"Pasien", "Waktu", "Dokter"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);
        JTable tabel = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabel);

        rightPanel.add(judul, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(navbar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        loadJadwalHariIni(model);

        setVisible(true);
    }

    private void loadJadwalHariIni(DefaultTableModel model) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT p.nama_lengkap, " +
                         "       CONCAT(jt.jam_terapi, ' - ', jd.jam_selesai) AS waktu, " +
                         "       CONCAT(d.nama, ', ', d.spesialisasi) AS dokter " +
                         "FROM jadwal_terapi jt " +
                         "JOIN jadwal_dokter jd ON jt.jadwal_dokter_id = jd.jadwal_id " +
                         "JOIN rencana_terapi rt ON jt.terapi_id = rt.terapi_id " +
                         "JOIN pasien p ON rt.pasien_id = p.pasien_id " +
                         "JOIN dokter d ON rt.dokter_id = d.dokter_id " +
                         "WHERE jt.tanggal_terapi = CURDATE()";

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String pasien = rs.getString("nama_lengkap");
                String waktu = rs.getString("waktu");
                String dokter = rs.getString("dokter");
                model.addRow(new Object[]{pasien, waktu, dokter});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat jadwal: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Dashboard("User123");
    }
}