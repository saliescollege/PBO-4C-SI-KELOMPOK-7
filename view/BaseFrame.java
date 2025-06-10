package PBO_4C_SI_KELOMPOK_7.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.BevelBorder; // Hanya butuh BevelBorder

public abstract class BaseFrame extends JFrame {

    protected String username;
    private JPanel navbarPanel;

    public BaseFrame(String title, String username) {
        this.username = username;
        setTitle(title);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initNavbar();
    }

    private void initNavbar() {
        // Gunakan GradientPanel untuk efek warna seperti tombol
        Color buttonLight = new Color(240, 240, 240);
        Color buttonDark = new Color(200, 200, 200);

        navbarPanel = new GradientPanel(buttonLight, buttonDark, false); // Gradien vertikal untuk tampilan cembung
        navbarPanel.setLayout(new BorderLayout());
        navbarPanel.setPreferredSize(new Dimension(900, 50));

        // Terapkan border bevel yang timbul untuk efek cembung
        navbarPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));


        // Kontainer untuk logo dan nama klinik di sebelah kiri
        JPanel leftNavContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); // FlowLayout.LEFT untuk posisi kiri
        leftNavContent.setOpaque(false); // Buat transparan agar latar belakang gradien terlihat

        ImageIcon logoIcon = new ImageIcon("src\\PBO_4C_SI_KELOMPOK_7\\assets\\Logo_Klinik.png"); // Pastikan path gambar benar
        Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaledLogo));
        logo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0)); // Padding kiri untuk logo
        logo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel klinikLabel = new JLabel("KLINIK SENTRA MEDIKA");
        klinikLabel.setFont(new Font("Arial", Font.BOLD, 18));
        klinikLabel.setForeground(Color.DARK_GRAY);
        klinikLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
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

        leftNavContent.add(logo);
        leftNavContent.add(klinikLabel);

        // Label user di kanan atas
        JLabel userLabel = new JLabel("👤 " + username);
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        userLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        userLabel.setForeground(Color.DARK_GRAY);
        userLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Profile(username).setVisible(true);
                dispose();
            }
        });

        // Tambahkan komponen ke BorderLayout navbarPanel
        navbarPanel.add(leftNavContent, BorderLayout.WEST); // Konten kiri di WEST
        navbarPanel.add(userLabel, BorderLayout.EAST); // Label user di EAST

        add(navbarPanel, BorderLayout.NORTH);
    }

    protected JPanel getNavbarPanel() {
        return navbarPanel;
    }
}