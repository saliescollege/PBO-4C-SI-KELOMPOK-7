// PBO_4C_SI_KELOMPOK_7/model/Dokter.java
package PBO_4C_SI_KELOMPOK_7.model;

import java.util.List;
import java.util.ArrayList;

public class Dokter {
    private int id;
    private String nama;
    private String spesialisasi;
    private String pendidikan;
    private String legalitas;
    private List<String> jadwal; // List to hold formatted schedule strings

    public Dokter() {
        this.jadwal = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = spesialisasi;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public String getLegalitas() {
        return legalitas;
    }

    public void setLegalitas(String legalitas) {
        this.legalitas = legalitas;
    }

    public List<String> getJadwal() {
        return jadwal;
    }

    public void setJadwal(List<String> jadwal) {
        this.jadwal = jadwal;
    }

    public void addJadwal(String entry) {
        this.jadwal.add(entry);
    }
}