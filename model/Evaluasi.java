package PBO_4C_SI_KELOMPOK_7.model;

import java.sql.Timestamp;

public class Evaluasi {

    // Kolom dari tabel evaluasi_kemo
    private int evaluasiId;
    private int jadwalId;
    private String kondisiPostTerapi;
    private String efekSamping;
    private String catatan;
    private Timestamp tanggalEvaluasi;

    // Kolom tambahan hasil JOIN untuk ditampilkan di View
    private String namaPasien;
    private String namaDokter;
    private int sesiKe;

    // --- GETTERS & SETTERS ---

    public int getEvaluasiId() {
        return evaluasiId;
    }

    public void setEvaluasiId(int evaluasiId) {
        this.evaluasiId = evaluasiId;
    }

    public int getJadwalId() {
        return jadwalId;
    }

    public void setJadwalId(int jadwalId) {
        this.jadwalId = jadwalId;
    }

    public String getKondisiPostTerapi() {
        return kondisiPostTerapi;
    }

    public void setKondisiPostTerapi(String kondisiPostTerapi) {
        this.kondisiPostTerapi = kondisiPostTerapi;
    }

    public String getEfekSamping() {
        return efekSamping;
    }

    public void setEfekSamping(String efekSamping) {
        this.efekSamping = efekSamping;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public Timestamp getTanggalEvaluasi() {
        return tanggalEvaluasi;
    }

    public void setTanggalEvaluasi(Timestamp tanggalEvaluasi) {
        this.tanggalEvaluasi = tanggalEvaluasi;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getNamaDokter() {
        return namaDokter;
    }

    public void setNamaDokter(String namaDokter) {
        this.namaDokter = namaDokter;
    }

    public int getSesiKe() {
        return sesiKe;
    }

    public void setSesiKe(int sesiKe) {
        this.sesiKe = sesiKe;
    }
}