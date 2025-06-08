package controller;

import db.DBConnection;
import model.Pasien; 

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PasienController {
    public static void tambahPasien(Pasien pasien) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "INSERT INTO pasien (nama, alamat, telepon, tanggal_lahir, kelamin, diagnosa, histopatologi, dokter_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, pasien.getNama());
            stmt.setString(2, pasien.getAlamat());
            stmt.setString(3, pasien.getTelepon());
            stmt.setDate(4, java.sql.Date.valueOf(pasien.getTanggalLahir()));
            stmt.setString(5, pasien.getKelamin());
            stmt.setString(6, pasien.getDiagnosa());
            stmt.setString(7, pasien.getHistopatologi());
            stmt.setInt(8, pasien.getDokterId());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}