package PBO_4C_SI_KELOMPOK_7.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DokterJadwal {
    private int jadwalId;
    private int dokterId;
    private DayOfWeek hari; // Use Java's DayOfWeek enum
    private LocalTime jamMulai;
    private LocalTime jamSelesai;

    // Constructors
    public DokterJadwal(int jadwalId, int dokterId, DayOfWeek hari, LocalTime jamMulai, LocalTime jamSelesai) {
        this.jadwalId = jadwalId;
        this.dokterId = dokterId;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    // Getters
    public int getJadwalId() {
        return jadwalId;
    }

    public int getDokterId() {
        return dokterId;
    }

    public DayOfWeek getHari() {
        return hari;
    }

    public LocalTime getJamMulai() {
        return jamMulai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai;
    }

    // Setters (optional, if you create objects then set)
    public void setJadwalId(int jadwalId) {
        this.jadwalId = jadwalId;
    }

    public void setDokterId(int dokterId) {
        this.dokterId = dokterId;
    }

    public void setHari(DayOfWeek hari) {
        this.hari = hari;
    }

    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
    }

    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }
}