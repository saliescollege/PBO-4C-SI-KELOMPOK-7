package PBO_4C_SI_KELOMPOK_7.controller;

import PBO_4C_SI_KELOMPOK_7.db.DBConnection;
import PBO_4C_SI_KELOMPOK_7.model.DokterJadwal;
import PBO_4C_SI_KELOMPOK_7.model.Pasien;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasienController {

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
    
    public static void tambahPasien(Pasien pasien) {
        Connection conn = null;
        PreparedStatement pstmtPasien = null;
        PreparedStatement pstmtDiagnosa = null;
        PreparedStatement pstmtPeriksaFisik = null;
        PreparedStatement pstmtRencanaTerapi = null;
        ResultSet rsPasien = null;

        try {
            conn = DBConnection.connect();
            if (conn == null) {
                throw new SQLException("Failed to connect to database.");
            }
            conn.setAutoCommit(false);

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
                pasienId = rsPasien.getInt(1);
            } else {
                throw new SQLException("Creating pasien failed, no ID obtained.");
            }
            pasien.setId(pasienId);

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
            pstmtRencanaTerapi = conn.prepareStatement(sqlRencanaTerapi);
            pstmtRencanaTerapi.setInt(1, pasienId);
            pstmtRencanaTerapi.setString(2, pasien.getJenisKemoterapi());
            pstmtRencanaTerapi.setString(3, pasien.getDosis());
            
            // --- PERBAIKAN TIPE DATA SIKLUS ---
            String siklusStr = pasien.getSiklus();
            int siklusInt = 0;
            if (siklusStr != null && !siklusStr.trim().isEmpty()) {
                try {
                    // Ambil hanya angka dari string (contoh: "21 hari" -> 21)
                    siklusInt = Integer.parseInt(siklusStr.replaceAll("[^0-9]", ""));
                } catch (NumberFormatException e) {
                    System.err.println("Format siklus tidak valid: " + siklusStr + ". Menggunakan nilai default 0.");
                }
            }
            pstmtRencanaTerapi.setInt(4, siklusInt); // Simpan sebagai integer
            
            pstmtRencanaTerapi.setString(5, pasien.getPremedikasi());
            pstmtRencanaTerapi.setString(6, pasien.getAksesVena());
            pstmtRencanaTerapi.setInt(7, pasien.getDokterId());
            pstmtRencanaTerapi.executeUpdate();
            
            conn.commit();

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback failed: " + ex.getMessage());
            }
            System.err.println("Gagal menambahkan pasien (Transaksi dibatalkan): " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rsPasien != null) rsPasien.close();
                if (pstmtPasien != null) pstmtPasien.close();
                if (pstmtDiagnosa != null) pstmtDiagnosa.close();
                if (pstmtPeriksaFisik != null) pstmtPeriksaFisik.close();
                if (pstmtRencanaTerapi != null) pstmtRencanaTerapi.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static List<Pasien> getAllPasien() {
        List<Pasien> list = new ArrayList<>();
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
                p.setDosis(rs.getString("dokter_nama"));
                list.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data pasien: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static Pasien getPasienById(int pasienId) {
        Pasien p = null;
        String sql = "SELECT p.pasien_id, p.nama_lengkap, p.alamat, p.no_telepon, p.tanggal_lahir, p.jenis_kelamin, p.dokter_id, " +
                     "d.nama AS dokter_nama, diag.diagnosa, diag.histopatologi, " +
                     "pf.tekanan_darah, pf.suhu_tubuh, pf.denyut_nadi, pf.berat_badan, pf.hb, pf.leukosit, pf.trombosit, pf.sgot_sgpt, pf.ureum_kreatinin, " +
                     "rt.jenis_kemoterapi, rt.dosis, rt.siklus, rt.premedikasi, rt.akses_vena, rt.tanggal_dibuat " +
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
                p.setDiagnosa(rs.getString("diagnosa"));
                p.setHistopatologi(rs.getString("histopatologi"));
                p.setTekananDarah(rs.getString("tekanan_darah"));
                p.setSuhuTubuh(rs.getString("suhu_tubuh"));
                p.setDenyutNadi(rs.getString("denyut_nadi"));
                p.setBeratBadan(rs.getString("berat_badan"));
                p.setHb(rs.getString("hb"));
                p.setLeukosit(rs.getString("leukosit"));
                p.setTrombosit(rs.getString("trombosit"));
                p.setFungsiHati(rs.getString("sgot_sgpt"));
                p.setFungsiGinjal(rs.getString("ureum_kreatinin"));
                p.setJenisKemoterapi(rs.getString("jenis_kemoterapi"));
                p.setDosis(rs.getString("dosis"));
                p.setSiklus(rs.getString("siklus"));
                p.setPremedikasi(rs.getString("premedikasi"));
                p.setAksesVena(rs.getString("akses_vena"));
                p.setTanggalDibuatRencanaTerapi(rs.getDate("tanggal_dibuat") != null ? rs.getDate("tanggal_dibuat").toLocalDate() : null);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil detail pasien: " + e.getMessage());
            e.printStackTrace();
        }
        return p;
    }

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
    
    public static boolean deletePasien(int pasienId) {
        Connection conn = null;
        try {
            conn = DBConnection.connect();
            if (conn == null) {
                throw new SQLException("Failed to connect to database.");
            }
            conn.setAutoCommit(false);

            String sqlDeleteEvaluasi = "DELETE FROM evaluasi_kemo WHERE jadwal_id IN (SELECT jadwal_id FROM jadwal_terapi WHERE terapi_id IN (SELECT terapi_id FROM rencana_terapi WHERE pasien_id = ?))";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteEvaluasi)) {
                pstmt.setInt(1, pasienId);
                pstmt.executeUpdate();
            }

            String sqlDeleteJadwalTerapi = "DELETE FROM jadwal_terapi WHERE terapi_id IN (SELECT terapi_id FROM rencana_terapi WHERE pasien_id = ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteJadwalTerapi)) {
                pstmt.setInt(1, pasienId);
                pstmt.executeUpdate();
            }

            String sqlDeleteRencanaTerapi = "DELETE FROM rencana_terapi WHERE pasien_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteRencanaTerapi)) {
                pstmt.setInt(1, pasienId);
                pstmt.executeUpdate();
            }

            String sqlDeletePeriksaFisik = "DELETE FROM periksa_fisik WHERE pasien_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeletePeriksaFisik)) {
                pstmt.setInt(1, pasienId);
                pstmt.executeUpdate();
            }

            String sqlDeleteDiagnosa = "DELETE FROM diagnosa WHERE pasien_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDiagnosa)) {
                pstmt.setInt(1, pasienId);
                pstmt.executeUpdate();
            }

            String sqlDeletePasien = "DELETE FROM pasien WHERE pasien_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeletePasien)) {
                pstmt.setInt(1, pasienId);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    return true;
                }
            }
            conn.rollback();
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

    public static List<Pasien> getPasienByDokterId(int dokterId) {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT pasien_id, nama_lengkap, no_telepon FROM pasien WHERE dokter_id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, dokterId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Pasien p = new Pasien();
                p.setId(rs.getInt("pasien_id"));
                p.setNama(rs.getString("nama_lengkap"));
                p.setTelepon(rs.getString("no_telepon"));
                list.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data pasien berdasarkan dokter: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public static boolean simpanJadwalTerapi(int pasienId, List<LocalDate> jadwalDihasilkan) {
        Connection conn = null;
        int terapiId = -1;

        try (Connection tempConn = DBConnection.connect();
             PreparedStatement pstmt = tempConn.prepareStatement("SELECT terapi_id FROM rencana_terapi WHERE pasien_id = ? ORDER BY tanggal_dibuat DESC LIMIT 1")) {
            pstmt.setInt(1, pasienId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                terapiId = rs.getInt("terapi_id");
            }
        } catch (SQLException e) {
            System.err.println("Gagal mendapatkan terapi_id: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        if (terapiId == -1) {
            System.err.println("Tidak ditemukan rencana terapi untuk pasien dengan ID: " + pasienId);
            return false;
        }

        try {
            conn = DBConnection.connect();
            conn.setAutoCommit(false);

            try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM jadwal_terapi WHERE terapi_id = ? AND tanggal_terapi >= CURDATE()")) {
                deleteStmt.setInt(1, terapiId);
                deleteStmt.executeUpdate();
            }

            String sqlInsert = "INSERT INTO jadwal_terapi (jadwal_dokter_id, terapi_id, sesi_ke, tanggal_terapi, jam_terapi, ruangan) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                
                for (int i = 0; i < jadwalDihasilkan.size(); i++) {
                    LocalDate tanggal = jadwalDihasilkan.get(i);
                    int sesiKe = i + 1;
                    
                    DayOfWeek dayOfWeek = tanggal.getDayOfWeek();
                    String hariIndonesia;

                    switch (dayOfWeek) {
                        case MONDAY: hariIndonesia = "SENIN"; break;
                        case TUESDAY: hariIndonesia = "SELASA"; break;
                        case WEDNESDAY: hariIndonesia = "RABU"; break;
                        case THURSDAY: hariIndonesia = "KAMIS"; break;
                        case FRIDAY: hariIndonesia = "JUMAT"; break;
                        case SATURDAY: hariIndonesia = "SABTU"; break;
                        case SUNDAY: hariIndonesia = "MINGGU"; break;
                        default: continue;
                    }

                    String sqlJadwalDokter = "SELECT jadwal_id, jam_mulai FROM jadwal_dokter jd JOIN rencana_terapi rt ON jd.dokter_id = rt.dokter_id WHERE rt.terapi_id = ? AND jd.hari = ? ORDER BY jd.jam_mulai LIMIT 1";
                    try (PreparedStatement jadwalDokterStmt = conn.prepareStatement(sqlJadwalDokter)) {
                        jadwalDokterStmt.setInt(1, terapiId);
                        jadwalDokterStmt.setString(2, hariIndonesia);
                        ResultSet rsJadwal = jadwalDokterStmt.executeQuery();

                        if (rsJadwal.next()) {
                            int jadwalDokterId = rsJadwal.getInt("jadwal_id");
                            LocalTime jamTerapi = rsJadwal.getTime("jam_mulai").toLocalTime();

                            insertStmt.setInt(1, jadwalDokterId);
                            insertStmt.setInt(2, terapiId);
                            insertStmt.setInt(3, sesiKe);
                            insertStmt.setDate(4, java.sql.Date.valueOf(tanggal));
                            insertStmt.setTime(5, java.sql.Time.valueOf(jamTerapi));
                            insertStmt.setString(6, "RJ-1");
                            insertStmt.addBatch();
                        } else {
                            System.out.println("Tidak ada jadwal dokter untuk hari " + hariIndonesia + " tanggal " + tanggal);
                        }
                    }
                }
                insertStmt.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Gagal menyimpan jadwal terapi ke database. Transaksi dibatalkan.");
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static List<LocalDate> getJadwalTersimpan(int pasienId) {
        List<LocalDate> jadwal = new ArrayList<>();
        String sql = "SELECT jt.tanggal_terapi FROM jadwal_terapi jt " +
                     "JOIN rencana_terapi rt ON jt.terapi_id = rt.terapi_id " +
                     "WHERE rt.pasien_id = ? AND jt.tanggal_terapi >= CURDATE() " +
                     "ORDER BY jt.tanggal_terapi";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pasienId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                jadwal.add(rs.getDate("tanggal_terapi").toLocalDate());
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil jadwal tersimpan: " + e.getMessage());
            e.printStackTrace();
        }
        return jadwal;
    }
}