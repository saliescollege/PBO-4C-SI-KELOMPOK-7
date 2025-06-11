package PBO_4C_SI_KELOMPOK_7.controller;

import PBO_4C_SI_KELOMPOK_7.db.DBConnection;
import PBO_4C_SI_KELOMPOK_7.model.Evaluasi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    
    /**
     * Mengambil semua riwayat evaluasi untuk satu pasien spesifik.
     * @param pasienId ID dari pasien.
     * @return List dari objek Evaluasi.
     */
    public static List<Evaluasi> getEvaluasiByPasienId(int pasienId) {
        List<Evaluasi> evaluasiList = new ArrayList<>();
        String sql = "SELECT e.evaluasi_id, e.jadwal_id, e.kondisi_post_terapi, e.efek_samping, e.catatan, e.tanggal_evaluasi, " +
                     "p.nama_lengkap AS nama_pasien, d.nama AS nama_dokter, jt.sesi_ke " +
                     "FROM evaluasi_kemo e " +
                     "JOIN jadwal_terapi jt ON e.jadwal_id = jt.jadwal_id " +
                     "JOIN rencana_terapi rt ON jt.terapi_id = rt.terapi_id " +
                     "JOIN pasien p ON rt.pasien_id = p.pasien_id " +
                     "JOIN dokter d ON rt.dokter_id = d.dokter_id " +
                     "WHERE p.pasien_id = ? " +
                     "ORDER BY e.tanggal_evaluasi DESC";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pasienId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Evaluasi eval = new Evaluasi();
                eval.setEvaluasiId(rs.getInt("evaluasi_id"));
                eval.setJadwalId(rs.getInt("jadwal_id"));
                eval.setKondisiPostTerapi(rs.getString("kondisi_post_terapi"));
                eval.setEfekSamping(rs.getString("efek_samping"));
                eval.setCatatan(rs.getString("catatan"));
                eval.setTanggalEvaluasi(rs.getTimestamp("tanggal_evaluasi"));
                eval.setNamaPasien(rs.getString("nama_pasien"));
                eval.setNamaDokter(rs.getString("nama_dokter"));
                eval.setSesiKe(rs.getInt("sesi_ke"));
                evaluasiList.add(eval);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data evaluasi pasien: " + e.getMessage());
            e.printStackTrace();
        }
        return evaluasiList;
    }
}