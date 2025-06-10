package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.PasienController;
import PBO_4C_SI_KELOMPOK_7.model.DokterJadwal;
import PBO_4C_SI_KELOMPOK_7.model.Pasien;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PasienDetail extends BaseFrame {
    private JTextPane detailTextPane;
    private int pasienId;
    private String username;
    private JButton btnGenerateJadwal;
    private JButton btnLihatJadwal;
    private String jadwalTerakhirHTML = null;
    private List<LocalDate> jadwalTerakhirList = null;


    public PasienDetail(int pasienId, String username) {
        super("Detail Pasien", username);
        this.pasienId = pasienId;
        this.username = username;
        // ... (Kode konstruktor lainnya sama) ...
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        Pasien pasien = PasienController.getPasienById(pasienId);
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Data pasien tidak ditemukan.");
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

        JScrollPane scrollPane = new JScrollPane(detailTextPane);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        populateDetail(pasien);

        // --- PANEL TOMBOL ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnGenerateJadwal = new JButton("Generate Jadwal");
        btnLihatJadwal = new JButton("Lihat Jadwal");
        btnGenerateJadwal.addActionListener(e -> handleGenerateJadwal());
        btnLihatJadwal.addActionListener(e -> handleLihatJadwal());
        btnLihatJadwal.setVisible(false);
        

        JButton btnEvaluasi = new JButton("Tambah Evaluasi Sesi");
        btnEvaluasi.addActionListener(e -> {
            // Membuka form tambah evaluasi, mengirimkan frame ini sebagai parent, beserta ID dan nama pasien
            new EvaluasiTambah(this, pasienId, pasien.getNama()).setVisible(true);
        });

        JButton btnHapus = new JButton("Hapus Pasien");
        btnHapus.setBackground(new Color(220, 53, 69));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "<html>Anda yakin ingin menghapus data pasien ini?<br>Semua data terkait juga akan terhapus.</html>", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (PasienController.deletePasien(pasienId)) {
                    JOptionPane.showMessageDialog(this, "Data pasien berhasil dihapus.");
                    new PasienList(username).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data pasien.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> dispose());
        buttonPanel.add(btnGenerateJadwal);
        buttonPanel.add(btnLihatJadwal);
        buttonPanel.add(btnEvaluasi);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnTutup);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    private void handleGenerateJadwal() {
        // ... (Kode input dialog sama seperti sebelumnya) ...
        String startDateStr = JOptionPane.showInputDialog(this, "Masukkan tanggal mulai (format: yyyy-MM-dd):", LocalDate.now().toString());
        if (startDateStr == null || startDateStr.trim().isEmpty()) return;
        String cyclesStr = JOptionPane.showInputDialog(this, "Masukkan jumlah siklus yang akan dibuat:", "6");
        if (cyclesStr == null || cyclesStr.trim().isEmpty()) return;

        try {
            LocalDate tanggalMulai = LocalDate.parse(startDateStr);
            int jumlahSiklus = Integer.parseInt(cyclesStr);

            Pasien pasien = PasienController.getPasienById(this.pasienId);
            List<DokterJadwal> jadwalDokter = PasienController.getDokterSchedules(pasien.getDokterId());
            
            // Simpan hasil generate ke variabel instance
            this.jadwalTerakhirList = generateJadwalTerapi(pasien, jadwalDokter, tanggalMulai, jumlahSiklus);
            this.jadwalTerakhirHTML = buildJadwalHtml(pasien, this.jadwalTerakhirList);

            // Tampilkan hasil dengan tombol Simpan
            displayJadwalPopup(this.jadwalTerakhirHTML, "Hasil Generate Jadwal", true);

            btnGenerateJadwal.setVisible(false);
            btnLihatJadwal.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Input tidak valid atau terjadi error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLihatJadwal() {
        if (this.jadwalTerakhirHTML != null) {
            // Tampilkan kembali hasil, tapi tanpa tombol Simpan
            displayJadwalPopup(this.jadwalTerakhirHTML, "Jadwal Terakhir Dibuat", false);
        }
    }

    private void displayJadwalPopup(String htmlContent, String title, boolean showSaveButton) {
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText(htmlContent);
        textPane.setEditable(false);
        textPane.setMargin(new Insets(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(450, 350));

        // Opsi tombol untuk JOptionPane
        Object[] options;
        if (showSaveButton) {
            options = new Object[]{"Simpan ke Database", "Tutup"};
        } else {
            options = new Object[]{"Tutup"};
        }

        JOptionPane optionPane = new JOptionPane(scrollPane, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, options[options.length-1]);
        JDialog dialog = optionPane.createDialog(title);
        
        optionPane.addPropertyChangeListener(e -> {
            String prop = e.getPropertyName();
            if (dialog.isVisible() && (e.getSource() == optionPane) && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                Object value = optionPane.getValue();

                // Jika tombol "Simpan ke Database" diklik
                if (value.equals("Simpan ke Database")) {
                    boolean success = PasienController.simpanJadwalTerapi(this.pasienId, this.jadwalTerakhirList);
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Jadwal berhasil disimpan ke database!");
                        dialog.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Gagal menyimpan jadwal.", "Error", JOptionPane.ERROR_MESSAGE);
                        // Reset value agar dialog tidak tertutup
                        optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE); 
                    }
                } else { // Jika tombol "Tutup" diklik
                    dialog.setVisible(false);
                }
            }
        });
        
        dialog.setVisible(true);
    }

     private String buildJadwalHtml(Pasien pasien, List<LocalDate> jadwal) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: SansSerif; font-size: 10pt; padding: 10px;'>");
        sb.append("<h2 style='text-align: center; color: #333;'>JADWAL RENCANA TERAPI</h2>");
        sb.append("<hr style='border: 0.5px solid #ccc;'><br>");

        sb.append("<table border='0' cellspacing='0' cellpadding='3' width='100%'>");
        sb.append("<tr><td width='110'><b>Nama Pasien</b></td><td>: ").append(pasien.getNama()).append("</td></tr>");
        sb.append("<tr><td><b>Dokter PJ</b></td><td>: ").append(pasien.getDokterNama()).append("</td></tr>");
        sb.append("<tr><td><b>Siklus</b></td><td>: ").append(pasien.getSiklus()).append("</td></tr>");
        sb.append("</table><br>");

        sb.append("<p><b>PERKIRAAN JADWAL SESI KEMOTERAPI:</b></p>");
        sb.append("<table border='1' cellspacing='0' cellpadding='5' width='100%' style='border-collapse: collapse; border: 1px solid #ccc;'>");
        sb.append("<tr style='background-color: #f0f0f0; text-align: center;'><th>Sesi Ke-</th><th>Perkiraan Tanggal Terapi</th></tr>");

        if (jadwal.isEmpty()) {
            sb.append("<tr><td colspan='2' style='text-align:center;'>Tidak ada jadwal yang bisa dibuat.</td></tr>");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
            for (int i = 0; i < jadwal.size(); i++) {
                sb.append(String.format("<tr><td style='text-align:center;'>%d</td><td>%s</td></tr>", (i + 1), jadwal.get(i).format(formatter)));
            }
        }

        sb.append("</table>");
        sb.append("<br><p style='font-size: 8pt; color: gray;'><i>*Jadwal ini adalah perkiraan. Konfirmasi akhir akan dilakukan oleh administrasi.</i></p>");
        sb.append("</body></html>");
        return sb.toString();
    }
    
    private List<LocalDate> generateJadwalTerapi(Pasien pasien, List<DokterJadwal> jadwalDokter, LocalDate tanggalMulai, int jumlahSiklus) {
        List<LocalDate> hasilJadwal = new ArrayList<>();
        int intervalSiklus = 0;
        try {
            intervalSiklus = Integer.parseInt(pasien.getSiklus().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return hasilJadwal;
        }

        if (intervalSiklus <= 0) return hasilJadwal;

        List<DayOfWeek> hariPraktik = jadwalDokter.stream()
            .map(DokterJadwal::getHari)
            .distinct()
            .collect(Collectors.toList());

        if (hariPraktik.isEmpty()) return hasilJadwal;

        LocalDate tanggalSiklusBerikutnya = tanggalMulai;

        for (int i = 0; i < jumlahSiklus; i++) {
            LocalDate tanggalValid = tanggalSiklusBerikutnya;
            boolean tanggalDitemukan = false;
            for (int j = 0; j < 14 && !tanggalDitemukan; j++) {
                if (hariPraktik.contains(tanggalValid.getDayOfWeek())) {
                    hasilJadwal.add(tanggalValid);
                    tanggalDitemukan = true;
                } else {
                    tanggalValid = tanggalValid.plusDays(1);
                }
            }
            tanggalSiklusBerikutnya = tanggalSiklusBerikutnya.plusDays(intervalSiklus);
        }
        return hasilJadwal;
    }

    private void populateDetail(Pasien p) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
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
        detailTextPane.setCaretPosition(0);
    }
}