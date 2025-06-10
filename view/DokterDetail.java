// PBO_4C_SI_KELOMPOK_7/view/DokterDetail.java
package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.DokterController;
import PBO_4C_SI_KELOMPOK_7.model.Dokter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DokterDetail extends BaseFrame {

    private JTextPane detailTextPane;
    private int dokterId;

    public DokterDetail(int dokterId, String username) {
        super("Detail Dokter", username);
        this.dokterId = dokterId;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        Dokter dokter = DokterController.getDokterById(dokterId);
        if (dokter == null) {
            JOptionPane.showMessageDialog(this, "Data dokter tidak ditemukan.");
            dispose();
            return;
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        detailTextPane = new JTextPane();
        detailTextPane.setEditable(false);
        detailTextPane.setContentType("text/html");
        detailTextPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        detailTextPane.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel contentWrapperPanel = new JPanel(new GridBagLayout());
        contentWrapperPanel.setBackground(Color.WHITE);
        contentWrapperPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentWrapperPanel.add(detailTextPane, gbc);

        JScrollPane scrollPane = new JScrollPane(contentWrapperPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        populateDetail(dokter);

        JButton pasienDitanganiBtn = new JButton("Pasien yang Ditangani");
        pasienDitanganiBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pasienDitanganiBtn.setBackground(new Color(100, 149, 237));
        pasienDitanganiBtn.setForeground(Color.WHITE);
        pasienDitanganiBtn.setFocusPainted(false);
        pasienDitanganiBtn.addActionListener(e -> {
            // Membuka frame PasienDitangani dengan mengirimkan ID, nama dokter, dan username
            new PasienDitangani(dokter.getId(), dokter.getNama(), username).setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        buttonPanel.add(pasienDitanganiBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void populateDetail(Dokter d) {
        // ... (Isi metode ini sama seperti sebelumnya, tidak perlu diubah) ...
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: SansSerif; font-size: 12pt; margin: 15px; background-color: white;'>");
        sb.append("<h2 style='text-align: center; color: #333;'>INFORMASI LENGKAP DOKTER</h2>");
        sb.append("<hr style='border: 0.5px solid #ccc;'><br>");
        sb.append("<p><b>DATA DOKTER:</b></p>");
        sb.append("<table border='0' cellspacing='0' cellpadding='4'>");
        sb.append(String.format("<tr><td width='150'>ID Dokter</td><td>: %d</td></tr>", d.getId()));
        sb.append(String.format("<tr><td>Nama Lengkap</td><td>: %s</td></tr>", d.getNama()));
        sb.append(String.format("<tr><td>Spesialisasi</td><td>: %s</td></tr>", d.getSpesialisasi()));
        sb.append(String.format("<tr><td>Pendidikan</td><td>: %s</td></tr>", d.getPendidikan()));
        sb.append(String.format("<tr><td>Legalitas</td><td>: %s</td></tr>", d.getLegalitas()));
        sb.append("</table><br>");
        sb.append("<p><b>JADWAL DOKTER:</b></p>");
        sb.append("<table border='0' cellspacing='0' cellpadding='4'>");
        List<String> schedules = d.getJadwal();
        if (schedules.isEmpty()) {
            sb.append("<tr><td colspan='2'>Tidak ada jadwal tersedia.</td></tr>");
        } else {
            for (String schedule : schedules) {
                sb.append(String.format("<tr><td width='150'></td><td>%s</td></tr>", schedule));
            }
        }
        sb.append("</table><br>");
        sb.append("</body></html>");
        detailTextPane.setText(sb.toString());
        detailTextPane.setCaretPosition(0);
    }
}