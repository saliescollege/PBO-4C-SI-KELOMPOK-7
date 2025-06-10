package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.PasienController;
import PBO_4C_SI_KELOMPOK_7.model.Pasien;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class PasienDetail extends BaseFrame {

    private JTextPane detailTextPane; // Changed to JTextPane for rich text capabilities
    private int pasienId;
    private String username;

    public PasienDetail(int pasienId, String username) {
        super("Detail Pasien", username);
        this.pasienId = pasienId;
        this.username = username;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setSize(800, 600); // Give more space for centering

        Pasien pasien = PasienController.getPasienById(pasienId);
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Data pasien tidak ditemukan.");
            dispose();
            return;
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Use JTextPane for document-like format and better font control
        detailTextPane = new JTextPane();
        detailTextPane.setEditable(false);
        detailTextPane.setContentType("text/html"); // Allow HTML for better formatting/centering
        
        // Initial styling for the JTextPane
        detailTextPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE); // Use setFont
        detailTextPane.setFont(new Font("SansSerif", Font.PLAIN, 12)); // A proper, readable font
        
        // This is a common way to simulate centering content within JTextPane
        // by wrapping it in an HTML div with text-align: center
        // However, for more precise "document" style, a fixed-width container with
        // padding is often better than simply text-align: center on all content.
        // Let's create a dedicated content panel that is centered.

        JPanel contentWrapperPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout to center
        contentWrapperPanel.setBackground(Color.WHITE); // White background for document feel
        contentWrapperPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); // Subtle border
        
        // Add the JTextPane to the wrapper panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Inner padding for the "document"
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentWrapperPanel.add(detailTextPane, gbc);

        JScrollPane scrollPane = new JScrollPane(contentWrapperPanel); // ScrollPane wraps the contentWrapper
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default scroll pane border

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Populate text area with formatted data
        populateDetail(pasien);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton btnEvaluasi = new JButton("Evaluasi Sesi");
        btnEvaluasi.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Fitur evaluasi untuk pasien ini akan segera ditambahkan.");
            // To integrate with EvaluasiTambah, you would typically pass patient details:
            // new EvaluasiTambah(pasien.getNama(), pasienId).setVisible(true); // Assuming EvaluasiTambah needs patient name and ID
            // dispose(); // If you want to close PasienDetail when opening EvaluasiTambah
        });

        JButton btnHapus = new JButton("Hapus Pasien");
        btnHapus.setBackground(new Color(220, 53, 69)); // Red for delete
        btnHapus.setForeground(Color.WHITE);
        btnHapus.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "<html>Anda yakin ingin menghapus data pasien ini?<br>" +
                "Semua data terkait (diagnosa, pemeriksaan, terapi, jadwal, evaluasi) juga akan terhapus.</html>",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                if (PasienController.deletePasien(pasienId)) {
                    JOptionPane.showMessageDialog(this, "Data pasien berhasil dihapus.");
                    new PasienList(username).setVisible(true); // Reopen/refresh list
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data pasien.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> dispose()); // Just close this window

        buttonPanel.add(btnEvaluasi);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnTutup);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void populateDetail(Pasien p) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        // Using HTML for better formatting in JTextPane
        sb.append("<html><body style='font-family: SansSerif; font-size: 12pt; margin: 15px; background-color: white;'>");
        sb.append("<h2 style='text-align: center; color: #333;'>INFORMASI LENGKAP PASIEN</h2>");
        sb.append("<hr style='border: 0.5px solid #ccc;'><br>");

        sb.append("<p><b>DATA PRIBADI:</b></p>");
        sb.append("<table border='0' cellspacing='0' cellpadding='4'>");
        sb.append(String.format("<tr><td width='150'>ID Pasien</td><td>: %d</td></tr>", p.getId()));
        sb.append(String.format("<tr><td>Nama Lengkap</td><td>: %s</td></tr>", p.getNama()));
        sb.append(String.format("<tr><td>Alamat</td><td>: %s</td></tr>", p.getAlamat()));
        sb.append(String.format("<tr><td>No. Telepon</td><td>: %s</td></tr>", p.getTelepon()));
        sb.append(String.format("<tr><td>Tanggal Lahir</td><td>: %s</td></tr>", p.getTanggalLahir() != null ? p.getTanggalLahir().format(dateFormatter) : "-"));
        sb.append(String.format("<tr><td>Jenis Kelamin</td><td>: %s</td></tr>", p.getKelamin()));
        sb.append(String.format("<tr><td>Dokter PJ</td><td>: %s (ID: %d)</td></tr>", p.getDokterNama(), p.getDokterId()));
        sb.append("</table><br>");

        sb.append("<p><b>DIAGNOSA:</b></p>");
        sb.append("<table border='0' cellspacing='0' cellpadding='4'>");
        sb.append(String.format("<tr><td width='150'>Diagnosa Utama</td><td>: %s</td></tr>", p.getDiagnosa() != null ? p.getDiagnosa() : "-"));
        sb.append(String.format("<tr><td>Histopatologi</td><td>: %s</td></tr>", p.getHistopatologi() != null ? p.getHistopatologi() : "-"));
        sb.append("</table><br>");

        sb.append("<p><b>PEMERIKSAAN FISIK & PENUNJANG:</b></p>");
        sb.append("<table border='0' cellspacing='0' cellpadding='4'>");
        sb.append(String.format("<tr><td width='150'>Tekanan Darah</td><td>: %s mmHg</td></tr>", p.getTekananDarah() != null ? p.getTekananDarah() : "-"));
        sb.append(String.format("<tr><td>Suhu Tubuh</td><td>: %s °C</td></tr>", p.getSuhuTubuh() != null ? p.getSuhuTubuh() : "-"));
        sb.append(String.format("<tr><td>Denyut Nadi</td><td>: %s bpm</td></tr>", p.getDenyutNadi() != null ? p.getDenyutNadi() : "-"));
        sb.append(String.format("<tr><td>Berat Badan</td><td>: %s kg</td></tr>", p.getBeratBadan() != null ? p.getBeratBadan() : "-"));
        sb.append(String.format("<tr><td>Hemoglobin (HB)</td><td>: %s g/dL</td></tr>", p.getHb() != null ? p.getHb() : "-"));
        sb.append(String.format("<tr><td>Leukosit</td><td>: %s /µL</td></tr>", p.getLeukosit() != null ? p.getLeukosit() : "-"));
        sb.append(String.format("<tr><td>Trombosit</td><td>: %s /µL</td></tr>", p.getTrombosit() != null ? p.getTrombosit() : "-"));
        sb.append(String.format("<tr><td>Fungsi Hati</td><td>: %s</td></tr>", p.getFungsiHati() != null ? p.getFungsiHati() : "-"));
        sb.append(String.format("<tr><td>Fungsi Ginjal</td><td>: %s</td></tr>", p.getFungsiGinjal() != null ? p.getFungsiGinjal() : "-"));
        sb.append("</table><br>");

        sb.append("<p><b>RENCANA TERAPI KEMOTERAPI:</b></p>");
        sb.append("<table border='0' cellspacing='0' cellpadding='4'>");
        sb.append(String.format("<tr><td width='150'>Jenis Kemoterapi</td><td>: %s</td></tr>", p.getJenisKemoterapi() != null ? p.getJenisKemoterapi() : "-"));
        sb.append(String.format("<tr><td>Dosis</td><td>: %s</td></tr>", p.getDosis() != null ? p.getDosis() : "-"));
        sb.append(String.format("<tr><td>Siklus</td><td>: %s</td></tr>", p.getSiklus() != null ? p.getSiklus() : "-"));
        sb.append(String.format("<tr><td>Premedikasi</td><td>: %s</td></tr>", p.getPremedikasi() != null ? p.getPremedikasi() : "-"));
        sb.append(String.format("<tr><td>Akses Vena</td><td>: %s</td></tr>", p.getAksesVena() != null ? p.getAksesVena() : "-"));
        sb.append("</table><br>");
        
        sb.append("</body></html>");

        detailTextPane.setText(sb.toString());
        detailTextPane.setCaretPosition(0); // Scroll to top
    }
}