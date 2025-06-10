// File: PBO_4C_SI_KELOMPOK_7/view/PasienDetail.java
package PBO_4C_SI_KELOMPOK_7.view;

import PBO_4C_SI_KELOMPOK_7.controller.PasienController;
import PBO_4C_SI_KELOMPOK_7.model.Pasien;
import PBO_4C_SI_KELOMPOK_7.model.DokterJadwal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PasienDetail extends BaseFrame {

    private JTextPane detailTextPane;
    private JTable scheduleTable;
    private DefaultTableModel scheduleTableModel;
    private int pasienId;
    private String username;

    public PasienDetail(int pasienId, String username) {
        super("Detail Pasien", username);
        this.pasienId = pasienId;
        this.username = username;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);

        Pasien pasien = PasienController.getPasienById(pasienId);
        if (pasien == null) {
            JOptionPane.showMessageDialog(this, "Data pasien tidak ditemukan.");
            dispose();
            return;
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel for Patient Details (HTML formatted)
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        
        detailTextPane = new JTextPane();
        detailTextPane.setEditable(false);
        detailTextPane.setContentType("text/html");
        detailTextPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        detailTextPane.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        GridBagConstraints gbcTextPane = new GridBagConstraints();
        gbcTextPane.insets = new Insets(15, 15, 15, 15);
        gbcTextPane.fill = GridBagConstraints.BOTH;
        gbcTextPane.weightx = 1.0;
        gbcTextPane.weighty = 1.0;
        detailPanel.add(detailTextPane, gbcTextPane);

        JScrollPane detailScrollPane = new JScrollPane(detailPanel);
        detailScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        detailScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        detailScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        mainPanel.add(detailScrollPane, BorderLayout.NORTH);


        // Schedule Table Panel
        JPanel schedulePanel = new JPanel(new BorderLayout());
        schedulePanel.setBorder(BorderFactory.createTitledBorder("Jadwal Kemoterapi Direkomendasikan"));

        scheduleTableModel = new DefaultTableModel(new Object[]{"Sesi Ke", "Tanggal", "Hari", "Jam Mulai", "Jam Selesai"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scheduleTable = new JTable(scheduleTableModel);
        scheduleTable.setFillsViewportHeight(true);
        JScrollPane scheduleScrollPane = new JScrollPane(scheduleTable);
        schedulePanel.add(scheduleScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(schedulePanel, BorderLayout.CENTER);

        // Populate text area with formatted data
        populateDetail(pasien);

        // Generate and display schedule
        List<DokterJadwal> dokterSchedules = PasienController.getDokterSchedules(pasien.getDokterId());
        generateChemoSchedule(pasien, dokterSchedules);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton btnEvaluasi = new JButton("Evaluasi Sesi");
        btnEvaluasi.addActionListener(e -> {
            // Open EvaluasiTambah, passing patient ID and name
            new EvaluasiTambah(pasien.getId(), pasien.getNama()).setVisible(true); // Pass patient ID and name
            // No need to dispose PasienDetail immediately, allow user to return
            // dispose(); // Uncomment if you want to close this window
        });

        JButton btnHapus = new JButton("Hapus Pasien");
        btnHapus.setBackground(new Color(220, 53, 69));
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
                    new PasienList(username).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data pasien.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> dispose());

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

        sb.append("<html><body style='font-family: SansSerif; font-size: 10pt; margin: 0px; background-color: white;'>");
        sb.append("<h3 style='text-align: center; color: #333; margin-bottom: 5px;'>INFORMASI LENGKAP PASIEN</h3>");
        sb.append("<hr style='border: 0.5px solid #ccc; margin-bottom: 10px;'><br>");

        sb.append("<p style='font-weight: bold; margin-bottom: 3px;'>DATA PRIBADI:</p>");
        sb.append("<table border='0' cellspacing='0' cellpadding='2' style='width:100%;'>");
        sb.append(String.format("<tr><td width='120'>ID Pasien</td><td>: %d</td></tr>", p.getId()));
        sb.append(String.format("<tr><td>Nama Lengkap</td><td>: %s</td></tr>", p.getNama()));
        sb.append(String.format("<tr><td>Alamat</td><td>: %s</td></tr>", p.getAlamat()));
        sb.append(String.format("<tr><td>No. Telepon</td><td>: %s</td></tr>", p.getTelepon()));
        sb.append(String.format("<tr><td>Tanggal Lahir</td><td>: %s</td></tr>", p.getTanggalLahir() != null ? p.getTanggalLahir().format(dateFormatter) : "-"));
        sb.append(String.format("<tr><td>Jenis Kelamin</td><td>: %s</td></tr>", p.getKelamin()));
        sb.append(String.format("<tr><td>Dokter PJ</td><td>: %s (ID: %d)</td></tr>", p.getDokterNama(), p.getDokterId()));
        sb.append("</table><br>");

        sb.append("<p style='font-weight: bold; margin-bottom: 3px;'>DIAGNOSA:</p>");
        sb.append("<table border='0' cellspacing='0' cellpadding='2' style='width:100%;'>");
        sb.append(String.format("<tr><td width='120'>Diagnosa Utama</td><td>: %s</td></tr>", p.getDiagnosa() != null ? p.getDiagnosa() : "-"));
        sb.append(String.format("<tr><td>Histopatologi</td><td>: %s</td></tr>", p.getHistopatologi() != null ? p.getHistopatologi() : "-"));
        sb.append("</table><br>");

        sb.append("<p style='font-weight: bold; margin-bottom: 3px;'>PEMERIKSAAN FISIK & PENUNJANG:</p>");
        sb.append("<table border='0' cellspacing='0' cellpadding='2' style='width:100%;'>");
        sb.append(String.format("<tr><td width='120'>Tekanan Darah</td><td>: %s mmHg</td></tr>", p.getTekananDarah() != null ? p.getTekananDarah() : "-"));
        sb.append(String.format("<tr><td>Suhu Tubuh</td><td>: %s °C</td></tr>", p.getSuhuTubuh() != null ? p.getSuhuTubuh() : "-"));
        sb.append(String.format("<tr><td>Denyut Nadi</td><td>: %s bpm</td></tr>", p.getDenyutNadi() != null ? p.getDenyutNadi() : "-"));
        sb.append(String.format("<tr><td>Berat Badan</td><td>: %s kg</td></tr>", p.getBeratBadan() != null ? p.getBeratBadan() : "-"));
        sb.append(String.format("<tr><td>Hemoglobin (HB)</td><td>: %s g/dL</td></tr>", p.getHb() != null ? p.getHb() : "-"));
        sb.append(String.format("<tr><td>Leukosit</td><td>: %s /µL</td></tr>", p.getLeukosit() != null ? p.getLeukosit() : "-"));
        sb.append(String.format("<tr><td>Trombosit</td><td>: %s /µL</td></tr>", p.getTrombosit() != null ? p.getTrombosit() : "-"));
        sb.append(String.format("<tr><td>Fungsi Hati</td><td>: %s</td></tr>", p.getFungsiHati() != null ? p.getFungsiHati() : "-"));
        sb.append(String.format("<tr><td>Fungsi Ginjal</td><td>: %s</td></tr>", p.getFungsiGinjal() != null ? p.getFungsiGinjal() : "-"));
        sb.append("</table><br>");

        sb.append("<p style='font-weight: bold; margin-bottom: 3px;'>RENCANA TERAPI KEMOTERAPI:</p>");
        sb.append("<table border='0' cellspacing='0' cellpadding='2' style='width:100%;'>");
        sb.append(String.format("<tr><td width='120'>Jenis Kemoterapi</td><td>: %s</td></tr>", p.getJenisKemoterapi() != null ? p.getJenisKemoterapi() : "-"));
        sb.append(String.format("<tr><td>Dosis</td><td>: %s</td></tr>", p.getDosis() != null ? p.getDosis() : "-"));
        sb.append(String.format("<tr><td>Siklus</td><td>: %s</td></tr>", p.getSiklus() != null ? p.getSiklus() : "-"));
        sb.append(String.format("<tr><td>Premedikasi</td><td>: %s</td></tr>", p.getPremedikasi() != null ? p.getPremedikasi() : "-"));
        sb.append(String.format("<tr><td>Akses Vena</td><td>: %s</td></tr>", p.getAksesVena() != null ? p.getAksesVena() : "-"));
        sb.append("</table><br>");
        
        sb.append("</body></html>");

        detailTextPane.setText(sb.toString());
        detailTextPane.setCaretPosition(0);
    }

    private void generateChemoSchedule(Pasien pasien, List<DokterJadwal> dokterSchedules) {
        scheduleTableModel.setRowCount(0); // Clear previous schedule

        // Group doctor schedules by DayOfWeek for easier lookup
        Map<DayOfWeek, List<DokterJadwal>> schedulesByDay = dokterSchedules.stream()
                .collect(Collectors.groupingBy(DokterJadwal::getHari));

        // Get the initial date for the first session from the database (tanggal_dibuat from rencana_terapi)
        LocalDate startDate = pasien.getTanggalDibuatRencanaTerapi();
        if (startDate == null) {
            JOptionPane.showMessageDialog(this, "Tanggal pembuatan rencana terapi tidak ditemukan. Tidak dapat membuat jadwal.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int cycle;
        try {
            // Assuming siklus in Pasien model is the interval in weeks
            cycle = Integer.parseInt(pasien.getSiklus());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format siklus terapi tidak valid. Tidak dapat membuat jadwal.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (cycle <= 0) {
            JOptionPane.showMessageDialog(this, "Siklus terapi harus lebih dari 0. Tidak dapat membuat jadwal.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        LocalDate currentScheduleDate = startDate;
        int sesiKe = 1;
        final int MAX_SESSIONS = 10; // Generate up to 10 future sessions

        DateTimeFormatter displayDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter displayDayFormatter = DateTimeFormatter.ofPattern("EEEE"); // For Indonesian day names

        while (sesiKe <= MAX_SESSIONS) {
            DayOfWeek dayOfWeek = currentScheduleDate.getDayOfWeek();
            List<DokterJadwal> availableSlots = schedulesByDay.get(dayOfWeek);

            if (availableSlots != null && !availableSlots.isEmpty()) {
                // Assuming the first available slot for that day is taken
                DokterJadwal slot = availableSlots.get(0); // Take the first available slot for the day
                
                scheduleTableModel.addRow(new Object[]{
                    sesiKe,
                    currentScheduleDate.format(displayDateFormatter),
                    currentScheduleDate.format(displayDayFormatter),
                    slot.getJamMulai().format(DateTimeFormatter.ofPattern("HH:mm")),
                    slot.getJamSelesai().format(DateTimeFormatter.ofPattern("HH:mm"))
                });
                sesiKe++;
            } else {
                // If no schedule for this day, try the next date in the cycle
                // For simplicity, we just move to the next cycle date even if no slot found for current day
            }

            // Move to the next cycle date (e.g., +2 weeks for a siklus 2)
            currentScheduleDate = currentScheduleDate.plusWeeks(cycle);
        }
    }
}