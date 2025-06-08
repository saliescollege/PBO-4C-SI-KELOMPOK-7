import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;

public class EvaluasiSesiKemo extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public EvaluasiSesiKemo() {
        setTitle("Evaluasi Sesi Kemoterapi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        EvaluasiSesiKemoPanel evaluasiPanel = new EvaluasiSesiKemoPanel();
        HasilEvaluasiPanel hasilPanel = new HasilEvaluasiPanel();

        cardPanel.add(evaluasiPanel, "Evaluasi");
        cardPanel.add(hasilPanel, "Hasil");

        // Action untuk tombol submit evaluasi
        evaluasiPanel.submitButton.addActionListener(e -> {
            if (validasiInput(evaluasiPanel)) {
                StringBuilder hasilEvaluasi = new StringBuilder();
                hasilEvaluasi.append("HASIL EVALUASI SESI KEMOTERAPI\n\n");
                
                hasilEvaluasi.append("INFORMASI SESI\n");
                hasilEvaluasi.append("Nama Pasien: ").append(evaluasiPanel.namaPasien.getText()).append("\n");
                hasilEvaluasi.append("Tanggal Sesi: ").append(evaluasiPanel.tanggalSesi.getText()).append("\n");
                hasilEvaluasi.append("Sesi Ke: ").append(evaluasiPanel.sesiKe.getText()).append("\n");
                hasilEvaluasi.append("Durasi Terapi: ").append(evaluasiPanel.durasiTerapi.getText()).append(" jam\n\n");

                hasilEvaluasi.append("KONDISI FISIK SELAMA TERAPI\n");
                hasilEvaluasi.append("Tekanan Darah: ").append(evaluasiPanel.tekananDarahSesi.getText()).append(" mmHg\n");
                hasilEvaluasi.append("Suhu Tubuh: ").append(evaluasiPanel.suhuTubuhSesi.getText()).append(" °C\n");
                hasilEvaluasi.append("Denyut Nadi: ").append(evaluasiPanel.nadiSesi.getText()).append(" bpm\n");
                hasilEvaluasi.append("Saturasi Oksigen: ").append(evaluasiPanel.saturasiOksigen.getText()).append(" %\n\n");

                hasilEvaluasi.append("EFEK SAMPING YANG DIALAMI\n");
                hasilEvaluasi.append("Mual/Muntah: ").append(evaluasiPanel.mualMuntah.getSelectedItem()).append("\n");
                hasilEvaluasi.append("Kelelahan: ").append(evaluasiPanel.kelelahan.getSelectedItem()).append("\n");
                hasilEvaluasi.append("Demam: ").append(evaluasiPanel.demam.getSelectedItem()).append("\n");
                hasilEvaluasi.append("Diare: ").append(evaluasiPanel.diare.getSelectedItem()).append("\n");
                hasilEvaluasi.append("Gangguan Nafsu Makan: ").append(evaluasiPanel.nafsuMakan.getSelectedItem()).append("\n");
                hasilEvaluasi.append("Nyeri/Sakit: ").append(evaluasiPanel.nyeri.getSelectedItem()).append("\n");
                hasilEvaluasi.append("Efek Samping Lain: ").append(evaluasiPanel.efekSampingLain.getText()).append("\n\n");

                hasilEvaluasi.append("RESPON TERHADAP TERAPI\n");
                hasilEvaluasi.append("Toleransi Pasien: ").append(evaluasiPanel.toleransiPasien.getSelectedItem()).append("\n");
                hasilEvaluasi.append("Mood/Kondisi Psikologis: ").append(evaluasiPanel.kondisiPsikologis.getSelectedItem()).append("\n");
                hasilEvaluasi.append("Komplikasi: ").append(evaluasiPanel.komplikasi.getText()).append("\n");
                hasilEvaluasi.append("Catatan Khusus: ").append(evaluasiPanel.catatanKhusus.getText()).append("\n\n");

                hasilEvaluasi.append("REKOMENDASI TINDAK LANJUT\n");
                hasilEvaluasi.append("Perubahan Dosis: ").append(evaluasiPanel.perubahanDosis.getSelectedItem()).append("\n");
                hasilEvaluasi.append("Jadwal Sesi Berikutnya: ").append(evaluasiPanel.sesiBerikutnya.getText()).append("\n");
                hasilEvaluasi.append("Obat Pendukung: ").append(evaluasiPanel.obatPendukung.getText()).append("\n");
                hasilEvaluasi.append("Perawatan Khusus: ").append(evaluasiPanel.perawatanKhusus.getText()).append("\n");
                hasilEvaluasi.append("Petugas Evaluator: ").append(evaluasiPanel.petugasEvaluator.getText()).append("\n\n");

                // Analisis dan Kesimpulan
                hasilEvaluasi.append("ANALISIS DAN KESIMPULAN\n");
                String analisis = generateAnalisis(evaluasiPanel);
                hasilEvaluasi.append(analisis);

                hasilPanel.setHasilEvaluasi(hasilEvaluasi.toString());
                cardLayout.show(cardPanel, "Hasil");
            }
        });

        // Action untuk tombol kembali dari hasil
        hasilPanel.kembaliButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "Evaluasi");
        });

        // Action untuk tombol selesai
        hasilPanel.selesaiButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menutup form evaluasi?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        // Action untuk tombol cetak
        hasilPanel.cetakButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Fitur cetak akan segera tersedia.\nHasil evaluasi dapat di-copy dari area teks.",
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
        });

        add(cardPanel);
        setVisible(true);
    }

    private boolean validasiInput(EvaluasiSesiKemoPanel panel) {
        // Validasi nama pasien
        if (panel.namaPasien.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pasien harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            panel.namaPasien.requestFocus();
            return false;
        }
        
        // Validasi tanggal sesi
        if (panel.tanggalSesi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tanggal sesi harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            panel.tanggalSesi.requestFocus();
            return false;
        }
        
        if (!isValidDate(panel.tanggalSesi.getText().trim())) {
            JOptionPane.showMessageDialog(this, 
                "Format tanggal tidak valid!\n" +
                "Gunakan format: dd-MM-yyyy\n" +
                "Contoh: 15-06-2025", 
                "Error", JOptionPane.ERROR_MESSAGE);
            panel.tanggalSesi.requestFocus();
            return false;
        }
        
        if (!isDateNotFuture(panel.tanggalSesi.getText().trim())) {
            JOptionPane.showMessageDialog(this, 
                "Tanggal sesi tidak boleh di masa depan!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            panel.tanggalSesi.requestFocus();
            return false;
        }
        
        if (!isDateNotTooOld(panel.tanggalSesi.getText().trim())) {
            JOptionPane.showMessageDialog(this, 
                "Tanggal sesi tidak boleh lebih dari 1 tahun yang lalu!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            panel.tanggalSesi.requestFocus();
            return false;
        }
        
        // Validasi sesi ke
        if (panel.sesiKe.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sesi ke harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            panel.sesiKe.requestFocus();
            return false;
        }
        
        // Validasi durasi terapi
        if (panel.durasiTerapi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Durasi terapi harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            panel.durasiTerapi.requestFocus();
            return false;
        }
        
        // Validasi tekanan darah
        if (panel.tekananDarahSesi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tekanan darah harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            panel.tekananDarahSesi.requestFocus();
            return false;
        }
        
        // Validasi suhu tubuh
        if (panel.suhuTubuhSesi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Suhu tubuh harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            panel.suhuTubuhSesi.requestFocus();
            return false;
        }
        
        // Validasi denyut nadi
        if (panel.nadiSesi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Denyut nadi harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            panel.nadiSesi.requestFocus();
            return false;
        }
        
        // Validasi saturasi oksigen
        if (panel.saturasiOksigen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Saturasi oksigen harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            panel.saturasiOksigen.requestFocus();
            return false;
        }
        
        // Validasi efek samping lain
        if (panel.efekSampingLain.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Efek samping lain harus diisi!\n(Jika tidak ada, tulis 'Tidak ada')", "Error", JOptionPane.ERROR_MESSAGE);
            panel.efekSampingLain.requestFocus();
            return false;
        }
        
        // Validasi komplikasi
        if (panel.komplikasi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Komplikasi harus diisi!\n(Jika tidak ada, tulis 'Tidak ada')", "Error", JOptionPane.ERROR_MESSAGE);
            panel.komplikasi.requestFocus();
            return false;
        }
        
        // Validasi catatan khusus
        if (panel.catatanKhusus.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Catatan khusus harus diisi!\n(Jika tidak ada, tulis 'Tidak ada')", "Error", JOptionPane.ERROR_MESSAGE);
            panel.catatanKhusus.requestFocus();
            return false;
        }
        
        // Validasi jadwal sesi berikutnya
        if (panel.sesiBerikutnya.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jadwal sesi berikutnya harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            panel.sesiBerikutnya.requestFocus();
            return false;
        }
        
        // Validasi obat pendukung
        if (panel.obatPendukung.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Obat pendukung harus diisi!\n(Jika tidak ada, tulis 'Tidak ada')", "Error", JOptionPane.ERROR_MESSAGE);
            panel.obatPendukung.requestFocus();
            return false;
        }
        
        // Validasi perawatan khusus
        if (panel.perawatanKhusus.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Perawatan khusus harus diisi!\n(Jika tidak ada, tulis 'Tidak ada')", "Error", JOptionPane.ERROR_MESSAGE);
            panel.perawatanKhusus.requestFocus();
            return false;
        }
        
        // Validasi petugas evaluator
        if (panel.petugasEvaluator.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Petugas evaluator harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            panel.petugasEvaluator.requestFocus();
            return false;
        }
        
        // Validasi numerik untuk beberapa field
        if (!isValidNumeric(panel.sesiKe.getText().trim(), "Sesi ke")) {
            panel.sesiKe.requestFocus();
            return false;
        }
        
        if (!isValidNumeric(panel.durasiTerapi.getText().trim(), "Durasi terapi")) {
            panel.durasiTerapi.requestFocus();
            return false;
        }
        
        if (!isValidNumeric(panel.suhuTubuhSesi.getText().trim(), "Suhu tubuh")) {
            panel.suhuTubuhSesi.requestFocus();
            return false;
        }
        
        if (!isValidNumeric(panel.nadiSesi.getText().trim(), "Denyut nadi")) {
            panel.nadiSesi.requestFocus();
            return false;
        }
        
        if (!isValidNumeric(panel.saturasiOksigen.getText().trim(), "Saturasi oksigen")) {
            panel.saturasiOksigen.requestFocus();
            return false;
        }
        
        return true;
    }
    
    // Method untuk validasi numerik
    private boolean isValidNumeric(String value, String fieldName) {
        try {
            double num = Double.parseDouble(value);
            if (num <= 0) {
                JOptionPane.showMessageDialog(this, 
                    fieldName + " harus berupa angka positif!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                fieldName + " harus berupa angka!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Method untuk validasi format tanggal
    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false); // Strict parsing
        
        try {
            Date date = sdf.parse(dateStr);
            // Double check: parse kembali untuk memastikan format benar
            String reformattedDate = sdf.format(date);
            return dateStr.equals(reformattedDate);
        } catch (ParseException e) {
            return false;
        }
    }
    
    // Method untuk memastikan tanggal tidak di masa depan
    private boolean isDateNotFuture(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        
        try {
            Date inputDate = sdf.parse(dateStr);
            Date today = new Date();
            return !inputDate.after(today);
        } catch (ParseException e) {
            return false;
        }
    }
    
    // Method untuk memastikan tanggal tidak terlalu lama (maksimal 1 tahun lalu)
    private boolean isDateNotTooOld(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        
        try {
            Date inputDate = sdf.parse(dateStr);
            
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1); // 1 tahun lalu
            Date oneYearAgo = cal.getTime();
            
            return !inputDate.before(oneYearAgo);
        } catch (ParseException e) {
            return false;
        }
    }

    private String generateAnalisis(EvaluasiSesiKemoPanel panel) {
        StringBuilder analisis = new StringBuilder();
        
        String toleransi = (String) panel.toleransiPasien.getSelectedItem();
        String mual = (String) panel.mualMuntah.getSelectedItem();
        String kelelahan = (String) panel.kelelahan.getSelectedItem();
        String demam = (String) panel.demam.getSelectedItem();
        
        analisis.append("Berdasarkan evaluasi sesi kemoterapi:\n\n");
        
        if ("Baik".equals(toleransi)) {
            analisis.append("✓ Pasien menunjukkan toleransi yang baik terhadap terapi kemoterapi.\n");
        } else if ("Sedang".equals(toleransi)) {
            analisis.append("⚠ Pasien menunjukkan toleransi sedang, perlu monitoring ketat.\n");
        } else {
            analisis.append("⚠ Pasien menunjukkan toleransi buruk, pertimbangkan penyesuaian terapi.\n");
        }
        
        if ("Tidak Ada".equals(mual) && "Tidak Ada".equals(kelelahan) && "Tidak Ada".equals(demam)) {
            analisis.append("✓ Tidak ada efek samping signifikan yang dilaporkan.\n");
        } else {
            analisis.append("⚠ Terdapat efek samping yang perlu diperhatikan dan ditangani.\n");
        }
        
        String rekomendasi = (String) panel.perubahanDosis.getSelectedItem();
        if ("Tidak Perlu".equals(rekomendasi)) {
            analisis.append("✓ Dosis saat ini dapat dilanjutkan sesuai protokol.\n");
        } else {
            analisis.append("⚠ Diperlukan penyesuaian dosis untuk sesi berikutnya.\n");
        }
        
        analisis.append("\nRekomendasi: Lanjutkan monitoring rutin dan evaluasi berkala untuk memastikan keamanan dan efektivitas terapi.");
        
        return analisis.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EvaluasiSesiKemo());
    }
}

// Panel untuk form evaluasi sesi kemoterapi
class EvaluasiSesiKemoPanel extends JPanel {
    JTextField namaPasien, tanggalSesi, sesiKe, durasiTerapi;
    JTextField tekananDarahSesi, suhuTubuhSesi, nadiSesi, saturasiOksigen;
    JComboBox<String> mualMuntah, kelelahan, demam, diare, nafsuMakan, nyeri;
    JTextArea efekSampingLain;
    JComboBox<String> toleransiPasien, kondisiPsikologis, perubahanDosis;
    JTextArea komplikasi, catatanKhusus, sesiBerikutnya, obatPendukung, perawatanKhusus;
    JTextField petugasEvaluator;
    JButton submitButton;

    public EvaluasiSesiKemoPanel() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Title
        JLabel titleLabel = new JLabel("EVALUASI HASIL SESI KEMOTERAPI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Tambahan label untuk menginformasikan bahwa semua field wajib diisi
        JLabel infoLabel = new JLabel("<html><i>* Semua field wajib diisi</i></html>");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        infoLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        mainPanel.add(infoLabel, gbc);

        // Reset gridwidth
        gbc.gridwidth = 1;
        int row = 2;

        // Informasi Sesi
        addSectionHeader(mainPanel, gbc, "INFORMASI SESI", row++);
        
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Nama Pasien: *"), gbc);
        gbc.gridx = 1;
        namaPasien = new JTextField(20);
        mainPanel.add(namaPasien, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Tanggal Sesi: *"), gbc);
        gbc.gridx = 1;
        
        // Panel untuk tanggal dengan format bantuan
        JPanel tanggalPanel = new JPanel(new BorderLayout());
        tanggalSesi = new JTextField(20);
        tanggalSesi.setToolTipText("Format: dd-MM-yyyy (Contoh: 15-06-2025)");
        
        // Label bantuan format
        JLabel formatLabel = new JLabel("<html><font size='-2' color='blue'>Format: dd-MM-yyyy</font></html>");
        
        tanggalPanel.add(tanggalSesi, BorderLayout.CENTER);
        tanggalPanel.add(formatLabel, BorderLayout.SOUTH);
        
        mainPanel.add(tanggalPanel, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Sesi Ke: *"), gbc);
        gbc.gridx = 1;
        sesiKe = new JTextField(20);
        mainPanel.add(sesiKe, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Durasi Terapi (jam): *"), gbc);
        gbc.gridx = 1;
        durasiTerapi = new JTextField(20);
        mainPanel.add(durasiTerapi, gbc);
        row++;

        // Kondisi Fisik
        addSectionHeader(mainPanel, gbc, "KONDISI FISIK SELAMA TERAPI", row++);

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Tekanan Darah: *"), gbc);
        gbc.gridx = 1;
        tekananDarahSesi = new JTextField(20);
        tekananDarahSesi.setToolTipText("Contoh: 120/80");
        mainPanel.add(tekananDarahSesi, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Suhu Tubuh (°C): *"), gbc);
        gbc.gridx = 1;
        suhuTubuhSesi = new JTextField(20);
        mainPanel.add(suhuTubuhSesi, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Denyut Nadi (bpm): *"), gbc);
        gbc.gridx = 1;
        nadiSesi = new JTextField(20);
        mainPanel.add(nadiSesi, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Saturasi Oksigen (%): *"), gbc);
        gbc.gridx = 1;
        saturasiOksigen = new JTextField(20);
        mainPanel.add(saturasiOksigen, gbc);
        row++;

        // Efek Samping
        addSectionHeader(mainPanel, gbc, "EFEK SAMPING YANG DIALAMI", row++);

        String[] severityOptions = {"Tidak Ada", "Ringan", "Sedang", "Berat"};

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Mual/Muntah:"), gbc);
        gbc.gridx = 1;
        mualMuntah = new JComboBox<>(severityOptions);
        mainPanel.add(mualMuntah, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Kelelahan:"), gbc);
        gbc.gridx = 1;
        kelelahan = new JComboBox<>(severityOptions);
        mainPanel.add(kelelahan, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Demam:"), gbc);
        gbc.gridx = 1;
        demam = new JComboBox<>(severityOptions);
        mainPanel.add(demam, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Diare:"), gbc);
        gbc.gridx = 1;
        diare = new JComboBox<>(severityOptions);
        mainPanel.add(diare, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Gangguan Nafsu Makan:"), gbc);
        gbc.gridx = 1;
        nafsuMakan = new JComboBox<>(severityOptions);
        mainPanel.add(nafsuMakan, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Nyeri/Sakit:"), gbc);
        gbc.gridx = 1;
        nyeri = new JComboBox<>(severityOptions);
        mainPanel.add(nyeri, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Efek Samping Lain: *"), gbc);
        gbc.gridx = 1;
        efekSampingLain = new JTextArea(2, 20);
        efekSampingLain.setToolTipText("Jika tidak ada, tulis 'Tidak ada'");
        mainPanel.add(new JScrollPane(efekSampingLain), gbc);
        row++;

        // Respon Terhadap Terapi
        addSectionHeader(mainPanel, gbc, "RESPON TERHADAP TERAPI", row++);

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Toleransi Pasien:"), gbc);
        gbc.gridx = 1;
        toleransiPasien = new JComboBox<>(new String[]{"Baik", "Sedang", "Buruk"});
        mainPanel.add(toleransiPasien, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Kondisi Psikologis:"), gbc);
        gbc.gridx = 1;
        kondisiPsikologis = new JComboBox<>(new String[]{"Stabil", "Cemas", "Depresi", "Optimis"});
        mainPanel.add(kondisiPsikologis, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Komplikasi: *"), gbc);
        gbc.gridx = 1;
        komplikasi = new JTextArea(2, 20);
        komplikasi.setToolTipText("Jika tidak ada, tulis 'Tidak ada'");
        mainPanel.add(new JScrollPane(komplikasi), gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Catatan Khusus: *"), gbc);
        gbc.gridx = 1;
        catatanKhusus = new JTextArea(2, 20);
        catatanKhusus.setToolTipText("Jika tidak ada, tulis 'Tidak ada'");
        mainPanel.add(new JScrollPane(catatanKhusus), gbc);
        row++;

        // Rekomendasi
        addSectionHeader(mainPanel, gbc, "REKOMENDASI TINDAK LANJUT", row++);

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Perubahan Dosis:"), gbc);
        gbc.gridx = 1;
        perubahanDosis = new JComboBox<>(new String[]{"Tidak Perlu", "Kurangi Dosis", "Naikkan Dosis", "Tunda Sesi"});
        mainPanel.add(perubahanDosis, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Jadwal Sesi Berikutnya: *"), gbc);
        gbc.gridx = 1;
        sesiBerikutnya = new JTextArea(2, 20);
        sesiBerikutnya.setToolTipText("Contoh: Senin, 14 Juni 2025");
        mainPanel.add(new JScrollPane(sesiBerikutnya), gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Obat Pendukung: *"), gbc);
        gbc.gridx = 1;
        obatPendukung = new JTextArea(2, 20);
        obatPendukung.setToolTipText("Jika tidak ada, tulis 'Tidak ada'");
        mainPanel.add(new JScrollPane(obatPendukung), gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Perawatan Khusus: *"), gbc);
        gbc.gridx = 1;
        perawatanKhusus = new JTextArea(2, 20);
        perawatanKhusus.setToolTipText("Jika tidak ada, tulis 'Tidak ada'");
        mainPanel.add(new JScrollPane(perawatanKhusus), gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Petugas Evaluator: *"), gbc);
        gbc.gridx = 1;
        petugasEvaluator = new JTextField(20);
        mainPanel.add(petugasEvaluator, gbc);
        row++;

        // Submit Button
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        submitButton = new JButton("Submit Evaluasi");
        submitButton.setFont(new Font("Arial", Font.BOLD, 12));
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.WHITE);
        mainPanel.add(submitButton, gbc);

        // Scroll pane untuk main panel
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addSectionHeader(JPanel panel, GridBagConstraints gbc, String title, int row) {
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        sectionLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tambah separator line
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.add(sectionLabel, BorderLayout.WEST);
        sectionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        panel.add(sectionPanel, gbc);
        
        // Reset untuk field berikutnya
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
    }
}

// Panel untuk menampilkan hasil evaluasi
class HasilEvaluasiPanel extends JPanel {
    private JTextArea hasilTextArea;
    JButton kembaliButton, selesaiButton, cetakButton;

    public HasilEvaluasiPanel() {
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("HASIL EVALUASI SESI KEMOTERAPI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Text area untuk hasil
        hasilTextArea = new JTextArea(25, 50);
        hasilTextArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        hasilTextArea.setEditable(false);
        hasilTextArea.setBackground(Color.WHITE);
        hasilTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(hasilTextArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Hasil Evaluasi"));
        add(scrollPane, BorderLayout.CENTER);

        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        kembaliButton = new JButton("Kembali ke Form");
        kembaliButton.setBackground(new Color(108, 117, 125));
        kembaliButton.setForeground(Color.WHITE);
        
        cetakButton = new JButton("Cetak/Print");
        cetakButton.setBackground(new Color(40, 167, 69));
        cetakButton.setForeground(Color.WHITE);
        
        selesaiButton = new JButton("Selesai");
        selesaiButton.setBackground(new Color(220, 53, 69));
        selesaiButton.setForeground(Color.WHITE);

        buttonPanel.add(kembaliButton);
        buttonPanel.add(cetakButton);
        buttonPanel.add(selesaiButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setHasilEvaluasi(String hasil) {
        hasilTextArea.setText(hasil);
        hasilTextArea.setCaretPosition(0); // Scroll ke atas
    }
}
