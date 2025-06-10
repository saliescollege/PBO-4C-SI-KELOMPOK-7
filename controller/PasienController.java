// File: PBO_4C_SI_KELOMPOK_7/controller/PasienController.java
package PBO_4C_SI_KELOMPOK_7.controller;

import PBO_4C_SI_KELOMPOK_7.db.DBConnection;
import PBO_4C_SI_KELOMPOK_7.model.Pasien;
import PBO_4C_SI_KELOMPOK_7.model.DokterJadwal;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasienController {

    // Helper map for Indonesian day names to DayOfWeek enum
    private static final Map<String, DayOfWeek> DAY_NAME_MAP = new HashMap<>();
    static {
        DAY_NAME_MAP.put("SENIN", DayOfWeek.MONDAY);
        DAY_NAME_MAP.put("SELASA", DayOfWeek.TUESDAY);
        DAY_NAME_MAP.put("RABU", DayOfWeek.WEDNESDAY);
        DAY_NAME_MAP.put("KAMIS", DayOfWeek.THURSDAY);
        DAY_NAME_MAP.put("JUMAT", DayOfWeek.FRIDAY);
        DAY_NAME_MAP.put("SABTU", DayOfWeek.SATURDAY);
        DAY_NAME_MAP.put("MINGGU", DayOfWeek.SUNDAY);
    }

    // CREATE - Tambah Pasien
    public static void tambahPasien(Pasien pasien) {
        Connection conn = null;
        PreparedStatement pstmtPasien = null;
        PreparedStatement pstmtDiagnosa = null;
        PreparedStatement pstmtPeriksaFisik = null;
        PreparedStatement pstmtRencanaTerapi = null;
        PreparedStatement pstmtJadwalTerapi = null; // New PreparedStatement for jadwal_terapi
        ResultSet rsPasien = null; // Renamed for clarity
        ResultSet rsTerapi = null; // New ResultSet for terapi_id
        ResultSet rsJadwalDokter = null; // New ResultSet for dokter schedule

        try {
            conn = DBConnection.connect();
            if (conn == null) {
                throw new SQLException("Failed to connect to database.");
            }
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert into 'pasien' table
            String sqlPasien = "INSERT INTO pasien (nama_lengkap, alamat, no_telepon, tanggal_lahir, jenis_kelamin, dokter_id) VALUES (?, ?, ?, ?, ?, ?)";
            pstmtPasien = conn.prepareStatement(sqlPasien, Statement.RETURN_GENERATED_KEYS);
            pstmtPasien.setString(1, pasien.getNama());
            pstmtPasien.setString(2, pasien.getAlamat());
            pstmtPasien.setString(3, pasien.getTelepon());
            pstmtPasien.setDate(4, Date.valueOf(pasien.getTanggalLahir()));
            pstmtPasien.setString(5, pasien.getKelamin());
            pstmtPasien.setInt(6, pasien.getDokterId());
            pstmtPasien.executeUpdate();

            rsPasien = pstmtPasien.getGeneratedKeys();
            int pasienId = -1;
            if (rsPasien.next()) {
                pasienId = rsPasien.getInt(1); // Get the auto-generated pasien_id
            } else {
                throw new SQLException("Creating pasien failed, no ID obtained.");
            }
            pasien.setId(pasienId); // Set ID back to the patient object

            // 2. Insert into 'diagnosa' table
            String sqlDiagnosa = "INSERT INTO diagnosa (pasien_id, diagnosa, histopatologi) VALUES (?, ?, ?)";
            pstmtDiagnosa = conn.prepareStatement(sqlDiagnosa);
            pstmtDiagnosa.setInt(1, pasienId);
            pstmtDiagnosa.setString(2, pasien.getDiagnosa());
            pstmtDiagnosa.setString(3, pasien.getHistopatologi());
            pstmtDiagnosa.executeUpdate();

            // 3. Insert into 'periksa_fisik' table
            String sqlPeriksaFisik = "INSERT INTO periksa_fisik (pasien_id, tekanan_darah, suhu_tubuh, denyut_nadi, berat_badan, hb, leukosit, trombosit, sgot_sgpt, ureum_kreatinin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmtPeriksaFisik = conn.prepareStatement(sqlPeriksaFisik);
            pstmtPeriksaFisik.setInt(1, pasienId);
            pstmtPeriksaFisik.setString(2, pasien.getTekananDarah());
            pstmtPeriksaFisik.setString(3, pasien.getSuhuTubuh());
            pstmtPeriksaFisik.setString(4, pasien.getDenyutNadi());
            pstmtPeriksaFisik.setString(5, pasien.getBeratBadan());
            pstmtPeriksaFisik.setString(6, pasien.getHb());
            pstmtPeriksaFisik.setString(7, pasien.getLeukosit());
            pstmtPeriksaFisik.setString(8, pasien.getTrombosit());
            pstmtPeriksaFisik.setString(9, pasien.getFungsiHati());
            pstmtPeriksaFisik.setString(10, pasien.getFungsiGinjal());
            pstmtPeriksaFisik.executeUpdate();

            // 4. Insert into 'rencana_terapi' table
            String sqlRencanaTerapi = "INSERT INTO rencana_terapi (pasien_id, jenis_kemoterapi, dosis, siklus, premedikasi, akses_vena, dokter_id, tanggal_dibuat) VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE())";
            pstmtRencanaTerapi = conn.prepareStatement(sqlRencanaTerapi, Statement.RETURN_GENERATED_KEYS); // Get generated keys
            pstmtRencanaTerapi.setInt(1, pasienId);
            pstmtRencanaTerapi.setString(2, pasien.getJenisKemoterapi());
            pstmtRencanaTerapi.setString(3, pasien.getDosis());
            pstmtRencanaTerapi.setString(4, pasien.getSiklus());
            pstmtRencanaTerapi.setString(5, pasien.getPremedikasi());
            pstmtRencanaTerapi.setString(6, pasien.getAksesVena());
            pstmtRencanaTerapi.setInt(7, pasien.getDokterId());
            pstmtRencanaTerapi.executeUpdate();

            rsTerapi = pstmtRencanaTerapi.getGeneratedKeys();
            int terapiId = -1;
            if (rsTerapi.next()) {
                terapiId = rsTerapi.getInt(1);
            } else {
                throw new SQLException("Creating rencana_terapi failed, no ID obtained.");
            }

            // 5. Insert the FIRST session into 'jadwal_terapi'
            // Determine the current date and its DayOfWeek
            LocalDate firstSessionDate = LocalDate.now(); // Since tanggal_dibuat is CURDATE()
            // Convert Java DayOfWeek to the String format used in your DB for comparison
            String dayOfWeekString = firstSessionDate.getDayOfWeek().toString();

            // Get the earliest available slot for the assigned doctor on this day
            // Ensure the 'hari' in DB uses consistent casing, e.g., 'SENIN'
            String sqlJadwalDokter = "SELECT jadwal_id, jam_mulai FROM jadwal_dokter WHERE dokter_id = ? AND hari = ? ORDER BY jam_mulai LIMIT 1";
            PreparedStatement pstmtFindJadwalDokter = conn.prepareStatement(sqlJadwalDokter);
            pstmtFindJadwalDokter.setInt(1, pasien.getDokterId());
            // Use the mapped DayOfWeek string that matches your DB values
            // (assuming your DB stores them as uppercase Indonesian names, e.g., "SENIN")
            String dbDayName = "";
            for (Map.Entry<String, DayOfWeek> entry : DAY_NAME_MAP.entrySet()) {
                if (entry.getValue() == firstSessionDate.getDayOfWeek()) {
                    dbDayName = entry.getKey();
                    break;
                }
            }
            pstmtFindJadwalDokter.setString(2, dbDayName); // Use the correct DB string representation
            rsJadwalDokter = pstmtFindJadwalDokter.executeQuery();

            int jadwalDokterId = -1;
            LocalTime jamTerapi = null;
            if (rsJadwalDokter.next()) {
                jadwalDokterId = rsJadwalDokter.getInt("jadwal_id");
                jamTerapi = rsJadwalDokter.getTime("jam_mulai").toLocalTime();
            } else {
                System.err.println("No doctor schedule found for current date's day of week for selected doctor. First therapy session not scheduled.");
            }

            if (jadwalDokterId != -1 && jamTerapi != null) {
                String sqlJadwalTerapi = "INSERT INTO jadwal_terapi (jadwal_dokter_id, terapi_id, sesi_ke, tanggal_terapi, jam_terapi, ruangan) VALUES (?, ?, ?, ?, ?, ?)";
                pstmtJadwalTerapi = conn.prepareStatement(sqlJadwalTerapi);
                pstmtJadwalTerapi.setInt(1, jadwalDokterId);
                pstmtJadwalTerapi.setInt(2, terapiId);
                pstmtJadwalTerapi.setInt(3, 1); // First session is sesi_ke 1
                pstmtJadwalTerapi.setDate(4, Date.valueOf(firstSessionDate));
                pstmtJadwalTerapi.setTime(5, Time.valueOf(jamTerapi));
                pstmtJadwalTerapi.setString(6, "RJ-1"); // Default room
                pstmtJadwalTerapi.executeUpdate();
            }

            conn.commit(); // Commit transaction if all inserts are successful

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Gagal menambahkan pasien (Transaksi dibatalkan): " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rsPasien != null) rsPasien.close();
                if (rsTerapi != null) rsTerapi.close();
                if (rsJadwalDokter != null) rsJadwalDokter.close();
                if (pstmtPasien != null) pstmtPasien.close();
                if (pstmtDiagnosa != null) pstmtDiagnosa.close();
                if (pstmtPeriksaFisik != null) pstmtPeriksaFisik.close();
                if (pstmtRencanaTerapi != null) pstmtRencanaTerapi.close();
                if (pstmtJadwalTerapi != null) pstmtJadwalTerapi.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // READ - Ambil Semua Pasien (untuk daftar/list)
    public static List<Pasien> getAllPasien() {
        List<Pasien> list = new ArrayList<>();
        // Modified SQL: JOIN with dokter table to get doctor's name
        String sql = "SELECT p.pasien_id, p.nama_lengkap, p.no_telepon, d.nama AS dokter_nama " +
                     "FROM pasien p JOIN dokter d ON p.dokter_id = d.dokter_id";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pasien p = new Pasien();
                p.setId(rs.getInt("pasien_id"));
                p.setNama(rs.getString("nama_lengkap"));
                p.setTelepon(rs.getString("no_telepon"));
                p.setDosis(rs.getString("dokter_nama")); // Using 'dosis' temporarily to pass doctor name
                list.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data pasien: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // READ - Ambil Detail Pasien berdasarkan ID (requires joining tables)
    public static Pasien getPasienById(int pasienId) {
        Pasien p = null;
        String sql = "SELECT p.pasien_id, p.nama_lengkap, p.alamat, p.no_telepon, p.tanggal_lahir, p.jenis_kelamin, p.dokter_id, " +
                     "d.nama AS dokter_nama, diag.diagnosa, diag.histopatologi, " +
                     "pf.tekanan_darah, pf.suhu_tubuh, pf.denyut_nadi, pf.berat_badan, pf.hb, pf.leukosit, pf.trombosit, pf.sgot_sgpt, pf.ureum_kreatinin, " +
                     "rt.jenis_kemoterapi, rt.dosis, rt.siklus, rt.premedikasi, rt.akses_vena, rt.tanggal_dibuat " + // Added rt.tanggal_dibuat
                     "FROM pasien p " +
                     "LEFT JOIN dokter d ON p.dokter_id = d.dokter_id " +
                     "LEFT JOIN diagnosa diag ON p.pasien_id = diag.pasien_id " +
                     "LEFT JOIN periksa_fisik pf ON p.pasien_id = pf.pasien_id " +
                     "LEFT JOIN rencana_terapi rt ON p.pasien_id = rt.pasien_id " +
                     "WHERE p.pasien_id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pasienId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                p = new Pasien();
                p.setId(rs.getInt("pasien_id"));
                p.setNama(rs.getString("nama_lengkap"));
                p.setAlamat(rs.getString("alamat"));
                p.setTelepon(rs.getString("no_telepon"));
                p.setTanggalLahir(rs.getDate("tanggal_lahir").toLocalDate());
                p.setKelamin(rs.getString("jenis_kelamin"));
                p.setDokterId(rs.getInt("dokter_id"));
                p.setDokterNama(rs.getString("dokter_nama"));

                // Data from diagnosa table
                p.setDiagnosa(rs.getString("diagnosa"));
                p.setHistopatologi(rs.getString("histopatologi"));

                // Data from periksa_fisik table
                p.setTekananDarah(rs.getString("tekanan_darah"));
                p.setSuhuTubuh(rs.getString("suhu_tubuh"));
                p.setDenyutNadi(rs.getString("denyut_nadi"));
                p.setBeratBadan(rs.getString("berat_badan"));
                p.setHb(rs.getString("hb"));
                p.setLeukosit(rs.getString("leukosit"));
                p.setTrombosit(rs.getString("trombosit"));
                p.setFungsiHati(rs.getString("sgot_sgpt"));
                p.setFungsiGinjal(rs.getString("ureum_kreatinin"));

                // Data from rencana_terapi table
                p.setJenisKemoterapi(rs.getString("jenis_kemoterapi"));
                p.setDosis(rs.getString("dosis"));
                p.setSiklus(rs.getString("siklus"));
                p.setPremedikasi(rs.getString("premedikasi"));
                p.setAksesVena(rs.getString("akses_vena"));
                // Added for scheduling logic
                p.setTanggalDibuatRencanaTerapi(rs.getDate("tanggal_dibuat") != null ? rs.getDate("tanggal_dibuat").toLocalDate() : null);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil detail pasien: " + e.getMessage());
            e.printStackTrace();
        }
        return p;
    }

    // NEW METHOD: Get Doctor's Schedule
    public static List<DokterJadwal> getDokterSchedules(int dokterId) {
        List<DokterJadwal> schedules = new ArrayList<>();
        String sql = "SELECT jadwal_id, dokter_id, hari, jam_mulai, jam_selesai FROM jadwal_dokter WHERE dokter_id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, dokterId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int jadwalId = rs.getInt("jadwal_id");
                String hariStr = rs.getString("hari");
                LocalTime jamMulai = rs.getTime("jam_mulai").toLocalTime();
                LocalTime jamSelesai = rs.getTime("jam_selesai").toLocalTime();

                // Convert String day from DB (Indonesian uppercase) to DayOfWeek enum
                DayOfWeek dayOfWeek = DAY_NAME_MAP.get(hariStr.toUpperCase());

                if (dayOfWeek != null) {
                    schedules.add(new DokterJadwal(jadwalId, dokterId, dayOfWeek, jamMulai, jamSelesai));
                } else {
                    System.err.println("Invalid DayOfWeek string from DB (unmapped): " + hariStr);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil jadwal dokter: " + e.getMessage());
            e.printStackTrace();
        }
        return schedules;
    }
    
    // NEW METHOD: Delete Pasien
    public static boolean deletePasien(int pasienId) {
        Connection conn = null;
        try {
            conn = DBConnection.connect();
            if (conn == null) {
                throw new SQLException("Failed to connect to database.");
            }
            conn.setAutoCommit(false); // Start transaction

            // IMPORTANT: Delete in reverse order of foreign key dependencies
            // 1. Delete from evaluasi_kemo (depends on jadwal_terapi)
            String sqlDeleteEvaluasi = "DELETE FROM evaluasi_kemo WHERE jadwal_id IN (SELECT jadwal_id FROM jadwal_terapi WHERE terapi_id IN (SELECT terapi_id FROM rencana_terapi WHERE pasien_id = ?))";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteEvaluasi)) {
                pstmt.setInt(1, pasienId);
                pstmt.executeUpdate();
            }

            // 2. Delete from jadwal_terapi (depends on rencana_terapi)
            String sqlDeleteJadwalTerapi = "DELETE FROM jadwal_terapi WHERE terapi_id IN (SELECT terapi_id FROM rencana_terapi WHERE pasien_id = ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteJadwalTerapi)) {
                pstmt.setInt(1, pasienId);
                pstmt.executeUpdate();
            }

            // 3. Delete from rencana_terapi (depends on pasien)
            String sqlDeleteRencanaTerapi = "DELETE FROM rencana_terapi WHERE pasien_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteRencanaTerapi)) {
                pstmt.setInt(1, pasienId);
                pstmt.executeUpdate();
            }

            // 4. Delete from periksa_fisik (depends on pasien)
            String sqlDeletePeriksaFisik = "DELETE FROM periksa_fisik WHERE pasien_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeletePeriksaFisik)) {
                pstmt.setInt(1, pasienId);
                pstmt.executeUpdate();
            }

            // 5. Delete from diagnosa (depends on pasien)
            String sqlDeleteDiagnosa = "DELETE FROM diagnosa WHERE pasien_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDiagnosa)) {
                pstmt.setInt(1, pasienId);
                pstmt.executeUpdate();
            }

            // 6. Finally, delete from pasien
            String sqlDeletePasien = "DELETE FROM pasien WHERE pasien_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeletePasien)) {
                pstmt.setInt(1, pasienId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit(); // Commit transaction
                    return true;
                }
            }
            conn.rollback(); // If no rows affected for pasien, rollback
            return false;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Gagal menghapus pasien (Transaksi dibatalkan): " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}