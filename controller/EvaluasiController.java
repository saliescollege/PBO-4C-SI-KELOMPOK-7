package PBO_4C_SI_KELOMPOK_7.controller;

import PBO_4C_SI_KELOMPOK_7.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvaluasiController {

    /**
     * Menambahkan data evaluasi baru untuk seorang pasien.
     * Metode ini akan otomatis mencari jadwal_id terakhir dari pasien tersebut.
     *
     * @param pasienId ID dari pasien yang dievaluasi.
     * @param kondisi Catatan kondisi pasca-terapi.
     * @param efekSamping Catatan efek samping yang muncul.
     * @param catatan Catatan tambahan lainnya.
     * @return true jika berhasil, false jika gagal.
     */
    public static boolean tambahEvaluasi(int pasienId, String kondisi, String efekSamping, String catatan) {
        int jadwalId = -1;

        // Langkah 1: Cari jadwal_id terakhir dari pasien ini yang sudah/sedang berlangsung
        String sqlFindJadwal = "SELECT jt.jadwal_id FROM jadwal_terapi jt " +
                               "JOIN rencana_terapi rt ON jt.terapi_id = rt.terapi_id " +
                               "WHERE rt.pasien_id = ? AND jt.tanggal_terapi <= CURDATE() " +
                               "ORDER BY jt.tanggal_terapi DESC, jt.jam_terapi DESC LIMIT 1";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlFindJadwal)) {
            
            pstmt.setInt(1, pasienId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                jadwalId = rs.getInt("jadwal_id");
            } else {
                // Tidak ada jadwal yang bisa dievaluasi untuk pasien ini
                System.err.println("Tidak ditemukan jadwal terapi yang bisa dievaluasi untuk pasien ID: " + pasienId);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Gagal mencari jadwal_id: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        // Langkah 2: Simpan data evaluasi dengan jadwal_id yang ditemukan
        String sqlInsert = "INSERT INTO evaluasi_kemo (jadwal_id, kondisi_post_terapi, efek_samping, catatan, tanggal_evaluasi) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {

            pstmt.setInt(1, jadwalId);
            pstmt.setString(2, kondisi);
            pstmt.setString(3, efekSamping);
            pstmt.setString(4, catatan);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Gagal menyimpan data evaluasi ke database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}