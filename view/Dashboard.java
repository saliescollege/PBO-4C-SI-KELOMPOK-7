package PBO_4C_SI_KELOMPOK_7.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import PBO_4C_SI_KELOMPOK_7.db.DBConnection;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dashboard extends BaseFrame { // Mengubah dari JFrame menjadi BaseFrame

    public Dashboard(String username) {
        super("Dashboard", username); // Memanggil konstruktor BaseFrame dengan judul dan username

        // Panel kiri untuk tombol Pasien dan Dokter
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(2, 1, 10, 10)); // Grid 2 baris, 1 kolom, dengan gap 10px
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Border padding

        // Tombol Pasien
        ImageIcon pasienIcon = new ImageIcon("src\\PBO_4C_SI_KELOMPOK_7\\assets\\Pasien.png"); // Mengambil gambar pasien
        Image scaledPasienImg = pasienIcon.getImage().getScaledInstance(480, 270, Image.SCALE_SMOOTH); // Skala gambar
        JButton pasienBtn = new JButton("Pasien", new ImageIcon(scaledPasienImg));
        pasienBtn.setHorizontalTextPosition(SwingConstants.CENTER); // Teks di tengah horizontal
        pasienBtn.setVerticalTextPosition(SwingConstants.BOTTOM); // Teks di bawah ikon
        pasienBtn.setFont(new Font("Arial", Font.BOLD, 16)); // Font tombol
        pasienBtn.setPreferredSize(new Dimension(250, 250)); // Ukuran preferensi tombol (opsional, layout akan mengatur)
        pasienBtn.addActionListener(e -> {
            new PasienList(username).setVisible(true); // Buka halaman daftar pasien
            dispose(); // Tutup dashboard
        });

        // Tombol Dokter
        ImageIcon dokterIcon = new ImageIcon("src\\PBO_4C_SI_KELOMPOK_7\\assets\\Dokter.png"); // Mengambil gambar dokter
        Image scaledDokterImg = dokterIcon.getImage().getScaledInstance(480, 270, Image.SCALE_SMOOTH); // Skala gambar
        JButton dokterBtn = new JButton("Dokter", new ImageIcon(scaledDokterImg));
        dokterBtn.setHorizontalTextPosition(SwingConstants.CENTER); // Teks di tengah horizontal
        dokterBtn.setVerticalTextPosition(SwingConstants.BOTTOM); // Teks di bawah ikon
        dokterBtn.setFont(new Font("Arial", Font.BOLD, 16)); // Font tombol
        dokterBtn.setPreferredSize(new Dimension(250, 250)); // Ukuran preferensi tombol (opsional)
        dokterBtn.addActionListener(e -> {
            // new DokterList(username).setVisible(true); // Jika ada DokterList yang ingin dibuka
            JOptionPane.showMessageDialog(this, "Fitur Dokter akan segera ditambahkan.");
        });

        leftPanel.add(pasienBtn); // Menambahkan tombol pasien ke panel kiri
        leftPanel.add(dokterBtn); // Menambahkan tombol dokter ke panel kiri

        // Panel kanan untuk tabel jadwal
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Border padding

        // Judul jadwal hari ini
        String tanggalHariIni = new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date()); // Format tanggal
        JLabel judulJadwal = new JLabel("Jadwal: " + tanggalHariIni, SwingConstants.CENTER); // Judul dengan tanggal
        judulJadwal.setFont(new Font("Arial", Font.BOLD, 18)); // Font judul
        judulJadwal.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Padding bawah judul

        // Model tabel untuk jadwal
        String[] kolom = {"Pasien", "Waktu", "Dokter"}; // Kolom tabel
        DefaultTableModel modelJadwal = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat sel tabel tidak bisa diedit
            }
        };
        JTable tabelJadwal = new JTable(modelJadwal); // Membuat tabel dengan model
        JScrollPane scrollPane = new JScrollPane(tabelJadwal); // Menambahkan scroll pane ke tabel

        rightPanel.add(judulJadwal, BorderLayout.NORTH); // Menambahkan judul ke panel kanan (atas)
        rightPanel.add(scrollPane, BorderLayout.CENTER); // Menambahkan tabel ke panel kanan (tengah)

        // Panel utama yang berisi panel kiri dan kanan
        JPanel mainContentPanel = new JPanel(new GridLayout(1, 2)); // Grid 1 baris, 2 kolom
        mainContentPanel.add(leftPanel); // Menambahkan panel kiri
        mainContentPanel.add(rightPanel); // Menambahkan panel kanan

        add(mainContentPanel, BorderLayout.CENTER); // Menambahkan panel konten utama ke frame

        loadJadwalHariIni(modelJadwal); // Memuat data jadwal

        setVisible(true); // Menampilkan frame
    }

    // Method untuk memuat jadwal hari ini dari database
    private void loadJadwalHariIni(DefaultTableModel model) {
        // Mengosongkan tabel sebelum memuat data baru
        model.setRowCount(0);

        try (Connection conn = DBConnection.connect()) { // Mendapatkan koneksi database
            String sql = "SELECT p.nama_lengkap, " +
                         "       CONCAT(jt.jam_terapi, ' - ', jd.jam_selesai) AS waktu, " +
                         "       CONCAT(d.nama, ', ', d.spesialisasi) AS dokter " +
                         "FROM jadwal_terapi jt " +
                         "JOIN jadwal_dokter jd ON jt.jadwal_dokter_id = jd.jadwal_id " +
                         "JOIN rencana_terapi rt ON jt.terapi_id = rt.terapi_id " +
                         "JOIN pasien p ON rt.pasien_id = p.pasien_id " +
                         "JOIN dokter d ON rt.dokter_id = d.dokter_id " +
                         "WHERE jt.tanggal_terapi = CURDATE()"; // Query untuk jadwal hari ini

            PreparedStatement stmt = conn.prepareStatement(sql); // Menyiapkan statement
            ResultSet rs = stmt.executeQuery(); // Menjalankan query

            // Memasukkan data dari ResultSet ke model tabel
            while (rs.next()) {
                String pasien = rs.getString("nama_lengkap");
                String waktu = rs.getString("waktu");
                String dokter = rs.getString("dokter");
                model.addRow(new Object[]{pasien, waktu, dokter}); // Menambahkan baris ke tabel
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat jadwal: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Main method untuk menjalankan Dashboard
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard("AdminKlinik")); // Contoh pemanggilan
    }
}