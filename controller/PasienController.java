package PBO_4C_SI_KELOMPOK_7.controller;
import db.DBConnection;
import model.Pasien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasienController {

    // CREATE - Tambah Pasien
    public static void tambahPasien(Pasien pasien) {
        String sql = "INSERT INTO pasien (nama, alamat, telepon, tanggal_lahir, kelamin, diagnosa, histopatologi, " +
                     "tekanan_darah, suhu_tubuh, denyut_nadi, berat_badan, hb, leukosit, trombosit, fungsi_hati, fungsi_ginjal, " +
                     "jenis_kemoterapi, dosis, siklus, premedikasi, akses_vena, dokter_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pasien.getNama());
            stmt.setString(2, pasien.getAlamat());
            stmt.setString(3, pasien.getTelepon());
            stmt.setDate(4, Date.valueOf(pasien.getTanggalLahir()));
            stmt.setString(5, pasien.getKelamin());
            stmt.setString(6, pasien.getDiagnosa());
            stmt.setString(7, pasien.getHistopatologi());

            stmt.setString(8, pasien.getTekananDarah());
            stmt.setString(9, pasien.getSuhuTubuh());
            stmt.setString(10, pasien.getDenyutNadi());
            stmt.setString(11, pasien.getBeratBadan());
            stmt.setString(12, pasien.getHb());
            stmt.setString(13, pasien.getLeukosit());
            stmt.setString(14, pasien.getTrombosit());
            stmt.setString(15, pasien.getFungsiHati());
            stmt.setString(16, pasien.getFungsiGinjal());

            stmt.setString(17, pasien.getJenisKemoterapi());
            stmt.setString(18, pasien.getDosis());
            stmt.setString(19, pasien.getSiklus());
            stmt.setString(20, pasien.getPremedikasi());
            stmt.setString(21, pasien.getAksesVena());
            stmt.setInt(22, pasien.getDokterId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menambahkan pasien: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // READ - Ambil Semua Pasien (untuk daftar/list)
    public static List<Pasien> getAllPasien() {
        List<Pasien> list = new ArrayList<>();
        String sql = "SELECT * FROM pasien";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pasien p = new Pasien();
                p.setId(rs.getInt("id"));
                p.setNama(rs.getString("nama"));
                p.setTelepon(rs.getString("telepon"));
                p.setDokterId(rs.getInt("dokter_id"));
                // Tambahkan field lain jika ingin ditampilkan di list
                list.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data pasien: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // READ - Ambil Detail Pasien berdasarkan ID
    public static Pasien getPasienById(int id) {
        Pasien p = null;
        String sql = "SELECT * FROM pasien WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Pasien();
                p.setId(rs.getInt("id"));
                p.setNama(rs.getString("nama"));
                p.setAlamat(rs.getString("alamat"));
                p.setTelepon(rs.getString("telepon"));
                p.setTanggalLahir(rs.getDate("tanggal_lahir").toLocalDate());
                p.setKelamin(rs.getString("kelamin"));
                p.setDiagnosa(rs.getString("diagnosa"));
                p.setHistopatologi(rs.getString("histopatologi"));

                p.setTekananDarah(rs.getString("tekanan_darah"));
                p.setSuhuTubuh(rs.getString("suhu_tubuh"));
                p.setDenyutNadi(rs.getString("denyut_nadi"));
                p.setBeratBadan(rs.getString("berat_badan"));
                p.setHb(rs.getString("hb"));
                p.setLeukosit(rs.getString("leukosit"));
                p.setTrombosit(rs.getString("trombosit"));
                p.setFungsiHati(rs.getString("fungsi_hati"));
                p.setFungsiGinjal(rs.getString("fungsi_ginjal"));

                p.setJenisKemoterapi(rs.getString("jenis_kemoterapi"));
                p.setDosis(rs.getString("dosis"));
                p.setSiklus(rs.getString("siklus"));
                p.setPremedikasi(rs.getString("premedikasi"));
                p.setAksesVena(rs.getString("akses_vena"));
                p.setDokterId(rs.getInt("dokter_id"));
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil detail pasien: " + e.getMessage());
            e.printStackTrace();
        }
        return p;
    }
}