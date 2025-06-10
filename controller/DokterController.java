// PBO_4C_SI_KELOMPOK_7/controller/DokterController.java
package PBO_4C_SI_KELOMPOK_7.controller;

import PBO_4C_SI_KELOMPOK_7.db.DBConnection;
import PBO_4C_SI_KELOMPOK_7.model.Dokter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DokterController {

    // Method to get all doctors with their schedules
    public static List<Dokter> getAllDokter() {
        List<Dokter> dokterList = new ArrayList<>();
        // Query to get doctor details and then their schedules
        String sql = "SELECT d.dokter_id, d.nama, d.spesialisasi, d.pendidikan, d.legalitas, " +
                     "jd.hari, jd.jam_mulai, jd.jam_selesai " +
                     "FROM dokter d " +
                     "LEFT JOIN jadwal_dokter jd ON d.dokter_id = jd.dokter_id " +
                     "ORDER BY d.nama, jd.hari, jd.jam_mulai";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            Dokter currentDokter = null;
            while (rs.next()) {
                int dokterId = rs.getInt("dokter_id");

                // Check if it's a new doctor or a new schedule for the same doctor
                if (currentDokter == null || currentDokter.getId() != dokterId) {
                    if (currentDokter != null) {
                        dokterList.add(currentDokter);
                    }
                    currentDokter = new Dokter();
                    currentDokter.setId(dokterId);
                    currentDokter.setNama(rs.getString("nama"));
                    currentDokter.setSpesialisasi(rs.getString("spesialisasi"));
                    currentDokter.setPendidikan(rs.getString("pendidikan"));
                    currentDokter.setLegalitas(rs.getString("legalitas"));
                }

                String hari = rs.getString("hari");
                String jamMulai = rs.getString("jam_mulai");
                String jamSelesai = rs.getString("jam_selesai");

                if (hari != null && jamMulai != null && jamSelesai != null) {
                    currentDokter.addJadwal(String.format("%s (%s-%s)", hari, jamMulai.substring(0, 5), jamSelesai.substring(0, 5)));
                }
            }
            // Add the last doctor to the list
            if (currentDokter != null) {
                dokterList.add(currentDokter);
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil data dokter: " + e.getMessage());
            e.printStackTrace();
        }
        return dokterList;
    }

    // Method to get a single doctor's details with their schedules
    public static Dokter getDokterById(int dokterId) {
        Dokter dokter = null;
        String sql = "SELECT d.dokter_id, d.nama, d.spesialisasi, d.pendidikan, d.legalitas, " +
                     "jd.hari, jd.jam_mulai, jd.jam_selesai " +
                     "FROM dokter d " +
                     "LEFT JOIN jadwal_dokter jd ON d.dokter_id = jd.dokter_id " +
                     "WHERE d.dokter_id = ? " +
                     "ORDER BY jd.hari, jd.jam_mulai";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, dokterId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (dokter == null) {
                    dokter = new Dokter();
                    dokter.setId(rs.getInt("dokter_id"));
                    dokter.setNama(rs.getString("nama"));
                    dokter.setSpesialisasi(rs.getString("spesialisasi"));
                    dokter.setPendidikan(rs.getString("pendidikan"));
                    dokter.setLegalitas(rs.getString("legalitas"));
                }

                String hari = rs.getString("hari");
                String jamMulai = rs.getString("jam_mulai");
                String jamSelesai = rs.getString("jam_selesai");

                if (hari != null && jamMulai != null && jamSelesai != null) {
                    dokter.addJadwal(String.format("%s (%s-%s)", hari, jamMulai.substring(0, 5), jamSelesai.substring(0, 5)));
                }
            }

        } catch (SQLException e) {
            System.err.println("Gagal mengambil detail dokter: " + e.getMessage());
            e.printStackTrace();
        }
        return dokter;
    }
}