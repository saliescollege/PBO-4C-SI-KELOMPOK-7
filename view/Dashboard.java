package view;
import java.awt.*;
import javax.swing.*;

public class Dashboard extends JFrame {
    public Dashboard(String username, String role) {
        setTitle("Dashboard - " + role);
        setSize(900, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Navbar
        JPanel navbar = new JPanel();
        navbar.setBackground(new Color(173, 216, 230)); // light blue
        navbar.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        navbar.add(new JLabel("👤 " + username + " (" + role + ")"));
        navbar.add(new JButton("Beranda"));
        navbar.add(new JButton("Pasien"));
        navbar.add(new JButton("Dokter"));

        // Gambar panel kiri
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2, 1, 10, 10));

        ImageIcon pasienImg = new ImageIcon("assets/pasien.jpg");
        JButton pasienBtn = new JButton("Pasien", pasienImg);
        pasienBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        pasienBtn.setVerticalTextPosition(SwingConstants.BOTTOM);

        ImageIcon dokterImg = new ImageIcon("assets/dokter.jpg");
        JButton dokterBtn = new JButton("Dokter", dokterImg);
        dokterBtn.setHorizontalTextPosition(SwingConstants.CENTER);
        dokterBtn.setVerticalTextPosition(SwingConstants.BOTTOM);

        leftPanel.add(pasienBtn);
        leftPanel.add(dokterBtn);

        // Jadwal Panel
        String[] kolom = {"Pasien", "Waktu", "Dokter"};
        Object[][] data = {
            {"Amanda Puteri", "09.30 - 11.30", "dr. Sinta Chaira, Sp.B (K) Onk"},
            {"", "", ""},
            {"", "", ""},
            {"", "", ""}
        };
        JTable tabelJadwal = new JTable(data, kolom);
        JScrollPane scrollPane = new JScrollPane(tabelJadwal);

        JPanel jadwalPanel = new JPanel(new BorderLayout());
        JLabel tanggal = new JLabel("📅 Jadwal - Senin, 17 Januari 2025");
        tanggal.setFont(new Font("Arial", Font.BOLD, 16));
        tanggal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jadwalPanel.add(tanggal, BorderLayout.NORTH);
        jadwalPanel.add(scrollPane, BorderLayout.CENTER);

        // Gabungkan panel
        setLayout(new BorderLayout());
        add(navbar, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(jadwalPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
