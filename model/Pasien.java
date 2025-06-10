package PBO_4C_SI_KELOMPOK_7.model;

import java.time.LocalDate;

public class Pasien {
    // Data Pasien
    private int id;
    private String nama;
    private String alamat;
    private String telepon;
    private LocalDate tanggalLahir;
    private String kelamin;
    private String diagnosa;
    private String histopatologi;

    // Pemeriksaan Fisik
    private String tekananDarah;
    private String suhuTubuh;
    private String denyutNadi;
    private String beratBadan;
    private String hb;
    private String leukosit;
    private String trombosit;
    private String fungsiHati;
    private String fungsiGinjal;

    // Rencana Terapi
    private String jenisKemoterapi;
    private String dosis;
    private String siklus;
    private String premedikasi;
    private String aksesVena;
    private int dokterId;

    private String dokterNama;

    // --- GETTERS & SETTERS ---

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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getKelamin() {
        return kelamin;
    }

    public void setKelamin(String kelamin) {
        this.kelamin = kelamin;
    }

    public String getDiagnosa() {
        return diagnosa;
    }

    public void setDiagnosa(String diagnosa) {
        this.diagnosa = diagnosa;
    }

    public String getHistopatologi() {
        return histopatologi;
    }

    public void setHistopatologi(String histopatologi) {
        this.histopatologi = histopatologi;
    }

    public String getTekananDarah() {
        return tekananDarah;
    }

    public void setTekananDarah(String tekananDarah) {
        this.tekananDarah = tekananDarah;
    }

    public String getSuhuTubuh() {
        return suhuTubuh;
    }

    public void setSuhuTubuh(String suhuTubuh) {
        this.suhuTubuh = suhuTubuh;
    }

    public String getDenyutNadi() {
        return denyutNadi;
    }

    public void setDenyutNadi(String denyutNadi) {
        this.denyutNadi = denyutNadi;
    }

    public String getBeratBadan() {
        return beratBadan;
    }

    public void setBeratBadan(String beratBadan) {
        this.beratBadan = beratBadan;
    }

    public String getHb() {
        return hb;
    }

    public void setHb(String hb) {
        this.hb = hb;
    }

    public String getLeukosit() {
        return leukosit;
    }

    public void setLeukosit(String leukosit) {
        this.leukosit = leukosit;
    }

    public String getTrombosit() {
        return trombosit;
    }

    public void setTrombosit(String trombosit) {
        this.trombosit = trombosit;
    }

    public String getFungsiHati() {
        return fungsiHati;
    }

    public void setFungsiHati(String fungsiHati) {
        this.fungsiHati = fungsiHati;
    }

    public String getFungsiGinjal() {
        return fungsiGinjal;
    }

    public void setFungsiGinjal(String fungsiGinjal) {
        this.fungsiGinjal = fungsiGinjal;
    }

    public String getJenisKemoterapi() {
        return jenisKemoterapi;
    }

    public void setJenisKemoterapi(String jenisKemoterapi) {
        this.jenisKemoterapi = jenisKemoterapi;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getSiklus() {
        return siklus;
    }

    public void setSiklus(String siklus) {
        this.siklus = siklus;
    }

    public String getPremedikasi() {
        return premedikasi;
    }

    public void setPremedikasi(String premedikasi) {
        this.premedikasi = premedikasi;
    }

    public String getAksesVena() {
        return aksesVena;
    }

    public void setAksesVena(String aksesVena) {
        this.aksesVena = aksesVena;
    }

    public int getDokterId() {
        return dokterId;
    }

    public void setDokterId(int dokterId) {
        this.dokterId = dokterId;
    }

    public String getDokterNama() {
        return dokterNama;
    }

    public void setDokterNama(String dokterNama) {
        this.dokterNama = dokterNama;
    }
}
