package PBO_4C_SI_KELOMPOK_7.view; // Use the correct package name

import PBO_4C_SI_KELOMPOK_7.db.DBConnection; // Import your DBConnection class
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;

public class EvaluasiTambah extends JFrame {
    private EvaluasiSesiKemoPanel evaluasiPanel;
    
    public EvaluasiTambah() {
        setTitle("Form Evaluasi Sesi Kemoterapi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setSize(600, 700);
        setLocationRelativeTo(null);

        evaluasiPanel = new EvaluasiSesiKemoPanel();
        add(evaluasiPanel);

        evaluasiPanel.submitButton.addActionListener(e -> {
            if (validasiInput(evaluasiPanel)) {
                saveEvaluasiToDatabase(evaluasiPanel);
            }
        });

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

    private void saveEvaluasiToDatabase(EvaluasiSesiKemoPanel panel) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.connect();
            if (conn != null) {
                // First, try to find a matching jadwal_terapi. This is a simplified approach.
                // In a real application, you'd likely select the jadwal_id based on more precise criteria
                // like pasien_id, tanggal_terapi, sesi_ke.
                String findJadwalSql = "SELECT jadwal_id FROM jadwal_terapi WHERE sesi_ke = ? AND DATE_FORMAT(tanggal_terapi, '%d-%m-%Y') = ?";
                pstmt = conn.prepareStatement(findJadwalSql);
                pstmt.setInt(1, Integer.parseInt(panel.sesiKe.getText()));
                pstmt.setString(2, panel.tanggalSesi.getText());
                rs = pstmt.executeQuery();

                int jadwalId = -1;
                if (rs.next()) {
                    jadwalId = rs.getInt("jadwal_id");
                } else {
                    JOptionPane.showMessageDialog(this, "Jadwal terapi tidak ditemukan. Pastikan 'Sesi ke' dan 'Tanggal Sesi' benar.", "Error Database", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Prepare the SQL insert statement for evaluasi_kemo
                String sql = "INSERT INTO evaluasi_kemo (jadwal_id, kondisi_post_terapi, efek_samping, catatan, tanggal_evaluasi) VALUES (?, ?, ?, ?, NOW())";
                pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                // Assuming 'kondisi_post_terapi' will combine several physical conditions for simplicity
                String kondisiPostTerapi = "Tekanan Darah: " + panel.tekananDarahSesi.getText() + " mmHg, " +
                                           "Suhu Tubuh: " + panel.suhuTubuhSesi.getText() + " °C, " +
                                           "Denyut Nadi: " + panel.nadiSesi.getText() + " bpm, " +
                                           "Saturasi Oksigen: " + panel.saturasiOksigen.getText() + " %";

                // Assuming 'efek_samping' will combine all side effects
                String efekSamping = "Mual/Muntah: " + panel.mualMuntah.getSelectedItem() + ", " +
                                     "Kelelahan: " + panel.kelelahan.getSelectedItem() + ", " +
                                     "Demam: " + panel.demam.getSelectedItem() + ", " +
                                     "Diare: " + panel.diare.getSelectedItem() + ", " +
                                     "Gangguan Nafsu Makan: " + panel.nafsuMakan.getSelectedItem() + ", " +
                                     "Nyeri/Sakit: " + panel.nyeri.getSelectedItem() + ", " +
                                     "Lain-lain: " + panel.efekSampingLain.getText();
                
                // Assuming 'catatan' will combine complications and special notes
                String catatan = "Komplikasi: " + panel.komplikasi.getText() + "\n" +
                                 "Catatan Khusus: " + panel.catatanKhusus.getText() + "\n" +
                                 "Toleransi Pasien: " + panel.toleransiPasien.getSelectedItem() + "\n" +
                                 "Kondisi Psikologis: " + panel.kondisiPsikologis.getSelectedItem() + "\n" +
                                 "Perubahan Dosis: " + panel.perubahanDosis.getSelectedItem() + "\n" +
                                 "Jadwal Sesi Berikutnya: " + panel.sesiBerikutnya.getText() + "\n" +
                                 "Obat Pendukung: " + panel.obatPendukung.getText() + "\n" +
                                 "Perawatan Khusus: " + panel.perawatanKhusus.getText() + "\n" +
                                 "Petugas Evaluator: " + panel.petugasEvaluator.getText();

                pstmt.setInt(1, jadwalId);
                pstmt.setString(2, kondisiPostTerapi);
                pstmt.setString(3, efekSamping);
                pstmt.setString(4, catatan);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data evaluasi berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    // Optionally, clear the form or open the view window
                    dispose(); // Close the input form
                    new EvaluasiView(); // Open the view to show all evaluations
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan data evaluasi.", "Error Database", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Input numerik tidak valid: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EvaluasiTambah());
    }
}

// EvaluasiSesiKemoPanel remains the same as in your original code
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
